package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import static numixe.atlas.dedalo.Dedalo.*;

import numixe.atlas.dedalo.entities.Zone.*;

public class Field {
	
	public ZoneNode[][] map;	// bidimensional map
	public HashMap<String, Vector2i> spawnCoords;		// current spawns
	
	public World world;
	
	public static final int MIN_DISTANCE = 2;
	public static final int DEFAULT_CHEST_SIZE = 20;
	public static final int DEFAULT_CHEST_PER_ZONE = 6;
	public static final int DEFAULT_ZONE_CHANGE = 3;
	
	public static final World DEFAULT_WORLD = Bukkit.getWorld("world");

	public Field() {
		
		spawnCoords = new HashMap<String, Vector2i>();
		map = null;
		world = DEFAULT_WORLD;
	}
	
	public Location getCurrentSpawnLocation(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		Vector2i coords = spawnCoords.get(team.name);
		return map[coords.x][coords.y].spawnLocation();
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
		
		Team[] teams = game.lobby.teams;
		
		for (Team i : teams) {
			
			if (spawnCoords.containsKey(i.name)) {		// remove old spawns
				
				Vector2i old  =spawnCoords.get(i.name);
				map[old.x][old.y].deleteSpawn();
				spawnCoords.remove(i.name);
			}
		}
		
		ZoneNode firstZone = map[zc1.x] [zc1.y];
		ZoneNode secondZone = map[zc2.x] [zc2.y];
		
		firstZone.setRandomSpawn();
		secondZone.setRandomSpawn();
		
		spawnCoords.put(teams[0].name, zc1);
		spawnCoords.put(teams[1].name, zc2);
	}
	
	public void refreshZones() {
		
		ArrayList<Vector2i> changed = new ArrayList<Vector2i>();
		
		while (changed.size() < DEFAULT_ZONE_CHANGE) {
			
			Vector2i coords = randomZoneCoords();
			
			if (changed.contains(coords))
				continue;
			
			map[coords.x][coords.y].assignRandomZone();
			map[coords.x][coords.y].zone.spawnBlocks(map[coords.x][coords.y].position);
			
			changed.add(coords);
			
			List<DPlayer> players = map[coords.x][coords.y].playersInside();
			
			for (DPlayer p : players) {
				
				Location current;
				
				while ((current = p.getLocation()).getBlock().getType() != Material.AIR) {	
					
					// sposta il giocatore in direzione del centro
					
					Vector unit = map[coords.x][coords.y].getCenter().subtract(current).toVector().normalize();
					p.player.teleport(current.add(unit));
				}
			}
		}
	}
	
	public void refreshChests() {
		
		ArrayList<Vector2i> changed = new ArrayList<Vector2i>();
		int csize = 0;
		
		while (changed.size() < DEFAULT_ZONE_CHANGE && csize < DEFAULT_CHEST_SIZE) {
			
			Vector2i coords = randomZoneCoords();
			
			if (changed.contains(coords))
				continue;
			
			int addsize = game.random.nextInt(DEFAULT_CHEST_PER_ZONE);
			csize += addsize;
			
			if (csize > DEFAULT_CHEST_SIZE)
				addsize = csize - DEFAULT_CHEST_SIZE;
			
			map[coords.x][coords.y].spawnRandomChests(addsize);
		}
	}
	
	public void refreshAll() {
		
		// changes the configuration
		
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
					else {
						
						// spawn zones
						
						j.assignRandomZone();
						j.zone.spawnBlocks(j.position);
					}
				}
			}
		}
		
		if (game.chestMode)
			refreshChests();
		
		refreshSpawns();
		
		return true;
	}
	
	public void destroy() {
		
		for (ZoneNode[] i : map) {
			
			for (ZoneNode j : i) {
				
				j.destroyZoneNode();
			}
		}
		
		spawnCoords.clear();
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
		public int[] chestsIndices;
		public Spawn spawn;			// null if no spawn
		
		public void assignRandomZone() {
			
			if (possibleZones == null)
				return;
			
			int rand;
			
			if (zone == null) 
				rand = game.random.nextInt(possibleZones.length);
			else {
				
				while (zone.name == possibleZones[rand = game.random.nextInt(possibleZones.length)])
					;
				
				zone.destroyBlocks(position);
			}
			
			zone = Zone.loadZone(possibleZones[rand]);
		}
		
		public void spawnRandomChests(int size) {
			
			if (zone == null)
				return;
			
			destroyChests();
			
			chestsIndices = new int[size];
			
			for (int i = 0; i < size; i++) {
				
				chestsIndices[i] = game.random.nextInt(zone.chestsSize());	// random chest location
				int inv = game.random.nextInt(Zone.INVENTORIES.length);	// random chest inventory
				
				zone.spawnChest(position, inv, chestsIndices[i]);
			}
		}
		
		public void destroyZoneNode() {
			
			if (zone == null)
				return;
			
			zone.destroyBlocks(position);
			
			destroyChests();
		}
		
		public void destroyChests() {
			
			if (chestsIndices == null)
				return;
			
			zone.destroyChests(position, chestsIndices);
			chestsIndices = null;
		}
		
		public List<DPlayer> playersInside() {
			
			List<DPlayer> out = new ArrayList<DPlayer>();
			
			Location furthest = zone.furthestLocation(position).subtract(position);
			
			for (DPlayer p : game.lobby.getPlayers()) {
				
				Location loc = p.player.getLocation().clone().subtract(position);
				int x = Math.abs(loc.getBlockX());
				int z = Math.abs(loc.getBlockZ());
				int fx = Math.abs(furthest.getBlockX());
				int fz = Math.abs(furthest.getBlockZ());
				
				if (x <= fx && z <= fz)
					out.add(p);
			}
			
			return out;
		}
		
		public Location getCenter() {
			
			return zone.getCenter(position);
		}
		
		public void setRandomSpawn() {
			
			spawn = zone.randomSpawn();
		}
		
		public void deleteSpawn() {
			
			spawn = null;
		}
		
		public Location spawnLocation() {
			
			return position.clone().add(spawn.location);
		}
	}
}
