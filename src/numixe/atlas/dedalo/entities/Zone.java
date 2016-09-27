package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import static numixe.atlas.dedalo.Dedalo.game;

public class Zone {
	
	private List<BlockNode> blocks;
	String name;
	private List<Spawn> spawns;
	private List<DChest> chests;
	private BlockNode furthest;

	public Zone(String name) {
		
		blocks = new ArrayList<BlockNode>();
		this.name = name;
		spawns = new ArrayList<Spawn>();	// possible spawns
		chests = new ArrayList<DChest>();
		furthest = null;
	}
	
	public void spawnBlocks(Location position) {
		
		// spawns all registered blocks
		
		for (BlockNode node : blocks) {
			
			node.spawnBlock(position);
		}
	}
	
	public void destroyBlocks(Location position) {
		
		// destroy all registered blocks
		
		for (BlockNode node : blocks) {
			
			node.destroyBlock(position);
		}
	}
	
	public void addSpawn(String name, Location absolute, Location reference) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name))
				return;
		}
		
		spawns.add(new Spawn(name, absolute, reference, this));
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
	
	public void addChest(String name, Location absolute, Location reference) {
		
		chests.add(new DChest(name, absolute, reference, this));
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
	
	public Location furthestLocation(final Location reference) {
		
		return reference.add(furthest.relative);
	}
	
	public void setFurthest(BlockNode arg) {
		
		this.furthest = arg;
	}
	
	public BlockNode getFurthest() {
		
		return furthest;
	}
	
	public static void writeBlock(BlockNode node) {
		
		// write to init.yml
	}
	
	public static List<BlockNode> loadBlocks() {
		
		List<BlockNode> out = new ArrayList<BlockNode>();
		
		// load from init.yml
		
		return out;
	}
	
	public static void writeZone(Zone zone) {
		
		// write zone to init.yml
	}
	
	public static Zone loadZone(String name) {
		
		Zone out = new Zone(name);
		
		//load zone from init.yml
		
		return out;
	}
	
	public class Spawn {
		
		// struct Spawn
		
		public Vector location;
		public String name;
		public Zone owner;
		
		public Spawn(String name, final Location absolute, final Location reference, Zone owner) {
			
			this.location = absolute.subtract(reference).toVector();
			this.name = name;
			this.owner = owner;
		}
	}
	
	public class DChest {
		
		public Chest chest;
		public Inventory inventory;
		public Vector location;
		public String name;
		public Zone owner;
		
		public DChest(String name, final Location absolute, final Location reference, Zone owner) {
			
			// da sistemare con le posizioni relative
			
			this.location = absolute.subtract(reference).toVector();
			this.name = name;
			this.owner = owner;
			
			chest = null;
			inventory = null;
		}
		
		public void generate(Inventory init) {
			
			location.toLocation(game.field.world).getBlock().setType(Material.CHEST);
			
			chest = (Chest) location.toLocation(game.field.world).getBlock().getState();
			inventory = chest.getBlockInventory();
			
			if (init == null)
				return;
			
			this.replaceInventory(init);
		}
		
		public void destroy() {
			
			location.toLocation(game.field.world).getBlock().setType(Material.AIR);
		}
		
		public void replaceInventory(Inventory inv) {
			
			inventory.setContents(inv.getContents());
		}
	}
	
	public class BlockNode {
		
		public final Vector relative;
		public final MaterialData blockdata;
		
		public BlockNode(final Block absolute, final Location reference) {
			
			relative = absolute.getLocation().subtract(reference).toVector();
			blockdata = absolute.getState().getData();
		}
		
		@SuppressWarnings("deprecation")
		public BlockNode(final Vector relative, Material material, byte data) {
			
			this.relative = relative;
			blockdata = new MaterialData(material, data);
		}
		
		public Block spawnBlock(final Location reference) {
			
			Location absolute = reference.add(relative);
			Block out = absolute.getBlock();
			out.getState().setData(blockdata);
			
			return out;
		}
		
		public void destroyBlock(final Location reference) {
			
			Location absolute = reference.add(relative);
			Block out = absolute.getBlock();
			out.setType(Material.AIR);
		}
		
		@SuppressWarnings("deprecation")
		public byte getData() {
			
			return blockdata.getData();
		}
		
		public Material getType() {
			
			return blockdata.getItemType();
		}
	}
}
