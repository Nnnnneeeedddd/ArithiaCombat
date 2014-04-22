package com.arithia.combat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arithia.commands.FightsCommand;
import com.arithia.listeners.PlayerListener;

public class Combat extends JavaPlugin{
	public static final Random rand = new Random();
	private ArrayList<Player> pvpPlayers = new ArrayList<Player>();
	public HashMap<Player, Player> enslavedPlayers = new HashMap<Player, Player>();
	public HashMap<Player, Integer> enslavedPlayersTimer = new HashMap<Player, Integer>();
	private ArrayList<Fight> fights = new ArrayList<Fight>();
	
	/*
	 * TODO AND CAVEAT LIST
	 * 
	 * sort out enslaved players
	 * add enslaved players timer
	 * 
	 */
	
	@Override
	public void onEnable(){
		listeners(getServer().getPluginManager());
		commands();
		
		Configuration.config(this);
		startTimer();
	}
	
	/*
	 * registers listeners
	 */
	private void listeners(PluginManager pm){
		PlayerListener playerListener = new PlayerListener(this);
		
		pm.registerEvents(playerListener, this);
	}
	
	/*
	 * sets command executors
	 */
	private void commands(){
		FightsCommand fightsExecutor = new FightsCommand(this);
		
		getServer().getPluginCommand("fights").setExecutor(fightsExecutor);
	}
	
	public void addFight(Fight fight){
		fights.add(fight);
	}
	
	public void removeFight(Fight fight){
		fights.remove(fight);
	}
	
	public ArrayList<Player> getPVPPlayers(){
		return pvpPlayers;
	}
	
	public void addPVPPlayer(Player playertoadd){
		pvpPlayers.add(playertoadd);
	}
	
	public void removePVPPlayer(Player playertoremove){
		pvpPlayers.remove(playertoremove);
	}
	
	/*
	 * gets the fight a player is in.
	 * param1: Player in fight.
	 */
	public Fight getFight(Player player){
		
		for(Fight fight: fights){
			Player[] players = fight.getPlayers();
			
			if(players[0] == player){
				return fight;
			}
			
			else if(players[1] == player){
				return fight;
			}
		}
		
		return null;
	}
	
	public void startTimer(){
		
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
			@Override
			public void run(){
				for(Player player: getServer().getOnlinePlayers()){
					if(enslavedPlayersTimer.containsKey(player)){
						int time = enslavedPlayersTimer.get(player);
						if(time == 0){
							enslavedPlayersTimer.remove(player);
							enslavedPlayers.remove(player);
							player.sendMessage(ChatColor.GREEN+"[ArithiaCombat] You are free");
						}else{
							time--;
							enslavedPlayersTimer.put(player, time);
						}
					}
				}
			}
		}, 10L, 10L);
		
	}
	
	
	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	
	/*
	 * return size of variable: fights
	 */
	public int getFightNumber(){
		return fights.size();
	}
}
