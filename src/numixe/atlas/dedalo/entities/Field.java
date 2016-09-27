package numixe.atlas.dedalo.entities;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import static numixe.atlas.dedalo.Dedalo.*;

import numixe.atlas.dedalo.entities.Zone.*;

public class Field {
	
	public ZoneNode[][] map;	// bidimensional map
	public Spawn[] spawns;		// current spawns
	public DChest[] chests;		// current chests
	
	public final int chest_size;
	public World world;
	
	public static final int MIN_DISTANCE = 2;
	public static final int DEFAULT_X_SIZE = 4;
	public static final int DEFAULT_CHEST_SIZE = 20;
	public static final int DEFAULT_ZONE_CHANGE = 3;
	public static final World DEFAULT_WORLD = Bukkit.getWorld("world");

	public Field() {
		
		spawns = new Spawn[2];
		map = null;
		world = DEFAULT_WORLD;
		
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
	}
	
	public DChest[] getChests() {
		
		return chests;
	}
	
	public Location getCurrentSpawnLocation(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team.id].location.toLocation(world);
	}
	
	public Location getCurrentSpawnLocation(int team_index) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team_index].location.toLocation(world);
	}
	
	private Vector2i randomZoneCoords() {
		
		Vector2i out = new Vector2i();
		
		for (int i = 0; i < map.length; i++) {
		
			out.x = game.random.nextInt(map.length);
		
			if (map[out.x].length != 0) {
				
				for (int j = 0; j < map[out.x].length; j++) {
					
					out.y = game.random.nextInt(map[out.x].length);
					
					if (map[out.x][out.y].zone != null)
						break;
				}
				
				break;
			}
		}
		
		return out;
	}
	
	
	
	public void refreshSpawns() {
		
		if (map == null) {
			
			spawns[0] = null;
			spawns[1] = null;
			return;
		}
		
		Vector2i zc1 = randomZoneCoords();
		Vector2i zc2 = null;
		
		int max = map.length * map.length;
		
		for (int p = 0; p < max; p++) {
			
			zc2 = randomZoneCoords();
			
			if (zc1.distance(zc2) >= MIN_DISTANCE)	// check for a minimal distance of the spawn zones
				break;
			
			// se non esistono zone abbastanza lontane verra' presa l'ultima generata
		}
		
		Zone firstZone = map[zc1.x] [zc1.y].zone;
		Zone secondZone = map[zc2.x] [zc2.y].zone;
		
		spawns[0] = firstZone.randomSpawn();
		spawns[1] = secondZone.randomSpawn();
	}
	
	public void refreshZones() {
		
		ArrayList<Vector2i> changed = new ArrayList<Vector2i>();
		
		while (changed.size() < DEFAULT_ZONE_CHANGE) {
			
			Vector2i coords = randomZoneCoords();
			
			if (changed.contains(coords))
				continue;
			
			String[] list = map[coords.x][coords.y].possibleZones;
			
			int randomIndex = game.random.nextInt(list.length);
			
			map[coords.x][coords.y].zone = Zone.loadZone(list[randomIndex]);
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
	
	public boolean initialize() {
		
		// check for null pointers
		
		for (ZoneNode[] i : map) {
			
			if (i == null)
				return false;
			else {
				
				for (ZoneNode j : i) {
					
					if (j == null)
						return false;
					else if (j.position == null || j.possibleZones == null)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public static Field loadField() {
		
		Field out = new Field();
		
		// load from init.yml
		
		return out;
	}
	
	public class Vector2i {
		
		public int x;
		public int y;
		
		public double distance(Vector2i v) {
			
			int x = this.x - v.x;
			int y = this.y - v.y;
			
			return Math.sqrt(x * x + y * y);
		}
	}
	
	public class ZoneNode {
		
		public Location position;
		public Zone zone;
		public String[] possibleZones;
	}
}
