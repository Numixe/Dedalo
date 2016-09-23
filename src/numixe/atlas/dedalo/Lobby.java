package numixe.atlas.dedalo;

import java.util.HashMap;

import numixe.atlas.dedalo.entities.DPlayer;
import numixe.atlas.dedalo.entities.Team;
import numixe.atlas.dedalo.listeners.WoolEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Lobby {
	
	public Team[] teams;
	private HashMap<String, Integer> map;	// register player team index, more search efficiency
	public static final String INVENTORY_NAME = "�9�lTeam Chooser";
	
	public Lobby() {
		
		teams = new Team[2];
		teams[0] = new Team(0, "Red");
		teams[1] = new Team(1, "Blue");
		
		map = new HashMap<String, Integer>();
	}
	
	public void addToTeam(int team_index, DPlayer p) {
		
		teams[team_index].addPlayer(p);
		map.put(p.getName(), team_index);
	}
	
	public void chooseTeam(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, INVENTORY_NAME);
		
		inv.setItem(0, WoolEvents.redWool);
		inv.setItem(1, WoolEvents.blueWool);
		
		p.openInventory(inv);
	}

	public boolean isFull() {
		
		return teams[0].size() == Team.MAX_PLAYERS && teams[1].size() == Team.MAX_PLAYERS;
	}
	
	public Team ownedBy(Player p) {
		
		Integer index = map.get(p.getName());
		
		if (index == null)
			return null;
		
		return teams[index];
	}
	
	public DPlayer getPlayer(Player p) {
		
		Team team = ownedBy(p);
		
		if (team == null)
			return null;
		
		return team.getPlayer(p.getName());
	 }
}
