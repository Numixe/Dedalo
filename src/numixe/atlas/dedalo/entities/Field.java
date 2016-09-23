package numixe.atlas.dedalo.entities;

import static numixe.atlas.dedalo.Dedalo.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Field {
	
	public List<List<Zone>> zones;
	public Location[] spawns;

	public Field() {
		
		zones = new ArrayList<List<Zone>>();
		spawns = new Location[2];
	}
	
	public Location spawnLocation(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team.id];
	}
	
	public boolean isOnSpawn(Player p) {
		
		return p.getLocation().getBlock().equals( spawnLocation(game.lobby.ownedBy(p)).getBlock());
	}
	
	public static Field loadField() {
		
		Field out = new Field();
		
		// load from init.yml
		
		return out;
	}
}
