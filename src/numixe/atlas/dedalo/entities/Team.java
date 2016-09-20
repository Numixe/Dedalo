package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

public class Team {

	public List<DPlayer> players;
	public String name;
	public static final int MAX_PLAYERS = 16;
	
	public Team(String name) {
		
		players = new ArrayList<DPlayer>();
		this.name = name;
	}
	
	public void addPlayer(DPlayer p) {
		
		if (players.size() >= MAX_PLAYERS || players.contains(p))
			return;
		
		players.add(p);
		
		p.player.sendMessage("Sei stato aggiunto a " + name + "!!");
	}
}
