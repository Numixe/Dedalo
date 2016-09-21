package numixe.atlas.dedalo;

public class Game {
	
	public Lobby lobby;
	private boolean running;

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
	}
	
	public void start() {
		
		// configure start event
		running = true;
	}
	
	public boolean isRunning() {
		
		return running;
	}
	
	public void finish() {
		
		// configure finish event
		running = false;
	}
}
