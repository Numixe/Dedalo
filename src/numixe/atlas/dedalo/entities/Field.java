package numixe.atlas.dedalo.entities;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.block.Chest;

import static numixe.atlas.dedalo.Dedalo.game;

import numixe.atlas.dedalo.entities.Zone.Spawn;

public class Field {
	
	public Zone[][] zones;
	public Location[][] positions;
	public Spawn[] spawns;		// current spawns
	private HashMap<String, Chest> chests;
	
	public final int x_size;
	
	public static final int MIN_DISTANCE = 2;

	public Field(int x_size) {
		
		this.x_size = x_size;
		zones = new Zone[x_size][];
		positions = new Location[x_size][];
		spawns = new Spawn[2];
		chests = new HashMap<String, Chest>();	// String = Zone name, Chest = Block state instance
	}
	
	public Chest[] getChests() {
		
		return chests.values().toArray(new Chest[chests.size()]);
	}
	
	public Spawn getCurrentSpawn(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team.id];
	}
	
	public Spawn getCurrentSpawn(int team_index) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team_index];
	}
	
	private int[] randomZoneCoords() {
		
		int[] out = new int[2];
		
		for (int i = 0; i < zones.length; i++) {
		
			out[0] = game.random.nextInt(zones.length);
		
			if (zones[out[0]].length != 0) {
				
				for (int j = 0; j < zones[out[0]].length; j++) {
					
					out[1] = game.random.nextInt(zones[out[0]].length);
					
					if (zones[out[0]][out[1]] != null)
						break;
				}
				
				break;
			}
		}
		
		return out;
	}
	
	private double distance(int[] v1, int[] v2) {
		
		int x = v2[0] - v1[0];
		int y = v2[1] - v1[1];
		
		return Math.sqrt(x * x + y * y);
	}
	
	public void refreshSpawns() {
		
		if (zones.length == 0) {
			
			spawns[0] = null;
			spawns[1] = null;
			return;
		}
		
		int[] zc1 = randomZoneCoords();
		int[] zc2 = null;
		
		int max = zones.length * zones.length;
		
		for (int p = 0; p < max; p++) {
			
			zc2 = randomZoneCoords();
			
			if (distance(zc1, zc2) >= MIN_DISTANCE)	// check for a minimal distance of the spawn zones
				break;
			
			// se non esistono zone abbastanza lontane verra' presa l'ultima generata
		}
		
		Zone firstZone = zones[zc1[0]] [zc1[1]];
		Zone secondZone = zones[zc2[0]] [zc2[1]];
		
		spawns[0] = firstZone.randomSpawn();
		spawns[1] = secondZone.randomSpawn();
	}
	
	public void refreshChests() {
		
		
	}
	
	public static Field loadField() {
		
		int x_size = 4;
		
		// load data from init.yml
		
		Field out = new Field(x_size);
		
		// load from init.yml
		
		return out;
	}
}
