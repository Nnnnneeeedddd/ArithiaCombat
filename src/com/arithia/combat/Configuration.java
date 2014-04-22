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
				+ "the player would be better off not wearing any armour"
				+ "EnslavementTime--------\n"
				+ "in seconds");
		
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
		
		config.addDefault("deathLoc.x", 10);
		config.addDefault("deathLoc.y", 10);
		config.addDefault("deathLoc.z", 10);
		
		config.addDefault("weapons.diamond.hoe", 3);
		config.addDefault("weapons.diamond.shovel", 3);
		config.addDefault("weapons.diamond.axe", 5);
		config.addDefault("weapons.diamond.sword", 6);
		
		config.addDefault("weapons.gold.hoe", 2);
		config.addDefault("weapons.gold.shovel", 3);
		config.addDefault("weapons.gold.axe", 4);
		config.addDefault("weapons.gold.sword", 4);
		
		config.addDefault("weapons.iron.hoe", 2);
		config.addDefault("weapons.iron.shovel", 2);
		config.addDefault("weapons.iron.axe", 3);
		config.addDefault("weapons.iron.sword", 4);
		
		config.addDefault("weapons.stone.hoe", 1);
		config.addDefault("weapons.stone.shovel", 1);
		config.addDefault("weapons.stone.axe", 2);
		config.addDefault("weapons.stone.sword", 3);
		
		config.addDefault("weapons.wood.hoe", 1);
		config.addDefault("weapons.wood.shovel", 1);
		config.addDefault("weapons.wood.axe", 1);
		config.addDefault("weapons.wood.sword", 2);
		
		config.addDefault("weapons.fists", 1);
		config.addDefault("weapons.bow", 3);
		config.addDefault("weapons.shears", 5);
		
		config.addDefault("armour.fullDiamondArmor", 0.3);
		config.addDefault("armour.fullGoldArmour", 0.5);
		config.addDefault("armour.fullIronArmour", 0.6);
		config.addDefault("armour.fullLeatherArmour", 0.8);
		
		config.addDefault("messages.damages.miss",
				Arrays.asList("&c{{player}}&c trips over and misses the hit (dealt 0 damage)",
				"&c{{player}}&c's weapon is obviously too heavy for him, he missed (dealt 0 damage)",
				"&c{{player}}&c's opponent blocks the attack (dealt 0 damage)"));
		
		config.addDefault("messages.damages.1", Arrays.asList("&b{{player}}&b Hits player really hard... on the foot (dealt 1 damage)",
				"&a{{player}} lashes out and gently hits the player?? (dealt 1 damage)"));
		
		config.addDefault("messages.damages.2", Arrays.asList("&1{{player}} cuts {{opponent}}&1's knee (dealt 2 damage)"));
		
		config.addDefault("messages.damages.3", Arrays.asList("&1{{player}} dislocates opponents hand (dealt 3 damage)"));
		
		config.addDefault("messages.damages.4", Arrays.asList("&1{{player}} cuts {{opponent}}&1 arm clean off (dealt 4 damage)"));
		
		config.addDefault("messages.damages.5", Arrays.asList("&1{{player}} mutilates the players face (dealt 5 damage)"));
		
		config.addDefault("messages.damages.6", Arrays.asList("&1{{player}} smacks player round the head hard (dealt 6 damage)"));
		
		config.addDefault("chances.miss", 30);
		config.addDefault("chances.flee", 60);
		
		config.options().copyDefaults(true);
	}
	
	
}
