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
				 return plugin.getConfig().getDouble("weapons.fists");
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
					
					if(weapon.equals(material+"_HOE")){
						return plugin.getConfig().getDouble("weapons."+material.toLowerCase()+".hoe");
					}else if(weapon.equals(material+"_SPADE")){
						return plugin.getConfig().getDouble("weapons."+material.toLowerCase()+".shovel");
					}else if(weapon.equals(material+"_AXE")){
						return plugin.getConfig().getDouble("weapons."+material.toLowerCase()+".axe");
					}else if(weapon.equals(material+"_SWORD")){
						return plugin.getConfig().getDouble("weapons."+material.toLowerCase()+".sword");
					}else{
						return -1;
					}
				}else{
					if(weapon.equals("BOW")){
						return plugin.getConfig().getDouble("weapons.bow");
					}else{
						return plugin.getConfig().getDouble("weapons.shears");
					}
				}
			}
		}else{
			return -1.0;
		}
	}
	
	/*
	 * works out is ItemStack args0 is a weapon
	 */
	public static boolean isWeapon(ItemStack is){
		
		/*
		 * player using weapon
		 */
		for(Material mat: weapons){
			if(is.getType() == mat){
				return true;
			}
		}
		
		/*
		 * player using fists
		 */
		if(is == null || is.getType() == Material.AIR){
			return true;
		}
		
		return false;
	}
	
}
