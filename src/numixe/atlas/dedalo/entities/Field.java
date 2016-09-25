package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import static numixe.atlas.dedalo.Dedalo.game;

import numixe.atlas.dedalo.entities.Zone.Spawn;

public class Field {
	
	public List<List<Zone>> zones;
	public Spawn[] spawns;		// current spawns
	
	public static final int MIN_DISTANCE = 2;

	public Field() {
		
		zones = new ArrayList<List<Zone>>();
		spawns = new Spawn[2];
	}
	
	public Spawn getCurrentSpawn(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team.id];
	}
	
	public Spawn getCurrentSpawn(int team_index) {	// returns the current spawn location (!! it changes during the game !!)
		
		return spawns[team_index];
	}
	
	private int[] randomZoneCoords() {
		
		int[] out = new int[2];
		
		for (int i = 0; i < zones.size(); i++) {
		
			out[0] = game.random.nextInt(zones.size());
		
			if (zones.get(out[0]).size() != 0) {
				
				for (int j = 0; j < zones.get(out[0]).size(); j++) {
					
					out[1] = game.random.nextInt(zones.get(out[0]).size());
					
					if (zones.get(out[0]).get(out[1]) != null)
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
		
		if (zones.size() == 0) {
			
			spawns[0] = null;
			spawns[1] = null;
			return;
		}
		
		int[] zc1 = randomZoneCoords();
		int[] zc2 = null;
		
		int max = zones.size() * zones.size();
		
		for (int p = 0; p < max; p++) {
			
			zc2 = randomZoneCoords();
			
			if (distance(zc1, zc2) >= MIN_DISTANCE)	// check for a minimal distance of the spawn zones
				break;
			
			// se non esistono zone abbastanza lontane verra' presa l'ultima generata
		}
		
		Zone firstZone = zones.get(zc1[0]).get(zc1[1]);
		Zone secondZone = zones.get(zc2[0]).get(zc2[1]);
		
		spawns[0] = firstZone.randomSpawn();
		spawns[1] = secondZone.randomSpawn();
	}
	
	public static Field loadField() {
		
		Field out = new Field();
		
		// load from init.yml
		
		return out;
	}
}
