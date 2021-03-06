package eu.wauz.wauzcore.system;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eu.wauz.wauzcore.WauzCore;
import eu.wauz.wauzcore.data.QuestConfigurator;

/**
 * A quest, generated from a quest config file.
 * 
 * @author Wauzmons
 */
public class WauzQuest {
	
	/**
	 * A map with lists of quests, indexed by level.
	 */
	private static Map<Integer, List<WauzQuest>> levelQuestsMap = new HashMap<>();
	
	/**
	 * A map of quests, indexed by name.
	 */
	private static Map<String, WauzQuest> questMap = new HashMap<>();
	
	/**
	 * Initializes all quest configs and fills the internal quest maps.
	 * 
	 * @see QuestConfigurator#getQuestNameList()
	 */
	public static void init() {
		for(int level = 1; level <= WauzCore.MAX_PLAYER_LEVEL; level++) {
			levelQuestsMap.put(level, new ArrayList<>());
		}
		
		for(String questName : QuestConfigurator.getQuestNameList()) {
			WauzQuest quest = new WauzQuest(questName);
			levelQuestsMap.get(quest.getLevel()).add(quest);
			questMap.put(questName, quest);
		}
	}
	
	/**
	 * @param level A quest level.
	 * 
	 * @return A list of quests with that level.
	 */
	public static List<WauzQuest> getQuestsForLevel(int level) {
		return levelQuestsMap.get(level);
	}
	
	/**
	 * @param questName A quest name.
	 * 
	 * @return Thr quest with that name.
	 */
	public static WauzQuest getQuest(String questName) {
		return questMap.get(questName);
	}
	
	/**
	 * @return The total amount of quests.
	 */
	public static int getQuestCount() {
		return questMap.size();
	}
	
	/**
	 * The canonical name of the quest.
	 */
	private String questName;
	
	/**
	 * The name of the quest, shown in the questlog.
	 */
	private String displayName;
	
	/**
	 * The coordinates of the quest location as text.
	 */
	private String coordinates;
	
	/**
	 * The type of the quest as text.
	 */
	private String type;
	
	/**
	 * The level of the quest.
	 */
	private int level;
	
	/**
	 * The amount of questphases.
	 */
	private int phaseAmount;
	
	/**
	 * The list of messages in the completion dialog.
	 */
	private List<String> completedDialog;
	
	/**
	 * The list of questphases.
	 */
	private List<Phase> phases = new ArrayList<>();
	
	/**
	 * Constructs a quest, based on the quest file name in the /WauzCore/QuestData folder.
	 * 
	 * @param questName The canonical name of the quest.
	 */
	public WauzQuest(String questName) {
		this.questName = questName;
		
		displayName = QuestConfigurator.getDisplayName(questName);
		coordinates = QuestConfigurator.getCoordinates(questName);
		type = QuestConfigurator.getType(questName);
		level = QuestConfigurator.getLevel(questName);
		phaseAmount = QuestConfigurator.getPhaseAmount(questName);
		completedDialog = QuestConfigurator.getCompletedDialog(questName);
		
		for(int phase = 1; phase <= phaseAmount; phase++) {
			phases.add(new Phase(questName, phase));
		}
	}

	/**
	 * @return The canonical name of the quest.
	 */
	public String getQuestName() {
		return questName;
	}

	/**
	 * @return The name of the quest, shown in the questlog.
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @return The coordinates of the quest location as text.
	 */
	public String getCoordinates() {
		return coordinates;
	}

	/**
	 * @return The type of the quest as text.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @return The level of the quest.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return The amount of questphases.
	 */
	public int getPhaseAmount() {
		return phaseAmount;
	}

	/**
	 * @return The list of messages in the completion dialog.
	 */
	public List<String> getCompletedDialog() {
		return completedDialog;
	}
	
	/**
	 * @return The coordinates of the quest location parsed to a 2D point.
	 */
	public Point2D getQuestPoint() {
		String[] splitCoordinated = coordinates.split(" ");
		double x = Double.parseDouble(splitCoordinated[0]);
		double z = Double.parseDouble(splitCoordinated[2]);
		return new Point2D.Double(x, z);
	}
	
	/**
	 * @param phase The number of the phase.
	 * 
	 * @return The requested phase.
	 */
	private Phase getPhase(int phase) {
		return phases.get(phase - 1);
	}
	
	/**
	 * @param phase The number of the phase.
	 * 
	 * @return The list of messages in the phase dialog.
	 */
	public List<String> getPhaseDialog(int phase) {
		return getPhase(phase).getPhaseDialog();
	}
	
	/**
	 * @param phase The number of the phase.
	 * 
	 * @return The list of messages in the uncompleted phase dialog.
	 */
	public String getUncompletedMessage(int phase) {
		return getPhase(phase).getUncompleteMessage();
	}
	
	/**
	 * @param phase The number of the phase.
	 * 
	 * @return The amount of requirements to complete the phase.
	 */
	public int getRequirementAmount(int phase) {
		return getPhase(phase).getRequirementAmount();
	}
	
	/**
	 * @param phase The number of the phase.
	 * @param requirement The number of the requirement.
	 * 
	 * @return The amount of the needed items, to fulfill the requirement.
	 */
	public int getRequirementNeededItemAmount(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemAmount();
	}
	
	/**
	 * @param phase The number of the phase.
	 * @param requirement The number of the requirement.
	 * 
	 * @return The name of the needed items, to fulfill the requirement.
	 */
	public String getRequirementNeededItemName(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemName();
	}
	
	/**
	 * @param phase The number of the phase.
	 * @param requirement The number of the requirement.
	 * 
	 * @return The coordinates of the needed items, to fulfill the requirement.
	 */
	public String getRequirementNeededItemCoordinates(int phase, int requirement) {
		return getPhase(phase).getRequirement(requirement).getNeededItemCoordinates();
	}
	
	/**
	 * A phase of a quest.
	 * 
	 * @author Wauzmons
	 */
	private static class Phase {
		
		/**
		 * The list of messages in the phase dialog.
		 */
		private List<String> phaseDialog;
		
		/**
		 * The list of messages in the uncompleted phase dialog.
		 */
		private String uncompleteMessage;
		
		/**
		 * The amount of requirements to complete the phase.
		 */
		private int requirementAmount;
		
		/**
		 * The list of questphase completion requirements.
		 */
		private List<Requirement> requirements = new ArrayList<>();
		
		/**
		 * Constructs a quest phase, based on the quest file name in the /WauzCore/QuestData folder.
		 * 
		 * @param questName The canonical name of the quest.
		 * @param phase The number of the phase.
		 */
		public Phase(String questName, int phase) {
			phaseDialog = QuestConfigurator.getPhaseDialog(questName, phase);
			uncompleteMessage = QuestConfigurator.getUncompletedMessage(questName, phase);
			requirementAmount = QuestConfigurator.getRequirementAmount(questName, phase);
			
			for(int requirement = 1; requirement <= requirementAmount; requirement++) {
				requirements.add(new Requirement(questName, phase, requirement));
			}
		}

		/**
		 * @return The list of messages in the phase dialog.
		 */
		public List<String> getPhaseDialog() {
			return phaseDialog;
		}

		/**
		 * @return The list of messages in the uncompleted phase dialog.
		 */
		public String getUncompleteMessage() {
			return uncompleteMessage;
		}

		/**
		 * @return The amount of requirements to complete the phase.
		 */
		public int getRequirementAmount() {
			return requirementAmount;
		}
		
		/**
		 * @param requirement The number of the requirement.
		 * 
		 * @return The requested requirement.
		 */
		public Requirement getRequirement(int requirement) {
			return requirements.get(requirement - 1);
		}
		
	}
	
	/**
	 * A completion requirement of a phase of a quest.
	 * 
	 * @author Wauzmons
	 */
	private static class Requirement {
		
		/**
		 * The amount of the needed items, to fulfill the requirement.
		 */
		int neededItemAmount;
		
		/**
		 * The name of the needed items, to fulfill the requirement.
		 */
		String neededItemName;
		
		/**
		 * The coordinates of the needed items, to fulfill the requirement.
		 */
		String neededItemCoordinates;
		
		/**
		 * Constructs a quest phase completion requirement, based on the quest file name in the /WauzCore/QuestData folder.
		 * 
		 * @param questName The canonical name of the quest.
		 * @param phase The number of the phase.
		 * @param requirement The number of the requirement.
		 */
		public Requirement(String questName, int phase, int requirement) {
			neededItemAmount = QuestConfigurator.getRequirementNeededItemAmount(questName, phase, requirement);
			neededItemName = QuestConfigurator.getRequirementNeededItemName(questName, phase, requirement);
			neededItemCoordinates = QuestConfigurator.getRequirementNeededItemCoordinates(questName, phase, requirement);
		}

		/**
		 * @return The amount of the needed items, to fulfill the requirement.
		 */
		public int getNeededItemAmount() {
			return neededItemAmount;
		}

		/**
		 * @return The name of the needed items, to fulfill the requirement.
		 */
		public String getNeededItemName() {
			return neededItemName;
		}

		/**
		 * @return The coordinates of the needed items, to fulfill the requirement.
		 */
		public String getNeededItemCoordinates() {
			return neededItemCoordinates;
		}
		
	}

}
