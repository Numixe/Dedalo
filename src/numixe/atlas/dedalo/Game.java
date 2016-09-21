package numixe.atlas.dedalo;

import org.bukkit.event.Listener;

public class Game implements Listener {
	
	public Lobby lobby;
	private boolean running;

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
	}
	
	public void start() {
		
		running = true;
	}
	
	public boolean isRunning() {
		
		return running;
	}
	
	public void finish() {
		
		running = false;
	}
}
