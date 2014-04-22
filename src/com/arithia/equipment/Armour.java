package com.arithia.equipment;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.arithia.combat.Combat;

public class Armour {
	private static Material[] diamondArmour = {
		Material.DIAMOND_HELMET,
		Material.DIAMOND_CHESTPLATE,
		Material.DIAMOND_LEGGINGS,
		Material.DIAMOND_BOOTS
	};
	
	private static Material[] goldArmour = {
		Material.GOLD_HELMET,
		Material.GOLD_CHESTPLATE,
		Material.GOLD_LEGGINGS,
		Material.GOLD_BOOTS
	};
	
	private static Material[] ironArmour = {
		Material.IRON_HELMET,
		Material.IRON_CHESTPLATE,
		Material.IRON_LEGGINGS,
		Material.IRON_BOOTS
	};
	
	private static Material[] leatherArmour = {
		Material.LEATHER_HELMET,
		Material.LEATHER_CHESTPLATE,
		Material.LEATHER_LEGGINGS,
		Material.LEATHER_BOOTS
	};
	
	
	public static double getDefence(Player p, Combat plugin){
		Material[] armour = getArmour(p);
		
		if(armour == null){
			return 1d;
		}
		
		char[] helmet = armour[0].toString().toCharArray();
		String material = "";
		for(char c: helmet){
			if(c != '_'){
				material = material+c;
			}else{
				break;
			}
		}
		material = material.toLowerCase();
		
		if(material.equals("diamond")){
			return plugin.getConfig().getDouble("armour.fullDiamondArmor");
		}else if(material.equals("gold")){
			return plugin.getConfig().getDouble("armour.fullGoldArmor");
		}else if(material.equals("iron")){
			return plugin.getConfig().getDouble("armour.fullIronArmor");
		}else if(material.equals("leather")){
			return plugin.getConfig().getDouble("armour.fullLeatherArmor");
		}
		
		return 1;
	}
	
	/*
	 * tests if player is wearing armour
	 */
	public static Material[] getArmour(Player p){
		/*
		 * if player is not wearing full armour
		 */
		if(p.getEquipment().getLeggings() == null ||
				p.getEquipment().getBoots() == null ||
				p.getEquipment().getChestplate() == null ||
				p.getEquipment().getHelmet() == null){
			return null;
		}
		
		int x = 0;
		for(Material m: diamondArmour){
			switch(x){
			case 0:
				if(p.getEquipment().getHelmet().getType() == m){
					x++;
				}
				break;
				
			case 1:
				if(p.getEquipment().getChestplate().getType() == m){
					x++;
				}
				break;
				
			case 2:
				if(p.getEquipment().getLeggings().getType() == m){
					x++;
				}
				break;
				
			case 3:
				if(p.getEquipment().getBoots().getType() == m){
					x++;
				}
				break;
			}
		}
		
		if(x == 4){
			return diamondArmour;
		}
		
		int xa = 0;
		for(Material m: goldArmour){
			switch(xa){
			case 0:
				if(p.getEquipment().getHelmet().getType() == m){
					xa++;
				}
				break;
				
			case 1:
				if(p.getEquipment().getChestplate().getType() == m){
					xa++;
				}
				break;
				
			case 2:
				if(p.getEquipment().getLeggings().getType() == m){
					xa++;
				}
				break;
				
			case 3:
				if(p.getEquipment().getBoots().getType() == m){
					xa++;
				}
				break;
			}
		}
		
		if(xa == 4){
			return goldArmour;
		}
		
		int xaa = 0;
		for(Material m: ironArmour){
			switch(xaa){
			case 0:
				if(p.getEquipment().getHelmet().getType() == m){
					xaa++;
				}
				break;
				
			case 1:
				if(p.getEquipment().getChestplate().getType() == m){
					xaa++;
				}
				break;
				
			case 2:
				if(p.getEquipment().getLeggings().getType() == m){
					xaa++;
				}
				break;
				
			case 3:
				if(p.getEquipment().getBoots().getType() == m){
					xaa++;
				}
				break;
			}
		}
		
		if(xaa == 4){
			return ironArmour;
		}
		
		int xaaa = 0;
		for(Material m: leatherArmour){
			switch(xaaa){
			case 0:
				if(p.getEquipment().getHelmet().getType() == m){
					xaaa++;
				}
				break;
				
			case 1:
				if(p.getEquipment().getChestplate().getType() == m){
					xaaa++;
				}
				break;
				
			case 2:
				if(p.getEquipment().getLeggings().getType() == m){
					xaaa++;
				}
				break;
				
			case 3:
				if(p.getEquipment().getBoots().getType() == m){
					xaaa++;
				}
				break;
			}
		}
		
		if(xaaa == 4){
			return leatherArmour;
		}
		
		return null;
		
	}
}
