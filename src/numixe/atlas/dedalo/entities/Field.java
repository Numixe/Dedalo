package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import static numixe.atlas.dedalo.Dedalo.*;

import numixe.atlas.dedalo.entities.Zone.*;

public class Field {
	
	public Zone[][] zones;
	public Location[][] positions;
	public Spawn[] spawns;		// current spawns
	public DChest[] chests;		// current chests
	
	private List<String> possibleZones;
	
	public final int x_size;
	public final int chest_size;
	
	public static final int MIN_DISTANCE = 2;
	public static final int DEFAULT_X_SIZE = 4;
	public static final int DEFAULT_CHEST_SIZE = 20;
	public static final int DEFAULT_ZONE_CHANGE = 3;

	public Field() {
		
		if (plugin.getConfig().contains("x_size"))
			x_size = plugin.getConfig().getInt("x_size");
		else {
			x_size = DEFAULT_X_SIZE;
			plugin.getConfig().createSection("x_size");
			plugin.getConfig().set("x_size", x_size);
		}
		
		zones = new Zone[x_size][];
		positions = new Location[x_size][];
		spawns = new Spawn[2];
		possibleZones = new ArrayList<String>();
		
		if (game.chestMode) {
			
			if (plugin.getConfig().contains("chests"))
				chest_size = plugin.getConfig().getInt("chests");
			else {
				plugin.getConfig().createSection("chests");
				plugin.getConfig().set("chests", DEFAULT_CHEST_SIZE);
				chest_size = DEFAULT_CHEST_SIZE;
			}
						
			chests = new DChest[chest_size];
			
		} else {
			
			chest_size = 0;
			chests = null;
		}
		
		if (plugin.getInit().contains("zones")) {
			
			for (String name : plugin.getInit().getConfigurationSection("zones").getKeys(true)) {
				
				possibleZones.add(name);
			}
		}
	}
	
	public DChest[] getChests() {
		
		return chests;
	}
	
	public Spawn getCurrentSpawn(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team.id];
	}
	
	public Spawn getCurrentSpawn(int team_index) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team_index];
	}
	
	private Vector2i randomZoneCoords() {
		
		Vector2i out = new Vector2i();
		
		for (int i = 0; i < zones.length; i++) {
		
			out.x = game.random.nextInt(zones.length);
		
			if (zones[out.x].length != 0) {
				
				for (int j = 0; j < zones[out.x].length; j++) {
					
					out.y = game.random.nextInt(zones[out.x].length);
					
					if (zones[out.x][out.y] != null)
						break;
				}
				
				break;
			}
		}
		
		return out;
	}
	
	
	
	public void refreshSpawns() {
		
		if (zones.length == 0) {
			
			spawns[0] = null;
			spawns[1] = null;
			return;
		}
		
		Vector2i zc1 = randomZoneCoords();
		Vector2i zc2 = null;
		
		int max = zones.length * zones.length;
		
		for (int p = 0; p < max; p++) {
			
			zc2 = randomZoneCoords();
			
			if (zc1.distance(zc2) >= MIN_DISTANCE)	// check for a minimal distance of the spawn zones
				break;
			
			// se non esistono zone abbastanza lontane verra' presa l'ultima generata
		}
		
		Zone firstZone = zones[zc1.x] [zc1.y];
		Zone secondZone = zones[zc2.x] [zc2.y];
		
		spawns[0] = firstZone.randomSpawn();
		spawns[1] = secondZone.randomSpawn();
	}
	
	public void refreshZones() {
		
		ArrayList<Vector2i> changed = new ArrayList<Vector2i>();
		
		while (changed.size() < DEFAULT_ZONE_CHANGE) {
			
			Vector2i coords = randomZoneCoords();
			
			if (changed.contains(coords))
				continue;
			
			int randomIndex = game.random.nextInt(possibleZones.size());
			
			zones[coords.x][coords.y] = Zone.loadZone(possibleZones.get(randomIndex));
			changed.add(coords);
		}
	}
	
	public void refreshChests() {
		
		// ancora da implementare
	}
	
	public void refreshAll() {
		
		// change the configuration
		
		refreshZones();
		
		if (game.chestMode)
			refreshChests();
		
		refreshSpawns();
	}
	
	public static Field loadField() {
		
		Field out = new Field();
		
		// load from init.yml
		
		return out;
	}
	
	public class Vector2i {
		
		int x;
		int y;
		
		public Vector2i() {}
		
		public Vector2i(int x, int y) {
			
			this.x = x;
			this.y = y;
		}
		
		public double distance(Vector2i v) {
			
			int x = this.x - v.x;
			int y = this.y - v.y;
			
			return Math.sqrt(x * x + y * y);
		}
	}
}
