package com.arithia.combat;

import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.arithia.equipment.Armour;
import com.arithia.equipment.Weapons;

public class Fight {
	private boolean won = false;
	private Player winner;
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
		
		if(player1.getItemInHand().getType() == Material.BOW){
			playerAttemptHit(player1, true);
		}else{
			playerAttemptHit(player1, false);
		}
	}
	
	
	
	/*
	 * called when a player of the correct turn
	 * attempts to hit his opponent
	 */
	public void playerAttemptHit(Player attempter, boolean hitWithBow){
		if(!fightStarted || won){
			return;
		}
		
		boolean hit = true;
		int chancesOfMiss = plugin.getConfig().getInt("chances.miss");
		int randomNumber = Combat.rand.nextInt(100)+1;
		
		if(randomNumber < chancesOfMiss){
			hit = false;
		}
		
		/*
		 * if player hits
		 */
		if(hit){
			double damageD;
			
			if(hitWithBow){
				damageD = Weapons.getDamage(null, plugin)+plugin.getConfig().getInt("weapons.bow.hit");
			}else{
				damageD = Weapons.getDamage(attempter.getItemInHand(), plugin);
			}
			
			if(damageD == -1.0){
				attempter.sendMessage(
				ChatColor.RED+"[ArithiaCombat] You must be holding a weapon or using your fists");
				return;
			}
			
			damageD *= Armour.getDefence(attempter, plugin);
			int damage = (int) Math.round(damageD);
			Player opponent = getOpponent(attempter);
			
			String[] wm = Weapons.getWeaponAndMat(attempter.getItemInHand().getType());
			
			Set<String> damages = null;
			if(wm[1].equals("")){
				damages = plugin.getConfig().getConfigurationSection("weapons."+wm[0]+".messages").getKeys(true);
			}else{
				attempter.sendMessage("weapons."+wm[1]+"."+wm[0]+".messages");
				damages = plugin.getConfig().getConfigurationSection("weapons."+wm[1]+"."+wm[0]+".messages").getKeys(true);
			}
			
			if(damages.contains(String.valueOf(damage))){
				List<String> messages=null;
				if(wm[1] == ""){
					messages = plugin.getConfig().getStringList("weapons."+wm[0]+".messages."+damage);
				}else{
					messages = plugin.getConfig().getStringList("weapons."+wm[1]+"."+wm[0]+".messages."+damage);
				}
				
				int random = Combat.rand.nextInt(messages.size());
				String message = messages.get(random);
				
				message = message.replace("&", "§");
				message = message.replace("{{player}}", attempter.getDisplayName());
				message = message.replace("{{opponent}}", opponent.getDisplayName());
				
				broadcast(message);
				opponent.damage(Double.valueOf(damage));
			}else{
				broadcast(ChatColor.RED+"ErrorInConfig: damage dealt: "+damage);
			}
			
		/*
		 * player misses
		 */
		}else{
			Player opponent = getOpponent(attempter);
			String[] wm = Weapons.getWeaponAndMat(attempter.getItemInHand().getType());
			
			List<String> messages=null;
			if(wm[1].equals("")){
				messages = plugin.getConfig().getStringList("weapons."+wm[0]+".messages.miss");
			}else{
				messages = plugin.getConfig().getStringList("weapons."+wm[1]+"."+wm[0]+".messages.miss");
			}
			
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
		if(!fightStarted || won){
			return;
		}
		
		int random = Combat.rand.nextInt(100)+1;
		int chancesOfFlee = plugin.getConfig().getInt("chances.flee");
		
		if(random < chancesOfFlee){
			//flee success
			attempter.sendMessage(plugin.getConfig().getString("fleeMessages.success").replace("&", "§"));
			closeWithoutMessage();
		}else{
			//flee fail
			attempter.sendMessage(plugin.getConfig().getString("fleeMessages.fail").replace("&", "§"));
			attempter.teleport(getOpponent(attempter));
		}
		
		changeTurn();
	}
	
	
	
	
	/*
	 * called when a player in fight dies
	 * player (args0) is the player who died
	 */
	public void playerDeath(Player player){
		won = true;
		winner = getOpponent(player);
		
		String winmessage = plugin.getConfig().getString("winMessages.winMessage");
		String mercyMessage = plugin.getConfig().getString("winMessages.mercy");
		String killMessage = plugin.getConfig().getString("winMessages.kill");
		String enslaveMessage = plugin.getConfig().getString("winMessages.enslave");
		winner.sendMessage(winmessage.replace("&", "§"));
		winner.sendMessage(mercyMessage.replace("&", "§"));
		winner.sendMessage(killMessage.replace("&", "§"));
		winner.sendMessage(enslaveMessage.replace("&", "§"));
	}
	
	
	public Player getWinner(){
		return winner;
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
	 * calls when player types "kill", "enslave" or "mercy" when the fight has been won
	 */
	public void possibleWinMessage(Player winner, String message){
		Player loser = getOpponent(winner);
		
		if(loser.isDead()){
			winner.sendMessage(ChatColor.YELLOW+"[ArithiaCombat] please wait for player to respawn");
			return;
		}
		
		if(message.equalsIgnoreCase("enslave")){
			broadcast(
					ChatColor.GOLD+winner.getDisplayName()+
					ChatColor.GOLD+" has enslaved: "+loser.getDisplayName()
			);
			
			plugin.enslavedPlayers.put(loser, winner);
			plugin.enslavedPlayersTimer.put(loser, plugin.getConfig().getInt("enslavementTime"));
			closeWithoutMessage();
		}else if(message.equalsIgnoreCase("kill")){
			broadcast(
				ChatColor.GOLD+winner.getDisplayName()+ChatColor.GOLD+
				" has killed: "+ loser.getDisplayName()
			);
			
			plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "killchar "+loser.getName());
			closeWithoutMessage();
		}else{//player typed mercy
			broadcast(
					ChatColor.GOLD+winner.getDisplayName()+ChatColor.GOLD+
					" has knocked out: "+ loser.getDisplayName()
				);
			loser.setHealth(0.0);
			closeWithoutMessage();
		}
	}
	
	public boolean hasBeenWon(){
		return won;
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
		
		player2.setMaxHealth(20.0);
		plugin.removeFight(this);
	}
	
	
	/*
	 * close Fight
	 */
	public void close(){
		broadcast(ChatColor.DARK_RED+"Fight Closing");
		player1.setMaxHealth(20.0);
		player2.setMaxHealth(20.0);
		plugin.removeFight(this);
	}
}
