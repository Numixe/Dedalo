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
		
		String color = "";
		
		if (name.equalsIgnoreCase("red"))
			color = "§c";
		else if (name.equalsIgnoreCase("blue"))
			color = "§9";
		
		p.player.sendMessage("§9Dedalo> §7Sei stato aggiunto al Team " + color + name + "!");
	}
	
	public int size() {
		
		return players.size();
	}
}
