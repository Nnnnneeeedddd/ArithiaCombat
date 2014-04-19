package com.arithia.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.arithia.combat.Combat;
import com.arithia.combat.Fight;

public class PlayerListener implements Listener{
	private Combat plugin;
	
	public PlayerListener(Combat combat){
		plugin = combat;
	}
	
	@EventHandler
	public void onEntityRegainHealth(EntityRegainHealthEvent e){
		/*
		 * if entity is a player
		 */
		if(e.getEntity() instanceof Player){
			//check if entity is in a fight
			if(plugin.getFight((Player) e.getEntity()) != null){
				//cancel
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e){
		
		//if player types 'yes' or 'no'
		if(e.getMessage().equalsIgnoreCase("yes") ||
				e.getMessage().equalsIgnoreCase("no")){
			//create fight var
			Fight fight = plugin.getFight(e.getPlayer());
			//see if player is in fight
			if(fight != null){
				if(e.getMessage().equalsIgnoreCase("yes")){
					fight.possibleVerficationMessage(e.getPlayer(), true);
					e.setCancelled(true);
				}else{
					fight.possibleVerficationMessage(e.getPlayer(), false);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e){
		Player player = e.getEntity();
		
		/*
		 * if player is in fight
		 */
		if(plugin.getFight(player) != null){
			//call fight death
			Fight fight = plugin.getFight(player);
			fight.playerDeath(player);
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e){
		Fight fight = plugin.getFight(e.getPlayer());
		
		//if player is in a fight
		if(fight != null){
			Player player = fight.getTurn();
			
			// if it is not players turn
			if(e.getPlayer() != player){
				int distance = (int) Math.round(
						player.getLocation().distance(
						fight.getOpponent(player).getLocation()));
				
				if(distance > 3){
					fight.getOpponent(player).teleport(player);
					String message = plugin.getConfig().getString("notYourTurnMessage");
					e.getPlayer().sendMessage(message.replace("&", "§"));
				}
			}
			
			//if it is players turn
			else{
				Player player2 = fight.getOpponent(player);
				
				Location player1L = player.getLocation();
				Location player2L = player2.getLocation();
				int distance = (int) Math.round(player1L.distance(player2L));
				
				//if distance is greater then flee distance
				if(distance > plugin.getConfig().getInt("fleeDistance")){
					//he is attempting to flee
					
					fight.playerAttemptFlee(player);
				}
				
			}
		}
	}
	
	@EventHandler
	public void onPlayerDamageEntity(EntityDamageByEntityEvent e){
		//both entities must be players
		if(e.getDamager() instanceof Player &&
				e.getEntity() instanceof Player){
			
			Player player1 = (Player) e.getDamager();
			Player player2 = (Player) e.getEntity();
			
			/*
			 * if both players are not in a fight
			 * then add a new one
			 */
			if(plugin.getFight(player1) == null &&
					plugin.getFight(player2) == null){
				
				int fightNumber = plugin.getFightNumber();
				int maxFights = plugin.getConfig().getInt("maxFights");
				
				if(maxFights < 0 || fightNumber < maxFights){
					Fight fight = new Fight(player1, player2, plugin);
					plugin.addFight(fight);
					fight.startFight();
					
					e.setCancelled(true);
				}else{
					String message = plugin.getConfig().getString("maxFightMessage");
					player1.sendMessage(message.replace("&", "§"));
				}
				
			}
			
			/*
			 * if one or both of the players
			 * are in a fight
			 */
			else{
				
				/*
				 * check that both players are in a fight
				 */
				if(plugin.getFight(player1) != null &&
						plugin.getFight(player2) != null){
					
					//make sure they are in the same fight
					if(plugin.getFight(player1) == plugin.getFight(player2)){
						//get the fight
						Fight fight = plugin.getFight(player1);
						
						if(player1 != fight.getTurn()){
							String message = plugin.getConfig().getString("notYourTurnMessage");
							player1.sendMessage(message.replace("&", "§"));
							
							e.setCancelled(true);
						}
						
						/*
						 * if it is players turn
						 */
						else{
							
							fight.playerAttemptHit(player1);
							e.setCancelled(true);
							
						}
						
					}
					
				}
				
			}
			
		}
	}
}
