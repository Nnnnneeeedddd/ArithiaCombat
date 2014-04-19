package com.arithia.combat;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.arithia.equipment.Weapons;

public class Fight {
	private boolean fightStarted = false; // to allow fight verification
	private int runID = 0; //id of runnable for verification
	private Combat plugin;
	private Player player1;//player 1 is the damager
	private Player player2;//player 2 is hit
	private int turn = 1;/*1 = player1's turn
						   2 = player2's turn*/
	
	//constructor: initialises the players
	public Fight(Player p1, Player p2, Combat plugin){
		player1 = p1;
		player2 = p2;
		this.plugin = plugin;
	}
	
	
	/*
	 * called when fight starts
	 */
	public void startFight(){
		if(!testPerms()){
			close();
			return;
		}
		
		//prompt
		player1.sendMessage(ChatColor.RED+"[ArithiaCombat] are you sure you want to start a fight");
		player1.sendMessage(ChatColor.RED+"[ArithiaCombat] Please answer 'yes' or 'no' within 20 seconds");
		
		runID = plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run(){
				close();
			}
		}, 500L);
	}
	
	/*
	 * called when verified
	 */
	private void startFightAfterVerification(){
		plugin.getServer().getScheduler().cancelTask(runID);
		
		if(!Weapons.isWeapon(player1.getItemInHand())){
			player1.sendMessage(ChatColor.RED+"[ArithiaCombat] You must use a certified weapon\n"
					+ "Fists, hoe, shovel, axe, bow, sword");
			close();
			return;
		}
		
		if(!Weapons.isWeapon(player2.getItemInHand())){
			player2.sendMessage(ChatColor.RED+"[ArithiaCombat] You must use a certified weapon\n"
					+ "Fists, hoe, shovel, axe, bow, sword");
			player1.sendMessage(ChatColor.RED+"Opponent must have certified weapon");
			close();
			return;
		}
		
		String message = plugin.getConfig().getString("startFightMessage");
		message = message.replace("&", "§");
		message = message.replace("{{player1}}", player1.getDisplayName());
		broadcast(message);
		
		setPlayersMaxHealth();
		
		playerAttemptHit(player1);
	}
	
	
	
	/*
	 * called when a player of the correct turn
	 * attempts to hit his opponent
	 */
	public void playerAttemptHit(Player attempter){
		if(!fightStarted){
			return;
		}
		
		boolean hit = false;
		int chancesOfMiss = plugin.getConfig().getInt("chances.miss");
		int randomNumber = Combat.rand.nextInt(100)+1;
		
		if(randomNumber < chancesOfMiss){
			hit = true;
		}
		
		/*
		 * if player hits
		 */
		if(hit){
			double damage = Weapons.getDamage(attempter.getItemInHand(), plugin);
			Player opponent = getOpponent(attempter);
			
			Set<String> damages = plugin.getConfig().getConfigurationSection("messages.damages").getKeys(true);
			
			if(damages.contains(String.valueOf(damage))){
				List<String> messages = plugin.getConfig().getStringList("messages.damages."+damage);
				int random = Combat.rand.nextInt(messages.size());
				String message = messages.get(random);
				
				message = message.replace("&", "§");
				message = message.replace("{{player}}", attempter.getDisplayName());
				message = message.replace("{{opponent}}", opponent.getDisplayName());
				
				broadcast(message);
				opponent.damage(damage);
			}else{
				broadcast(ChatColor.RED+"ErrorInConfig: damage dealt: "+damage);
			}
			
		/*
		 * player misses
		 */
		}else{
			Player opponent = getOpponent(attempter);
			List<String> messages = plugin.getConfig().getStringList("messages.damages.miss");
			int random = Combat.rand.nextInt(messages.size());
			String message = messages.get(random);
			
			message = message.replace("&", "§");
			message = message.replace("{{player}}", attempter.getDisplayName());
			message = message.replace("{{opponent}}", opponent.getDisplayName());
			
			broadcast(message);
		}
		
		changeTurn();
	}
	
	
	
	/*
	 * called when a player of the correct turn
	 * attempts to flee
	 */
	public void playerAttemptFlee(Player attempter){
		if(!fightStarted){
			return;
		}
		
		int random = Combat.rand.nextInt(100)+1;
		int chancesOfFlee = plugin.getConfig().getInt("chances.flee");
		
		if(random < chancesOfFlee){
			//flee success
			attempter.sendMessage(ChatColor.GREEN+"[ArithiaCombat] Successfully Fled!!");
			closeWithoutMessage();
		}else{
			//flee fail
			attempter.sendMessage(ChatColor.RED+"[ArithiaCombat] Flee failed");
			attempter.teleport(getOpponent(attempter));
		}
		
		changeTurn();
	}
	
	
	
	
	/*
	 * called when a player in fight dies
	 * player (args0) is the player who died
	 */
	public void playerDeath(Player player){
		closeWithoutMessage();
		Player winner = getOpponent(player);
		winner.sendMessage(ChatColor.GOLD+"You WIN!!!!!");
	}
	
	
	
	
	/*
	 * calls when player types 'yes' or 'no' into chat
	 * player (args0) is the player that typed
	 */
	public void possibleVerficationMessage(Player player, boolean yesno){
		if(player == player1){
			if(yesno){
				fightStarted = true;
				startFightAfterVerification();
			}else{
				plugin.getServer().getScheduler().cancelTask(runID);
				close();
			}
		}
	}
	
	
	
	/*
	 * tests whether both players have appropriate permissions
	 */
	private boolean testPerms(){
		if(!player1.hasPermission("arithiacombat.startfight")){
			
			player1.sendMessage(ChatColor.RED+
				"[ArithiaCombat] You do not have permission to start a fight");
			
			return false;
		}
		
		if(player2.hasPermission("arithiacombat.unfightable")){
			
			player1.sendMessage(ChatColor.RED+
				"This player is not Fightable");
			
			return false;
		}
		
		return true;
	}
	
	
	
	
	/*
	 * returns an array of players that
	 * consists of player1 and player2
	 */
	public Player[] getPlayers(){
		Player[] players = {player1, player2};
		
		return players;
	}
	
	
	
	
	/*
	 * broadcasts a message
	 * to just the two players
	 */
	public void broadcast(String message){
		player1.sendMessage(message);
		player2.sendMessage(message);
	}
	
	
	
	/*
	 * returns player who's turn it is
	 */
	public Player getTurn(){
		if(turn == 1){
			return player1;
		}else if(turn == 2){
			return player2;
		}
		
		return null;
	}
	
	
	
	/*
	 * gets the opposing player to args0 (Player player)
	 */
	public Player getOpponent(Player player){
		if(player1 == player){
			return player2;
		}else{
			return player1;
		}
	}
	
	
	
	/*
	 * sets players max health to 100
	 * and takes a percentage of his old health
	 * sets his health to the percentage
	 */
	private void setPlayersMaxHealth(){
		Damageable dm1 = player1;
		double percentagehealth1 = (dm1.getHealth() / dm1.getMaxHealth()) * 100.0;
		player1.setMaxHealth(100.0);
		player1.setHealth(percentagehealth1);
		
		Damageable dm2 = player2;
		double percentagehealth2 = (dm2.getHealth() / dm2.getMaxHealth()) * 100.0;
		player2.setMaxHealth(100.0);
		player2.setHealth(percentagehealth2);
	}
	
	
	
	/*
	 * changes turn
	 */
	private void changeTurn(){
		if(turn == 1){
			turn = 2;
		}else{
			turn = 1;
		}
	}
	
	
	public void closeWithoutMessage(){
		player1.setMaxHealth(20.0);
		player1.setHealth(20.0);
		
		player2.setMaxHealth(20.0);
		player2.setHealth(20.0);
		plugin.removeFight(this);
	}
	
	
	/*
	 * close Fight
	 */
	public void close(){
		broadcast(ChatColor.DARK_RED+"Fight Closing");
		player1.setMaxHealth(20.0);
		player1.setHealth(20.0);
		
		player2.setMaxHealth(20.0);
		player2.setHealth(20.0);
		plugin.removeFight(this);
	}
}
