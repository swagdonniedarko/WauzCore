package eu.wauz.wauzcore.menu;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import eu.wauz.wauzcore.menu.util.HeadUtils;
import eu.wauz.wauzcore.menu.util.MenuUtils;
import eu.wauz.wauzcore.menu.util.WauzInventory;
import eu.wauz.wauzcore.menu.util.WauzInventoryHolder;
import eu.wauz.wauzcore.mobs.citizens.RelationTracker;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizen;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenInteractions;
import eu.wauz.wauzcore.mobs.citizens.WauzCitizenSpawner;
import net.md_5.bungee.api.ChatColor;

/**
 * An inventory that can be used as menu or for other custom interaction mechanics.
 * A citizen interaction menu, that enables the player to talk to citizen npcs.
 * 
 * @author Wauzmons
 * 
 * @see WauzCitizen
 * @see WauzCitizenSpawner
 */
public class CitizenInteractionMenu implements WauzInventory {
	
	/**
	 * Opens the menu for the given player.
	 * Always shows the relation to the given citizen and a "goodbye" option.
	 * A list of interactions is shown, based on the citizen.
	 * If a the citizen contains a mode selection,
	 * the screen of that gamemode is opened, instead of the menu.
	 * 
	 * @param player The player that should view the inventory.
	 * @param citizen The citizen that should be interacted with.
	 * 
	 * @see WauzCitizenInteractions#createInteractionMenuBase(org.bukkit.inventory.InventoryHolder, String)
	 * @see WauzCitizenInteractions#getModeSelection()
	 * @see WauzModeMenu#selectMenuPoint(InventoryClickEvent)
	 * @see MenuUtils#setBorders(Inventory)
	 */
	public static void open(Player player, WauzCitizen citizen) {
		String modeSelection = citizen.getInteractions().getModeSelection();
		if(StringUtils.isNotBlank(modeSelection)) {
			WauzModeMenu.selectMode(player, modeSelection);
			return;
		}
		
		WauzInventoryHolder holder = new WauzInventoryHolder(new CitizenInteractionMenu(citizen));
		String displayName = citizen.getDisplayName();
		Inventory menu = citizen.getInteractions().createInteractionMenuBase(holder,
				ChatColor.BLACK + "" + ChatColor.BOLD + displayName);
		
		ItemStack citizenItemStack = HeadUtils.getCitizenRelationItem();
		ItemMeta citizenItemMeta = citizenItemStack.getItemMeta();
		citizenItemMeta.setDisplayName(ChatColor.YELLOW + displayName);
		citizenItemMeta.setLore(RelationTracker.generateProgressLore(player, displayName));
		citizenItemStack.setItemMeta(citizenItemMeta);
		menu.setItem(0, citizenItemStack);
		
		ItemStack goodbyeItemStack = HeadUtils.getDeclineItem();
		MenuUtils.setItemDisplayName(goodbyeItemStack, ChatColor.DARK_RED + "Goodbye");
		menu.setItem(8, goodbyeItemStack);
		
		MenuUtils.setBorders(menu);
		player.openInventory(menu);
	}
	
	/**
	 * The citizen that should be interacted with.
	 */
	private WauzCitizen citizen;
	
	/**
	 * Creates a new menu to interact with citizen npcs.
	 * 
	 * @param citizen The citizen that should be interacted with.
	 */
	private CitizenInteractionMenu(WauzCitizen citizen) {
		this.citizen = citizen;
	}

	/**
	 * Checks if an interaction with the citizen was triggered by a player click.
	 * The default event will be automatically canceled.
	 * 
	 * @param event The inventory click event.
	 * 
	 * @see WauzCitizenInteractions#checkForValidInteractions(Player, ItemStack)
	 */
	@Override
	public void selectMenuPoint(InventoryClickEvent event) {
		event.setCancelled(true);
		ItemStack clicked = event.getCurrentItem();
		Player player = (Player) event.getWhoClicked();
		
		if(clicked == null || !clicked.getType().equals(Material.PLAYER_HEAD)) {
			return;
		}
		else if(HeadUtils.isHeadMenuItem(clicked, "Goodbye")) {
			player.closeInventory();
		}
		else {
			citizen.getInteractions().checkForValidInteractions(player, clicked);
		}
	}

}
