package numixe.atlas.dedalo;

import org.bukkit.Bukkit;

import numixe.atlas.dedalo.listeners.TimeEvent;

public class Game {
	
	public Lobby lobby;
	private volatile boolean running;	// volatile = accessibilita' multithread
	public TimeEvent timeEvents;

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
		timeEvents = null;
	}
	
	public void start() {
		
		// configure start event
		running = true;
		timeEvents = new TimeEvent(180); // 3 minuti
		Bukkit.getServer().broadcastMessage("§9Starting game");
	}
	
	public boolean isRunning() {
		
		return running;
	}
	
	public void finish() {
		
		// configure finish event
		Bukkit.getServer().broadcastMessage("§9Finishing game");
		running = false;
		timeEvents.destroy();
	}
}
