package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

public class Team {

	public List<DPlayer> players;
	public String name;
	public int id;
	public static final int MAX_PLAYERS = 16;
	
	public Team(int id, String name) {
		
		players = new ArrayList<DPlayer>();
		this.name = name;
		this.id = id;
	}
	
	public void addPlayer(DPlayer p) {
		
		if (players.size() >= MAX_PLAYERS)
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
