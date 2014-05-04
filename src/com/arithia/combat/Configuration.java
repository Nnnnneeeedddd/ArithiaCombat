package com.arithia.combat;

import java.util.Arrays;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {
	private Combat plugin;
	
	public static void config(Combat combat){
		Configuration c = new Configuration();
		c.plugin = combat;
		c.addDefaultsAndHeader();
		c.plugin.saveConfig();
	}
	
	/*
	 * adds everything in the config
	 */
	public void addDefaultsAndHeader(){
		FileConfiguration config = plugin.getConfig();
		
		config.options().header("This is the default config file for ArithiaCombat\n"
				+ "Players-----------\n"
				+ "Player1 is always considered as the starter of fights\n"
				+ "Player2 is the one that doesn't\n"
				+ "MaxFights---------\n"
				+ "The option to change the maximum number of fights\n"
				+ "is to save memory set it to -1 for unlimited\n"
				+ "FleeDistance-------\n"
				+ "Flee distance MUST be 4 or above\n"
				+ "Weapons-----\n"
				+ "These are all the weapons and how much damage they do (unless there is a miss)\n"
				+ "1 unit = 1/2 player heart. players health is 100\n"
				+ "Messages------\n"
				+ "lists of messages that are randomly chosen when a certain amount of damage is done\n"
				+ "make sure all the damages that you assign on the weapons has a message for example if the\n"
				+ "damage for a diamond sword was 20 the you would need to have:\n"
				+ "\n"
				+ "messages:\n"
				+ "  damages:\n"
				+ "    20:\n"
				+ "    - 'ouch that was a big hit with a big weapon (dealt 20 damage)'\n"
				+ "\n"
				+ "in all messages {{player}} is the player who hit and {{opponent}} is the opponent\n"
				+ "Chances------\n"
				+ "percentages so if miss is 50 then hit is 50\n"
				+ "do it is 50, 50 if miss is 60 then hit would be 40\n"
				+ "ArmourDefense------\n"
				+ "if the player is wearing full armour of one type E.g. Diamond\n"
				+ "it will multiply any damage done by the value so the lower it is the more protective the armour\n"
				+ "if you set it to one the armour will make no difference, if you set it to anything over one then\n"
				+ "the player would be better off not wearing any armour\n"
				+ "EnslavementTime--------\n"
				+ "in seconds\n"
				+ "BOWS-------\n"
				+ "In the bow there is a \"hit: \" value\n"
				+ "this is how much will be added to fist damage if you are holding a bow\n"
				+ "IMPORTANT: the bow must have damage values that is 'weapons.bow.hit' higher then the highest fist value");
		
		config.options().copyHeader(true);
		
		config.addDefault("startFightMessage", "&6[ArithiaCombat] Player {{player1}}&6 has started a fight");
		config.addDefault("maxFights", -1);
		config.addDefault("maxFightMessage", "&b[ArithiaCombat] Too many fights");
		config.addDefault("notYourTurnMessage", "&c[ArithiaCombat] You can not take action; It is not your turn!");
		config.addDefault("fleeDistance", 5);
		
		config.addDefault("winMessages.winMessage", "&6[ArithiaCombat] You WIN!!!!!");
		config.addDefault("winMessages.kill", "&6[ArithiaCombat] type 'kill' to kill and delete players character");
		config.addDefault("winMessages.enslave", "&6[ArithiaCombat] type 'enslave' to enslave the loser");
		config.addDefault("winMessages.mercy", "&6[ArithiaCombat] type 'mercy' to be merciful and knock the player out (normal death)");
		
		config.addDefault("fleeMessages.success", "&6Successfully fled");
		config.addDefault("fleeMessages.fail", "&cFlee failed");
		
		config.addDefault("enslavementTime", 2000);
		
		
		String[] materials = {"diamond", "gold", "iron", "stone", "wood"};
		String[] weapons = {"hoe", "shovel", "axe", "sword"};
		
		for(String material : materials){
			for(String weapon: weapons){
				String path = "weapons."+material+"."+weapon+".";
				config.addDefault(path+"damageFrom", 1);
				config.addDefault(path+"damageTo", 3);
				config.addDefault(path+"messages.miss", Arrays.asList("Messages for when player misses with this weapon"));
				config.addDefault(path+"messages.1", Arrays.asList("messages for damage 1 for weapon: "+weapon));
				config.addDefault(path+"messages.2", Arrays.asList("messages for damage 2 for weapon: "+weapon));
				config.addDefault(path+"messages.3", Arrays.asList("messages for damage 3 for weapon: "+weapon));
			}
		}
		
		String[] weaponsWithoutMaterials = {"fists", "bow", "shears"};
		
		for(String weapon : weaponsWithoutMaterials){
			config.addDefault("weapons."+weapon+".damageFrom", 1);
			config.addDefault("weapons."+weapon+".damageTo", 3);
			config.addDefault("weapons."+weapon+".messages.miss", Arrays.asList("Messages for when player misses with this weapon"));
			config.addDefault("weapons."+weapon+".messages.1", Arrays.asList("messages for damage 1 for weapon: "+weapon));
			config.addDefault("weapons."+weapon+".messages.2", Arrays.asList("messages for damage 2 for weapon: "+weapon));
			config.addDefault("weapons."+weapon+".messages.3", Arrays.asList("messages for damage 3 for weapon: "+weapon));
			if(weapon.equals("bow")){
				plugin.getConfig().addDefault("weapons.bow.messages.4", "THIS MESSAGE IS ESSENTIAL because it is one"
						+ " higher then 3 (the highest fist value)");
				plugin.getConfig().addDefault("weapons.bow.hit", 1);
			}
		}
		
		config.addDefault("armour.fullDiamondArmor", 0.3);
		config.addDefault("armour.fullGoldArmour", 0.5);
		config.addDefault("armour.fullIronArmour", 0.6);
		config.addDefault("armour.fullLeatherArmour", 0.8);
		
		config.addDefault("chances.miss", 30);
		config.addDefault("chances.flee", 60);
		
		config.options().copyDefaults(true);
	}
	
	
}
