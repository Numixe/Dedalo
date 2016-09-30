package numixe.atlas.dedalo;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import numixe.atlas.dedalo.entities.Field;
import numixe.atlas.dedalo.listeners.PlayerEvents;
import numixe.atlas.dedalo.listeners.ReloadTouchEvents;
import numixe.atlas.dedalo.listeners.TimeEvent;

import static numixe.atlas.dedalo.Dedalo.plugin;

public class Game {
	
	public Lobby lobby;
	private volatile boolean running;	// volatile = accessibilita' multithread
	public TimeEvent timeEvents;
	public Field field;
	public Random random;
	
	Listener playerEvents, reloadTouchEvents;
	
	// gameMode booleans
	
	public boolean chestMode, reloadTouchMode;	// true by default

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
		timeEvents = null;
		field = Field.loadField();	// carica gia' il campo, ma non generarlo
		random = new Random();
		
		chestMode = true;
		reloadTouchMode = true;
		
		configGameMode();
	}
	
	public void configGameMode() {
		
		if (plugin.getConfig().contains("gamemode")) {
			
			if (plugin.getConfig().contains("gamemode.chest"))
				chestMode = plugin.getConfig().getBoolean("gamemode.chest");
			else {
				plugin.getConfig().createSection("gamemode.chest");
				plugin.getConfig().set("gamemode.chest", true);
			}
			
			if (plugin.getConfig().contains("gamemode.reloadtouch"))
				reloadTouchMode = plugin.getConfig().getBoolean("gamemode.reloadtouch");
			else {
				plugin.getConfig().createSection("gamemode.reloadtouch");
				plugin.getConfig().set("gamemode.reloadtouch", true);
			}
			
		} else {
			
			plugin.getConfig().createSection("gamemode");
			plugin.getConfig().createSection("gamemode.chest");
			plugin.getConfig().createSection("gamemode.reloadTouch");
			
			plugin.getConfig().set("gamemode.chest", true);
			plugin.getConfig().set("gamemode.reloadTouch", true);
		}
	}
	
	/*
	 *  Inizia il gioco
	 *  Da chiamare esternamente quando la lobby e' piena
	 *  O quando viene lanciato il comando /startgame
	 */
	
	public void start() {
		
		if (running)
			return;
		
		// field.initialize() restituisce true se e' andato tutto bene, altrimenti false
		
		if (field.initialize())
			Bukkit.getServer().broadcastMessage("§7Dedalo>§9 Starting game");
		else {
			Bukkit.getServer().broadcastMessage("§7Dedalo>§9 Unable to start the game");
			return;
		}
		
		timeEvents = new TimeEvent(180); // 180 sec = 3 minuti
		Bukkit.getServer().getPluginManager().registerEvents(playerEvents = new PlayerEvents(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(reloadTouchEvents = new ReloadTouchEvents(), plugin);
		
		running = true;
	}
	
	public boolean isRunning() {
		
		return running;
	}
	
	/*
	 *  Termina il gioco
	 *  Da chiamare esternamente quando il tempo e' scaduto
	 *  O quando viene lanciato il comando /finishgame
	 */
	
	public void finish() {
		
		if (!running)
			return;
		
		// configure finish event
		Bukkit.getServer().broadcastMessage("§7Dedalo>§9 Finishing game");
		timeEvents.destroy();
		field.destroy();
		
		HandlerList.unregisterAll(playerEvents);
		HandlerList.unregisterAll(reloadTouchEvents);
		
		running = false;
	}
}
