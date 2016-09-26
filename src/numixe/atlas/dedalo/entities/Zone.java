package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import static numixe.atlas.dedalo.Dedalo.game;

public class Zone {
	
	List<Block> blocks;
	String name;
	private List<Spawn> spawns;
	private List<DChest> chests;

	public Zone(String name) {
		
		blocks = new ArrayList<Block>();
		this.name = name;
		spawns = new ArrayList<Spawn>();	// possible spawns
		chests = new ArrayList<DChest>();
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
		
		spawns.add(new Spawn(name, loc, this));
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
	
	public void addChest(String name, Location loc) {
		
		chests.add(new DChest(name, loc, this));
	}
	
	public void removeChest(DChest chest) {
		
		chests.remove(chest);
	}
	
	public DChest getChest(int index) {
		
		return chests.get(index);
	}
	
	public void setChest(int index, DChest chest) {
		
		chests.set(index, chest);
	}
	
	public void setChestInventory(int index, Inventory inv) {
		
		DChest ch = chests.get(index);
		ch.replaceInventory(inv);
	}
	
	public class Spawn {
		
		// struct Spawn
		
		public Location location;
		public String name;
		public int id;
		public Zone owner;
		
		public Spawn(String name, Location location, Zone owner) {
			
			this.location = location;
			this.name = name;
			this.owner = owner;
			
			this.id = game.random.nextInt();
		}
	}
	
	public class DChest {
		
		public Chest chest;
		public Inventory inventory;
		public Location location;
		public String name;
		public int id;
		public Zone owner;
		
		public DChest(String name, Location location, Zone owner) {
			
			this.location = location;
			this.name = name;
			this.owner = owner;
			
			this.id = game.random.nextInt();
			
			chest = null;
			inventory = null;
		}
		
		public void generate(Inventory init) {
			
			location.getBlock().setType(Material.CHEST);
			
			chest = (Chest) location.getBlock().getState();
			inventory = chest.getBlockInventory();
			
			if (init == null)
				return;
			
			this.replaceInventory(init);
		}
		
		public void replaceInventory(Inventory inv) {
			
			inventory.setContents(inv.getContents());
		}
	}
}
