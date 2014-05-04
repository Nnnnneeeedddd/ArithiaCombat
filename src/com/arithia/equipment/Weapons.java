package com.arithia.equipment;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.arithia.combat.Combat;

public class Weapons {
	
	/*
	 * array of all weapons
	 */
	private static Material[] weapons = {
		Material.WOOD_HOE,
		Material.WOOD_SPADE,
		Material.WOOD_AXE,
		Material.WOOD_SWORD,
		
		Material.STONE_HOE,
		Material.STONE_SPADE,
		Material.STONE_AXE,
		Material.STONE_SWORD,
		
		Material.IRON_HOE,
		Material.IRON_SPADE,
		Material.IRON_AXE,
		Material.IRON_SWORD,
		
		Material.GOLD_HOE,
		Material.GOLD_SPADE,
		Material.GOLD_AXE,
		Material.GOLD_SWORD,
		
		Material.DIAMOND_HOE,
		Material.DIAMOND_SPADE,
		Material.DIAMOND_AXE,
		Material.DIAMOND_SWORD,
		
		Material.BOW,
		Material.SHEARS
	};
	
	/*
	 * if a weapon then it will get the amount of damage the weapon does
	 */
	public static double getDamage(ItemStack is, Combat plugin){
		if(isWeapon(is)){
			if(is == null || is.getType() == Material.AIR){
				int from = plugin.getConfig().getInt("weapons.fists.damageFrom");
				int to = plugin.getConfig().getInt("weapons.fists.damageTo");
				return randomNumber(from, to);
			}else{
				String weapon = is.getType().toString();
				
				if(weapon.contains("_")){
					char[] weaponCA = weapon.toCharArray();
					String material = "";
					
					for(char c: weaponCA){
						if(c != '_'){
							material = material+c;
						}else{
							break;
						}
					}
					
					String[] weapons = {"_HOE", "_SPADE", "_AXE", "_SWORD"};
					
					for(String weaponT : weapons){
						if(weapon.equals(material+weaponT)){
							String weaponTLC = weaponT.toLowerCase().replace("_", "");
							String path = "weapons."+material.toLowerCase()+"."+weaponTLC;
							int from = plugin.getConfig().getInt(path+".damageFrom");
							int to = plugin.getConfig().getInt(path+".damageTo");
							return randomNumber(from, to);
						}
					}
					
					return -1;
				}else{
					if(weapon.equals("BOW")){
						int from = plugin.getConfig().getInt("weapons.bow.damageFrom");
						int to = plugin.getConfig().getInt("weapons.bow.damageTo");
						return randomNumber(from, to);
					}else{
						int from = plugin.getConfig().getInt("weapons.shears.damageFrom");
						int to = plugin.getConfig().getInt("weapons.shears.damageTo");
						return randomNumber(from, to);
					}
				}
			}
		}else{
			return -1.0;
		}
	}
	
	/*
	 * returns string array of [0] being weapon
	 * and [1] being the material of the weapon
	 */
	public static String[] getWeaponAndMat(Material weaponM){
		String weapon="";
		String full = weaponM.toString();
		String material="";
		

		if(weaponM == Material.AIR || weaponM == null){
			//player using fists
			weapon = "fists";
			material = "";
		}else{
			if(full.contains("_")){
				char[] weaponCA = full.toCharArray();
				for(char c: weaponCA){
					if(c != '_'){
						material = material+c;
					}else{
						break;
					}
				}
				
				weapon = full.replace(material+"_", "").toLowerCase();
				material = material.toLowerCase();
			}else{
				if(full.equals("BOW")){
					weapon = "bow";
					material = "";
				}else{
					weapon = "shears";
					material = "";
				}
			}
		}
		
		String[] wam = {weapon, material};
		return wam;
	}
	
//	/*
//	 * returns the list of messages for a certain weapon
//	 */
//	public static List<String> getWeaponMessages(Material weapon, Combat plugin){
//		String weaponS = weapon.toString();
//		
//		if(weaponS.contains("_")){
//			
//		}else{
//			if(weaponS.equals("BOW")){
//				return plugin.getConfig().getStringList("weapons.bow");
//			}else{
//				
//			}
//		}
//	}
	
	/*
	 * works out a random number
	 * with a from and to value
	 */
	private static int randomNumber(int from, int to){
		return Combat.rand.nextInt(to)+from;
	}
	
	/*
	 * works out is ItemStack args0 is a weapon
	 */
	public static boolean isWeapon(ItemStack is){
		/*
		 * player using fists
		 */
		if(is == null || is.getType() == Material.AIR){
			return true;
		}
		
		/*
		 * player using weapon
		 */
		for(Material mat: weapons){
			if(is.getType() == mat){
				return true;
			}
		}
		
		return false;
	}
	
}
