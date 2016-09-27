package numixe.atlas.dedalo.entities;

import java.util.HashMap;

public class Team {

	public String name;
	public int id;
	public static final int MAX_PLAYERS = 16;
	
	private HashMap<String, DPlayer> players;
	
	public Team(int id, String name) {
		
		players = new HashMap<String, DPlayer>();
		this.name = name;
		this.id = id;
	}
	
	public void addPlayer(DPlayer p) {
		
		if (players.size() >= MAX_PLAYERS)
			return;
		
		players.put(p.getName(), p);
		
		String color = "";
		
		if (name.equalsIgnoreCase("red"))
			color = "§c";
		else if (name.equalsIgnoreCase("blue"))
			color = "§9";
		
		p.player.sendMessage("§9Dedalo> §7Sei stato aggiunto al Team " + color + name + "!");
	}
	
	public DPlayer getPlayer(String name) {
		
		return players.get(name);
	}
	
	public DPlayer[] getPlayers() {
		
		return players.values().toArray(new DPlayer[players.size()]);
	}
	
	public int size() {
		
		return players.size();
	}
}
