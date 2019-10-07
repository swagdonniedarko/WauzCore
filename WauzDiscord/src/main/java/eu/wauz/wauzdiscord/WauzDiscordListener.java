package eu.wauz.wauzdiscord;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This Class listens to Bukkit Events, to send Join/Leave Messages to Discord.
 * 
 * @author Wauzmons
 */
public class WauzDiscordListener implements Listener {

	/**
	 * Sends a Join Message to Discord.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onLogin(PlayerLoginEvent event) throws Exception {
		Player player = event.getPlayer();
		WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[+] " + player.getName() + " joined the game!");
	}

	/**
	 * Sends a Leave Message to Discord.
	 * 
	 * @param event
	 */
	@EventHandler
	public void onLogout(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		WauzDiscord.getShiroDiscordBot().sendMessageFromMinecraft("[-] " + player.getName() + " left the game!");
	}
	
}
