package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;

import static numixe.atlas.dedalo.Dedalo.game;

public class Zone {
	
	List<Block> blocks;
	String name;
	private List<Spawn> spawns;

	public Zone(String name) {
		
		blocks = new ArrayList<Block>();
		this.name = name;
		spawns = new ArrayList<Spawn>();	// possible spawns
	}
	
	public static void writeZone(Zone zone) {
		
		// write zone to init.yml
	}
	
	public static Zone loadZone(String name) {
		
		Zone out = new Zone(name);
		
		//load zone from init.yml
		
		return out;
	}
	
	public void addSpawn(String name, Location loc) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name))
				return;
		}
		
		spawns.add(new Spawn(name, loc));
	}
	
	public Spawn getSpawn(String name) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name))
				return i;
		}
		
		return null;
	}
	
	public Spawn getSpawn(int index) {
		
		return spawns.get(index);
	}
	
	public Spawn randomSpawn() {
		
		int index = game.random.nextInt(spawns.size());
		
		return spawns.get(index);
	}
	
	public void removeSpawn(String name) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name)) {
				
				spawns.remove(i);
				return;
			}
		}
		
		throw new NullPointerException("Removing a not existing spawn");
	}
	
	public void removeSpawn(int index) {
		
		spawns.remove(index);
	}
	
	public class Spawn {
		
		// struct Spawn
		
		public Location location;
		public String name;
		
		public Spawn(String name, Location location) {
			
			this.location = location;
			this.name = name;
		}
	}
}
