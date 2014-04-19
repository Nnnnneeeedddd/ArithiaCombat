package com.arithia.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.arithia.combat.Combat;

public class FightsCommand implements CommandExecutor{
	private Combat plugin;
	
	/*
	 * constructor used to initialise plugin variable
	 */
	public FightsCommand(Combat plugin){
		this.plugin = plugin;
	}
	
	/*
	 * called when command /fights is called
	 */
	public boolean onCommand(CommandSender s, Command c, String l, String[] args){
		
		if(args.length < 1){
			return false;
		}else{
			
			/*
			 * sub command exit: exits fight
			 */
			if(args[0].equalsIgnoreCase("exit")){
				if(!(s instanceof Player)){
					s.sendMessage(ChatColor.RED+
							"[ArithiaCombat] Must be a player in a fight to perform this command");
					return true;
				}
				
				if(plugin.getFight((Player)s) != null){
					plugin.getFight((Player) s).close();
					s.sendMessage(ChatColor.RED+
							"[ArithiaCombat] Player: "+((Player)s).getDisplayName()+" is exiting the fight");
				}else{
					s.sendMessage(ChatColor.RED+
							"[ArithiaCombat] Must be a player in a fight to perform this command");
				}
			}
			
			/*
			 * command to turn pvp on or off
			 */
			else if(args[0].equalsIgnoreCase("pvp")){
				if(s.hasPermission("arithiacombat.pvpcommand")){
					
					if(args.length != 2){
						s.sendMessage(ChatColor.RED+"[ArithiaCombat] Usage: /fights pvp <player>");
						return true;
					}
					
					String playerSTR = args[1];
					Player player = Bukkit.getPlayer(playerSTR);
					if(plugin.getFight(player) != null){
						s.sendMessage(ChatColor.RED+"[ArithiaCombat] this player is in a fight");
						return true;
					}
					
					if(plugin.getPVPPlayers().contains(player)){
						plugin.removePVPPlayer(player);
					}else{
						plugin.addPVPPlayer(player);
					}
					
				}else{
					s.sendMessage(ChatColor.RED+"You do not have permission to perform this command");
				}
			}
			
			/*
			 * sub command help: lists all sub commands
			 */
			else if(args[0].equalsIgnoreCase("help")){
				s.sendMessage(ChatColor.GOLD+"/fights help: displays this message");
				s.sendMessage(ChatColor.GOLD+"/fights exit: if player is in a fight, fight will be closed");
			}
		}
		
		return true;
	}
	
}
