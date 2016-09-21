package numixe.atlas.dedalo;

public class Game {
	
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
