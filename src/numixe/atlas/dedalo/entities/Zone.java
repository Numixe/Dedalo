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

import static numixe.atlas.dedalo.Dedalo.*;

public class Zone {
	
	private List<BlockNode> blocks;	// informazioni relative ad ogni singolo blocco
	String name;					// nome identificativo della zona, ogni zona registrata ha un proprio nome
	private List<Spawn> spawns;		// potenziali spawn nella zona
	private List<DChest> chests;	// potenziali chest nella zona
	private BlockNode furthest;		// blocco piu' lontano della zona, estremita' opposta
	
	public static final Inventory[] INVENTORIES = loadInventories();	// potenziali inventari nelle chest, comuni a tutte le zone

	public Zone(String name) {
		
		blocks = new ArrayList<BlockNode>();
		this.name = name;
		spawns = new ArrayList<Spawn>();
		chests = new ArrayList<DChest>();
		furthest = null;
	}
	
	/*
	 *  Calcola il centro della zona
	 */
	
	public Location getCenter(final Location position) {
		
		return position.clone().add(furthest.relative).multiply(0.5);
	}
	
	/*
	 *  Genera nel campo tutti i blocchi registrati rispetto a una posizione nel campo
	 */
	
	public void spawnBlocks(Location position) {
		
		for (BlockNode node : blocks) {
			
			node.spawnBlock(position);
		}
	}
	
	/*
	 *  Volatilizza tutti i blocchi registrati rispetto a una posizione nel campo
	 */
	
	public void destroyBlocks(Location position) {
		
		// destroy all registered blocks
		
		for (BlockNode node : blocks) {
			
			node.destroyBlock(position);
		}
	}
	
	/*
	 *  Aggiunge un nuovo spawn alla lista
	 *  absolute = posizione reale dello spawn
	 *  relative = posizione di riferimento
	 */
	
	public void addSpawn(String name, Location absolute, Location reference) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name))
				return;
		}
		
		spawns.add(new Spawn(name, absolute, reference));
	}
	
	/*
	 *  Restituisce uno spawn dal nome
	 */
	
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
	
	/*
	 *  Restituisce uno spawn casuale presente nella lista
	 */
	
	public Spawn randomSpawn() {
		
		int index = game.random.nextInt(spawns.size());
		
		return spawns.get(index);
	}
	
	/*
	 *  Rimuove uno spawn dal nome
	 */
	
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
	
	/*
	 *  Genera una chest nel campo rispetto alla posizione della zona
	 *  inv = indice di scelta dell'inventario della chest
	 *  index = indice di scelta della chest
	 */
	
	public void spawnChest(Location position, int inv, int index) {
		
		DChest chest = chests.get(index);
			
		if (chest.isGenerated())
			return;
			
		chest.generate(position, Zone.INVENTORIES[inv]);
	}
	
	/*
	 *  Volatilizza tutte le chest presenti nella zona
	 */
	
	public void destroyChests(Location position, int[] indices) {
		
		for (int i : indices) {
			
			DChest chest = chests.get(i);
			
			if (chest.isGenerated())
				chest.destroy(position);
		}
	}
	
	/*
	 *  restituisce la dimensione della lista chests
	 */
	
	public int chestsSize() {
		
		return chests.size();
	}
	
	/*
	 *  aggiunge una chest
	 */
	
	public void addChest(String name, Location absolute, Location reference) {
		
		chests.add(new DChest(name, absolute, reference));
	}
	
	/*
	 *  rimuove una chest
	 */
	
	public void removeChest(DChest chest) {
		
		chests.remove(chest);
	}
	
	
	public DChest getChest(int index) {
		
		return chests.get(index);
	}
	
	public void setChest(int index, DChest chest) {
		
		chests.set(index, chest);
	}
	
	/*
	 *  Imposta ad una chest un inventario
	 */
	
	public void setChestInventory(int index, Inventory inv) {
		
		DChest ch = chests.get(index);
		ch.replaceInventory(inv);
	}
	
	/*
	 *  Restituisce la posizione reale del blocco piu' lontano della zona
	 */
	
	public Location furthestLocation(final Location reference) {
		
		return reference.clone().add(furthest.relative);
	}
	
	public void setFurthest(BlockNode arg) {
		
		this.furthest = arg;
	}
	
	public BlockNode getFurthest() {
		
		return furthest;
	}
	
	/*
	 *  Scrive le informazioni di un blocco su init.yml
	 *  Posizione (relativa, non reale)
	 */
	
	public static void writeBlock(Zone zone, Block block, Location position) {
		
		// write to init.yml
		
		BlockNode node = zone.new BlockNode(block, position);
		
		// Il nome del blocco dipende dalla sua posizione
		// Per una ricerca piu' efficiente all'interno di init.yml
		
		String id = node.getID();
		
		if (!plugin.getInit().contains("zones." + zone.name + ".blocks")) {
			
			if (!plugin.getInit().contains("zones"))
				plugin.getInit().createSection("zones");
			
			if (!plugin.getInit().contains("zones." + zone.name))
				plugin.getInit().createSection("zones." + zone.name);
				
			plugin.getInit().createSection("zones." + zone.name + ".blocks");
		}
		
		plugin.getInit().createSection("zones." + zone.name + ".blocks." + id);
		plugin.getInit().createSection("zones." + zone.name + ".blocks." + id + ".location");
		plugin.getInit().createSection("zones." + zone.name + ".blocks." + id + ".type");
		plugin.getInit().createSection("zones." + zone.name + ".blocks." + id + ".data");
		
		plugin.getInit().set("zones." + zone.name + ".blocks." + id + ".location", node.relative);
		plugin.getInit().set("zones." + zone.name + ".blocks." + id + ".type", node.getType().toString());
		plugin.getInit().set("zones." + zone.name + ".blocks." + id + ".data", node.getData());
	}
	
	/*
	 *  Carica da init.yml tutti i blocchi 
	 */
	
	public static List<BlockNode> loadBlocks(Zone zone) {
		
		List<BlockNode> out = new ArrayList<BlockNode>();
		
		// load from init.yml
		
		return out;
	}
	
	/*
	 *  Carica tutti gli inventari delle chest da init.yml
	 */
	
	public final static Inventory[] loadInventories() {
		
		List<Inventory> out = new ArrayList<Inventory>();
		
		// load from init.yml
		
		return out.toArray(new Inventory[out.size()]);
	}
	
	/*
	 *  Scrive un nuovo inventario su init.yml
	 */
	
	public static void writeNewInventory(Inventory inv) {
		
		// write to init.yml
	}
	
	/*
	 *  Scrive o sovrascrive una zona si init.yml
	 */
	
	public static void writeZone(Zone zone) {
		
		// write zone to init.yml
	}
	
	/*
	 *  Carica una zona da init.yml
	 */
	
	public static Zone loadZone(String name) {
		
		Zone out = new Zone(name);
		
		//load zone from init.yml
		
		return out;
	}
	
	public class Spawn {	// informazioni relative a uno spawn
		
		// struct Spawn
		
		public Vector location;	// posizione relativa in una zona
		public String name;		// nome identificativo
		
		public Spawn(String name, final Location absolute, final Location reference) {
			
			this.location = absolute.clone().subtract(reference).toVector();
			this.name = name;
		}
	}
	
	public class DChest {
		
		public Chest chest;				// BlockState della chest
		public Inventory inventory;		// Inventario della chest
		public Vector location;			// Posizione relativa in una zona
		
		public DChest(String name, final Location absolute, final Location reference) {
			
			location = absolute.clone().subtract(reference).toVector();
			
			chest = null;
			inventory = null;
		}
		
		/*
		 *  Genera la chest nel campo rispetto alla posizione della zona
		 */
		
		public void generate(final Location position, Inventory init) {
			
			Block bk = position.clone().add(location).getBlock();
			
			bk.setType(Material.CHEST);
			
			chest = (Chest) bk.getState();
			inventory = chest.getBlockInventory();
			
			if (init == null)
				return;
			
			this.replaceInventory(init);
		}
		
		/*
		 *  Volatilizza la chest
		 */
		
		public void destroy(final Location position) {
			
			position.clone().add(location).getBlock().setType(Material.AIR);
			chest = null;
			inventory = null;
		}
		
		/*
		 *  Sostituisce l'inventario con un altro
		 */
		
		public void replaceInventory(Inventory inv) {
			
			inventory.setContents(inv.getContents());
		}
		
		/*
		 *  restuisce true se la chest esiste nel campo
		 */
		
		public boolean isGenerated() {
			
			return inventory != null;
		}
	}
	
	public class BlockNode {
		
		public final Vector relative;			// posizione relativa del blocco
		public final MaterialData blockdata;	// dati del blocco
		
		public BlockNode(final Block absolute, final Location reference) {
			
			// crea l'oggetto a partire da un blocco esistente
			
			relative = absolute.getLocation().clone().subtract(reference).toVector();
			blockdata = absolute.getState().getData();
		}
		
		@SuppressWarnings("deprecation")
		public BlockNode(final Vector relative, Material material, byte data) {
			
			// crea l'oggetto a partire dalla posizione relativa e i dati del blocco
			
			this.relative = relative;
			blockdata = new MaterialData(material, data);
		}
		
		/*
		 *  Genera il blocco nel campo
		 */
		
		public Block spawnBlock(final Location reference) {
			
			Location absolute = reference.clone().add(relative);
			Block out = absolute.getBlock();
			out.getState().setData(blockdata);
			
			return out;
		}
		
		/*
		 *  Volatilizza il blocco nel campo
		 */
		
		public void destroyBlock(final Location reference) {
			
			Location absolute = reference.clone().add(relative);
			absolute.getBlock().setType(Material.AIR);
		}
		
		/*
		 *  Restituisce un numero di 8bit significativo del blocco
		 */
		
		@SuppressWarnings("deprecation")
		public byte getData() {
			
			return blockdata.getData();
		}
		
		/*
		 *  Restituisce il materiale del blocco
		 */
		
		public Material getType() {
			
			return blockdata.getItemType();
		}
		
		/*
		 *  Returns the String id, based on the location
		 */
		
		public String getID() {
			
			return "x" + relative.getBlockX() + "y" + relative.getBlockY() + "z" + relative.getBlockZ();
		}
	}
}
