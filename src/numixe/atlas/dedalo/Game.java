package numixe.atlas.dedalo;

public class Game {
	
	public Lobby lobby;
	public boolean running;

	public Game(Lobby lobby) {
		
		this.lobby = lobby;
		running = false;
	}
	
	public void start() {
		
		running = true;
	}
}
