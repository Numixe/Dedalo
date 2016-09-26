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

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
		timeEvents = null;
		field = Field.loadField();
		random = new Random();
	}
	
	public void start() {
		
		// configure start event
		running = true;
		timeEvents = new TimeEvent(180); // 3 minuti
		Bukkit.getServer().broadcastMessage("§9Starting game");
		
		Bukkit.getServer().getPluginManager().registerEvents(playerEvents = new PlayerEvents(), plugin);
		Bukkit.getServer().getPluginManager().registerEvents(reloadTouchEvents = new ReloadTouchEvents(), plugin);
	}
	
	public boolean isRunning() {
		
		return running;
	}
	
	public void finish() {
		
		// configure finish event
		Bukkit.getServer().broadcastMessage("§9Finishing game");
		running = false;
		timeEvents.destroy();
		
		HandlerList.unregisterAll(playerEvents);
		HandlerList.unregisterAll(reloadTouchEvents);
	}
}
