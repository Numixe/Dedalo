package numixe.atlas.dedalo;

import numixe.atlas.dedalo.entities.DPlayer;
import numixe.atlas.dedalo.entities.Team;
import numixe.atlas.dedalo.listeners.WoolEvents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class Lobby {
	
	public Team[] teams;
	
	public Lobby() {
		
		teams = new Team[2];
		teams[0] = new Team("Red");
		teams[1] = new Team("Blue");
	}
	
	public void addToTeam(int team_index, DPlayer p) {
		
		teams[team_index].addPlayer(p);
	}
	
	public void chooseTeam(Player p) {
		
		Inventory inv = Bukkit.createInventory(null, 9, "§9§lTeam Chooser");
		
		inv.setItem(0, WoolEvents.redWool);
		inv.setItem(1, WoolEvents.blueWool);
		
		p.openInventory(inv);
	}

	public boolean isFull() {
		
		return teams[0].size() == Team.MAX_PLAYERS && teams[1].size() == Team.MAX_PLAYERS;
	}
	
	public Team ownedBy(Player pl) {
		
		for (Team t : teams) {
		
			for (DPlayer p : t.players) {
			
				if (p.player.equals(pl))
					return t;
			}
		}
		
		return null;
	}
	
	public DPlayer getPlayer(Player pl) {
		
		for (Team t : teams) {
			
			for (DPlayer p : t.players) {
				
				if (p.player.equals(pl))
					return p;
			}
		}
		
		return null;
	 }
}
