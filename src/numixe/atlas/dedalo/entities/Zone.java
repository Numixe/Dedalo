package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

import static numixe.atlas.dedalo.Dedalo.*;

public class Zone {
	
	private List<BlockNode> blocks;	// informazioni relative ad ogni singolo blocco
	public String name;					// nome identificativo della zona, ogni zona registrata ha un proprio nome
	private List<Spawn> spawns;		// potenziali spawn nella zona
	private List<DChest> chests;	// potenziali chest nella zona
	private BlockNode furthest;		// blocco piu' lontano della zona, estremita' opposta
	
	public static List<InventoryNode> INVENTORIES = loadInventories();	// potenziali inventari nelle chest, comuni a tutte le zone

	public Zone(String name) {
		
		blocks = new ArrayList<BlockNode>();
		this.name = name;
		spawns = new ArrayList<Spawn>();
		chests = new ArrayList<DChest>();
		furthest = null;
	}
	
	/**
	 *  Calcola il centro della zona
	 */
	
	public Location getCenter(final Location position) {
		
		return position.clone().add(furthest.relative).multiply(0.5);
	}
	
	/**
	 *  Genera nel campo tutti i blocchi registrati rispetto a una posizione nel campo
	 */
	
	public void spawnBlocks(Location position) {
		
		for (BlockNode node : blocks) {
			
			node.spawnBlock(position);
		}
	}
	
	/**
	 *  Volatilizza tutti i blocchi registrati rispetto a una posizione nel campo
	 */
	
	public void destroyBlocks(Location position) {
		
		// destroy all registered blocks
		
		for (BlockNode node : blocks) {
			
			node.destroyBlock(position);
		}
	}
	
	/**
	 *  Metodi di aggiunta e rimozione di blocchi
	 */
	
	public void addBlock(Block block, Location position) {
		
		blocks.add(new BlockNode(block, position));
	}
	
	/**
	 *  Metodi di aggiunta e rimozione di blocchi
	 */
	
	public void removeBlock(Block block, Location position) {
		
		BlockNode node = new BlockNode(block, position);
		
		for (BlockNode b : blocks) {
			
			if (b.relative.equals(node.relative)) {
				
				blocks.remove(b);
				break;
			}
		}
	}
	
	/**
	 *  Metodi di aggiunta e rimozione di blocchi
	 */
	
	public void clearBlocks() {
		
		blocks.clear();
	}
	
	/**
	 *  Aggiunge un nuovo spawn alla lista;
	 *  
	 *  absolute = posizione reale dello spawn;
	 *  
	 *  relative = posizione di riferimento
	 */
	
	public void addSpawn(String name, Location absolute, Location reference) {
		
		for (Spawn i : spawns) {
			
			if (i.name.equalsIgnoreCase(name))
				return;
		}
		
		spawns.add(new Spawn(name, absolute, reference));
	}
	
	/**
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
	
	/**
	 *  Restituisce uno spawn casuale presente nella lista
	 */
	
	public Spawn randomSpawn() {
		
		int index = game.random.nextInt(spawns.size());
		
		return spawns.get(index);
	}
	
	/**
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
	
	/**
	 *  Genera una chest nel campo rispetto alla posizione della zona
	 *  inv = indice di scelta dell'inventario della chest
	 *  index = indice di scelta della chest
	 */
	
	public void spawnChest(Location position, int inv, int index) {
		
		DChest chest = chests.get(index);
			
		if (chest.isGenerated())
			return;
			
		chest.generate(position, Zone.INVENTORIES.get(inv));
	}
	
	/**
	 *  Volatilizza tutte le chest presenti nella zona
	 */
	
	public void destroyChests(Location position, int[] indices) {
		
		for (int i : indices) {
			
			DChest chest = chests.get(i);
			
			if (chest.isGenerated())
				chest.destroy(position);
		}
	}
	
	/**
	 *  restituisce la dimensione della lista chests
	 */
	
	public int chestsSize() {
		
		return chests.size();
	}
	
	/**
	 *  aggiunge una chest
	 */
	
	public void addChest(Location absolute, Location reference) {
		
		chests.add(new DChest(absolute, reference));
	}
	
	/**
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
	
	/**
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
	
	/**
	 *  Scrive le informazioni di un blocco su init.yml
	 *  Posizione (relativa, non reale)
	 */
	
	public static void writeBlock(Zone zone, ConfigurationSection section, BlockNode node) {
		
		// write to init.yml
		
		// Il nome del blocco dipende dalla sua posizione
		// Per una ricerca piu' efficiente all'interno di init.yml
		
		String id = node.getID();
		
		if (section.contains(id))	// se l'ID del blocco esiste gia', esso viene sovrascritto
			section.set(id, null);
		else
			section.createSection(id);
		
		section.createSection(id + ".location");
		section.createSection(id + ".type");
		section.createSection(id + ".data");
		
		section.set(id + ".location", node.relative);
		section.set(id + ".type", node.getType().toString());
		section.set(id + ".data", node.getData());
	}
	
	/**
	 *  Scrive o sovrascrive i blocchi della zone
	 */
	
	public void writeBlocks() {
		
		if (plugin.getInit().contains("zones." + this.name + ".blocks"))
			plugin.getInit().set("zones." + this.name + ".blocks", null);
		else
			plugin.getInit().createSection("zones." + this.name + ".blocks");
		
		ConfigurationSection section = plugin.getInit().getConfigurationSection("zones." + this.name + ".blocks");
		
		for (BlockNode i : blocks)
			writeBlock(this, section, i);
	}
	
	/**
	 *  Carica da init.yml tutti i blocchi 
	 */
	
	public void loadBlocks() {
		
		blocks.clear();
		
		for (String id : plugin.getInit().getConfigurationSection("zones." + this.name + ".blocks").getKeys(false)) {
			
			Vector loc = plugin.getInit().getVector("zones." + this.name + ".blocks." + id + ".location");
			Material mat = Material.matchMaterial(plugin.getInit().getString("zones." + this.name + ".blocks." + id + ".type"));
			byte data = (byte) plugin.getInit().getInt("zones." + this.name + ".blocks." + id + ".data");
			
			blocks.add(new BlockNode(loc, mat, data));
		}
	}
	
	/**
	 *  Scrive una chest di una zona
	 */
	
	public static void writeChest(Zone zone, ConfigurationSection section, DChest chest) {
		
		String id = chest.getID();
		
		if (section.contains(id))
			section.set(id, null);
		else
			section.createSection(id);
		
		section.createSection(id + ".location");
		
		section.set(id + ".location", chest.location);
	}
	
	/**
	 *  Scrive o sovrascrive le chest su init.yml
	 */
	
	public void writeChests() {
		
		if (plugin.getInit().contains("zones." + this.name + ".chests"))
			plugin.getInit().set("zones." + this.name + ".chests", null);
		else
			plugin.getInit().createSection("zones." + this.name + ".chests");
		
		ConfigurationSection section = plugin.getInit().getConfigurationSection("zones." + this.name + ".chests");
		
		for (DChest i : chests)
			writeChest(this, section, i);
	}
	
	/**
	 *  Carica tutte le chest sulla lista
	 */
	
	public void loadChests() {
		
		chests.clear();
		
		ConfigurationSection section = plugin.getInit().getConfigurationSection("zones." + this.name + ".chests");
		
		for (String id : section.getKeys(false)) {
			
			Vector loc = section.getVector(id + ".location");
			
			chests.add(new DChest(loc));
		}
	}
	
	/**
	 *  Carica tutti gli inventari delle chest da init.yml
	 */
	
	public static List<InventoryNode> loadInventories() {
		
		List<InventoryNode> out = new ArrayList<InventoryNode>();
		
		for (String key : plugin.getInit().getConfigurationSection("inventories").getKeys(false)) {
			
			@SuppressWarnings("unchecked")
			List<ItemStack> stacks = (List<ItemStack>) plugin.getInit().getList("inventories." + key);
			
			out.add(new InventoryNode(stacks, key));
		}
		
		return out;
	}
	
	/**
	 *  Scrive un nuovo inventario su init.yml
	 */
	
	public static void writeInventory(InventoryNode node) {
		
		if (plugin.getInit().contains("inventories." + node.name))
			plugin.getInit().set("inventories." + node.name, null);
		else
			plugin.getInit().createSection("inventories." + node.name);		// elimina il contenuto
		
		plugin.getInit().set("inventories." + node.name, node.getStackList());
		INVENTORIES.add(node);
	}
	
	/**
	 *  Scrive uno spawn su init.yml
	 */
	
	public static void writeSpawn(Zone zone, ConfigurationSection section, Spawn spawn) {
		
		if (section.contains(spawn.name))
			section.set(spawn.name, null);
		else
			section.createSection(spawn.name);
		
		section.createSection(spawn.name + ".name");
		section.set(spawn.name + ".name", spawn.name);
		
		section.createSection(spawn.name + ".location");
		section.set(spawn.name + ".name", spawn.location);
	}
	
	/**
	 *  Scrive o sovrascrive tutti gli spawn
	 */
	
	public void writeSpawns() {
		
		if (plugin.getInit().contains("zones." + this.name + ".spawns"))
			plugin.getInit().set("zones." + this.name + ".spawns", null);
		else
			plugin.getInit().createSection("zones." + this.name + ".spawns");
		
		ConfigurationSection section = plugin.getInit().getConfigurationSection("zones." + this.name + ".spawns");
		
		for (Spawn i : spawns)
			writeSpawn(this, section, i);
	}
	
	/**
	 *  Carica gli spawn da init.yml
	 */
	
	public void loadSpawns() {
		
		spawns.clear();
		
		ConfigurationSection section = plugin.getInit().getConfigurationSection("zones." + this.name + ".spawns");
		
		for (String id : section.getKeys(false)) {
			
			Vector loc = section.getVector(id + ".location");
			String name = section.getString(id + ".name");
			
			spawns.add(new Spawn(name, loc));
		}
	}
	
	/**
	 *  Scrive la posizione piu' lontana
	 */
	
	public void writeFurthest() {
		
		if (plugin.getInit().contains("zones." + this.name + ".furthest"))
			plugin.getInit().set("zones." + this.name + ".furthest", null);
		else
			plugin.getInit().createSection("zones." + this.name + ".furthest");
		
		plugin.getInit().set("zones." + this.name + ".furthest", furthest.relative);
	}
	
	/**
	 *  Carica la posizione piu' lontana
	 */
	
	public void loadFurthest() {
		
		Vector loc = plugin.getInit().getVector("zones." + this.name + ".furthest");
		String id = genID(loc);
		
		if (plugin.getInit().contains("zones." + this.name + ".blocks." + id)) {
			
			int index = 0;
			
			for (String key : plugin.getInit().getConfigurationSection("zones." + this.name + ".blocks.").getKeys(false)) {
				
				if (key.equals(id))
					break;
				
				index++;
			}
			
			furthest = blocks.get(index);
			
		} else
			evaluateFurthest();
	}
	
	/**
	 *  Rivaluta il blocco piu' lontano in base alla lista blocks
	 */
	
	public void evaluateFurthest() {
		
		if (blocks.size() == 0) {
			
			furthest = null;
			return;
		}
		
		furthest = blocks.get(0);
		Vector nullV = new Vector(0, 0, 0);
		
		for (BlockNode node : blocks) {
			
			if (node.relative.distanceSquared(nullV) > furthest.relative.distanceSquared(nullV))
				furthest = node;
		}
	}
	
	/**
	 *  codifica l'ID di un entita' in base alla sua posizione
	 */
	
	public static String genID(Vector v) {
		
		return "x" + v.getBlockX() + "y" + v.getBlockY() + "z" + v.getBlockZ();
	}
	
	/**
	 *  Scrive o sovrascrive una zona si init.yml
	 */
	
	public static void writeZone(Zone zone) {
		
		// write zone to init.yml
			
		if (!plugin.getInit().contains("zones"))
			plugin.getInit().createSection("zones");
			
		if (plugin.getInit().contains("zones." + zone.name))	
			plugin.getInit().set("zones." + zone.name, null);	// se una zona con lo stesso nome esiste gia', essa viene sovrascritta
		else
			plugin.getInit().createSection("zones." + zone.name);
			
		plugin.getInit().createSection("zones." + zone.name + ".blocks");
		zone.writeBlocks();
		
		plugin.getInit().createSection("zones." + zone.name + ".spawns");
		zone.writeSpawns();
		
		plugin.getInit().createSection("zones." + zone.name + ".chests");
		zone.writeChests();
		
		plugin.getInit().createSection("zones." + zone.name + ".furthest");
		zone.writeFurthest();
	}
	
	/**
	 *  Carica una zona da init.yml
	 */
	
	public static Zone loadZone(String name) {
		
		Zone out = new Zone(name);
		
		out.loadBlocks();
		out.loadChests();
		out.loadSpawns();
		out.loadFurthest();
		
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
		
		public Spawn(String name, Vector location) {
			
			this.name = name;
			this.location = location;
		}
	}
	
	public class DChest {
		
		public Chest chest;				// BlockState della chest
		public Inventory inventory;		// Inventario della chest
		public Vector location;			// Posizione relativa in una zona
		
		public DChest(final Location absolute, final Location reference) {
			
			location = absolute.clone().subtract(reference).toVector();
			
			chest = null;
			inventory = null;
		}
		
		public DChest(Vector relative) {
			
			location = relative;
			
			chest = null;
			inventory = null;
		}
		
		/**
		 *  Genera la chest nel campo rispetto alla posizione della zona
		 */
		
		public void generate(final Location position, InventoryNode inv) {
			
			Block bk = position.clone().add(location).getBlock();
			
			bk.setType(Material.CHEST);
			
			chest = (Chest) bk.getState();
			inventory = chest.getBlockInventory();
			
			if (inv == null)
				return;
			
			this.replaceInventory(inv);
		}
		
		/**
		 *  Volatilizza la chest
		 */
		
		public void destroy(final Location position) {
			
			position.clone().add(location).getBlock().setType(Material.AIR);
			chest = null;
			inventory = null;
		}
		
		/**
		 *  Sostituisce l'inventario con un altro
		 */
		
		public void replaceInventory(InventoryNode inv) {
			
			inventory.setContents(inv.stack);
		}
		
		/**
		 *  restuisce true se la chest esiste nel campo
		 */
		
		public boolean isGenerated() {
			
			return inventory != null;
		}
		
		public String getID() {
			
			return genID(location);
		}
	}
	
	public static class InventoryNode {
		
		public ItemStack[] stack;
		public String name;
		
		public InventoryNode(ItemStack[] stack, String name) {
			
			this.stack = stack;
			this.name = name;
		}
		
		public InventoryNode(List<ItemStack> stack, String name) {
			
			this.stack = stack.toArray(new ItemStack[InventoryType.CHEST.getDefaultSize()]);
			this.name = name;
		}
		
		public List<ItemStack> getStackList() {
			
			return Arrays.asList(stack);
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
		
		/**
		 *  Genera il blocco nel campo
		 */
		
		public Block spawnBlock(final Location reference) {
			
			Location absolute = reference.clone().add(relative);
			Block out = absolute.getBlock();
			out.getState().setData(blockdata);
			
			return out;
		}
		
		/**
		 *  Volatilizza il blocco nel campo
		 */
		
		public void destroyBlock(final Location reference) {
			
			Location absolute = reference.clone().add(relative);
			absolute.getBlock().setType(Material.AIR);
		}
		
		/**
		 *  Restituisce un numero di 8bit significativo del blocco
		 */
		
		@SuppressWarnings("deprecation")
		public byte getData() {
			
			return blockdata.getData();
		}
		
		/**
		 *  Restituisce il materiale del blocco
		 */
		
		public Material getType() {
			
			return blockdata.getItemType();
		}
		
		/**
		 *  Returns the String id, based on the location
		 */
		
		public String getID() {
			
			return "x" + relative.getBlockX() + "y" + relative.getBlockY() + "z" + relative.getBlockZ();
		}
	}
}
