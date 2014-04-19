package com.arithia.combat;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.arithia.commands.FightsCommand;
import com.arithia.listeners.PlayerListener;

public class Combat extends JavaPlugin{
	public static final Random rand = new Random();
	private ArrayList<Player> pvpPlayers = new ArrayList<Player>();
	private ArrayList<Fight> fights = new ArrayList<Fight>();
	
	/*
	 * CAVEAT AND TODO LIST
	 * 
	 * Armour defence isn't finished
	 * make it so pvp players are pvp players!!!
	 * 
	 */
	
	@Override
	public void onEnable(){
		listeners(getServer().getPluginManager());
		commands();
		
		Configuration.config(this);
	}
	
	@Override
	public void onDisable(){
		//need to exit all fights here
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
	
	/*
	 * return size of variable: fights
	 */
	public int getFightNumber(){
		return fights.size();
	}
}
