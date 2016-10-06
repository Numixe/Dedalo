package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import static numixe.atlas.dedalo.Dedalo.*;

import numixe.atlas.dedalo.Lobby;
import numixe.atlas.dedalo.entities.Zone.*;

public class Field {
	
	private ZoneNode[][] map;	// mappa bidimensionale dell'intero campo, un insieme di spazi contenenti zone
	public HashMap<String, Vector2i> spawnCoords;		// coordinate delle zone contenenti gli spawn registrati per ciascun team
	private int node_size;
	
	/*
	 *  La ragione per cui map e' array ZoneNode[][] e non un ArrayList<List<ZoneNode>> e' che
	 *  un array e' fisso nella struttura (dimensioni) ma piu' efficiente nella modifica dei valori
	 *  Mentre un ArrayList e' piu' lento a modificare i valori ma rapido a modificare le dimensioni.
	 *  map viene caricato una sola volta e durante il corso del gioco la sua struttura non viene modificata,
	 *  di conseguenza e' piu' conveniente l'array
	 */
	
	public static final int DEFAULT_MIN_DISTANCE = 2;
	public static final int DEFAULT_CHEST_SIZE = 20;
	public static final int DEFAULT_MAX_CHEST_PER_ZONE = 6;
	public static final int DEFAULT_ZONE_CHANGE = 3;
	
	public int min_distance;
	public int chest_size;
	public int max_chest_per_zone;
	public int zone_change;

	public Field() {
		
		spawnCoords = new HashMap<String, Vector2i>();
		map = null;
		node_size = 0;
		
		min_distance = DEFAULT_MIN_DISTANCE;
		chest_size = DEFAULT_CHEST_SIZE;
		max_chest_per_zone = DEFAULT_MAX_CHEST_PER_ZONE;
		zone_change = DEFAULT_ZONE_CHANGE;
	}
	
	/**
	 * Imposta un valore in un nodo preciso della mappa
	 * @param indices
	 * @param position
	 * @param possibleZones
	 */
	
	public void mapSetTo(Vector2i indices, Location position, String ... possibleZones) {
		
		ZoneNode node;
		
		try {
			
			node = getNode(indices);
			
		} catch (ArrayIndexOutOfBoundsException e) {
			
			return;
		}
		
		node.position = position;
		node.possibleZones = possibleZones;
	}
	
	/**
	 * Aggiunge un nodo a un indice della mappa
	 * @param index
	 * @param position
	 * @param possibleZones
	 */
	
	public void mapAddTo(int index, Location position, String ... possibleZones) {
		
		if (index >= map.length)
			return;
		
		int last;
		
		if (map[index] == null)
			last = 0;
		else
			last = map[index].length;
		
		ZoneNode[] copy = new ZoneNode[last + 1];	// crea un buffer piu' grande di 1
		
		for (int i = 0; i < last; i++) {	// copia il contenuto di map al buffer
			
			copy[i] = map[index][i];
		}
		
		copy[last] = new ZoneNode();
		copy[last].position = position;
		copy[last].possibleZones = possibleZones;
		
		map[index] = copy;
	}
	
	/**
	 * Rimuove un nodo da un nodo preciso della mappa
	 * @param coords
	 */
	
	public void mapRemove(Vector2i coords) {
		
		if (coords.x >= map.length)
			return;
		else if (coords.y >= map[coords.x].length)
			return;
		
		ZoneNode[] copy = new ZoneNode[map[coords.x].length - 1];
		
		int j = 0;
		
		for (int i = 0; i < map[coords.x].length; i++) {
			
			if (i == coords.y)
				continue;
			
			copy[j] = map[coords.x][i];
			j++;
		}
		
		map[coords.x] = copy;
	}
	
	/**
	 * Aggiunge un indice alla mappa
	 */
	
	public void mapAddIndex() {
		
		ZoneNode[][] copy = new ZoneNode[map.length + 1][];
		
		for (int i = 0; i < map.length; i++)
			copy[i] = map[i];				// copia i pointer da un array all'altro
		
		copy[map.length] = null;	// assegna null al nuovo slot
		
		map = copy;		// assegna il nuovo array a map
	}
	
	/**
	 * Rimuove un indice dalla mappa
	 * @param index
	 */
	
	public void mapRemoveIndex(int index) {
		
		if (index >= map.length)
			return;
		
		ZoneNode[][] copy = new ZoneNode[map.length - 1][];
		
		int j = 0;
		
		for (int i = 0; i < map.length; i++) {
			
			if (i == index)
				continue;
			
			copy[j] = map[i];				// copia i pointer da un array all'altro
			j++;
		}
		
		map = copy;		// assegna il nuovo array a map
	}
	
	/**
	 * Restituisce un instanza della mappa
	 * @return un pointer alla mappa
	 */
	
	public final ZoneNode[][] getMap() {
		
		return map;
	}
	
	/**
	 *  Resituisce il nodo corrispondente alle coordinate 
	 */
	
	public ZoneNode getNode(Vector2i v) {
		
		return map[v.x][v.y];
	}
	
	/**
	 *  Restituisce la posizione reale dello spawn di un team
	 */
	
	public Location getCurrentSpawnLocation(Team team) {	// returns the current spawn location (!! it changes during the game !!)
		
		Vector2i coords = spawnCoords.get(team.name);
		return map[coords.x][coords.y].spawnLocation();
	}
	
	/**
	 *  Genera casualmente due coordinate int x, y
	 */
	
	private Vector2i randomZoneCoords() {
		
		Vector2i out = new Vector2i();
		
		for (int i = 0; i < map.length; i++) {
		
			out.x = game.random.nextInt(map.length);
		
			if (map[out.x].length != 0) {
				
				for (int j = 0; j < map[out.x].length; j++) {
					
					out.y = game.random.nextInt(map[out.x].length);
					
					if (getNode(out).zone != null)
						break;
				}
				
				break;
			}
		}
		
		return out;
	}
	
	/**
	 *  Modifica le posizioni degli spawn
	 */
	
	public void refreshSpawns() {
		
		if (map == null)
			return;
		
		Vector2i zc1 = randomZoneCoords();	// genera la prima zona
		Vector2i zc2 = null;
		
		for (int p = 0; p < node_size; p++) {
			
			zc2 = randomZoneCoords();	// genera la seconda zona
			
			if (zc1.distance(zc2) >= min_distance)	// controlla che le due zone siano sufficientemente lontane
				break;
			
			// se non esistono zone abbastanza lontane verra' presa l'ultima generata
		}
		
		Team[] teams = game.lobby.teams;
		
		for (Team i : teams) {
			
			if (spawnCoords.containsKey(i.name)) {		// rimuove i vecchi spawn
				
				Vector2i old = spawnCoords.get(i.name);
				getNode(old).deleteSpawn();
				spawnCoords.remove(i.name);
			}
		}
		
		ZoneNode firstZone = getNode(zc1);
		ZoneNode secondZone = getNode(zc2);
		
		// imposta i nuovi spawn casualmente
		
		firstZone.setRandomSpawn();	
		secondZone.setRandomSpawn();
		
		spawnCoords.put(teams[0].name, zc1);
		spawnCoords.put(teams[1].name, zc2);
	}
	
	/**
	 *  Cambia la configurazione di un certo numero di zone
	 */
	
	public void refreshZones() {
		
		List<Vector2i> changed = new ArrayList<Vector2i>();		// Array di zone gia' scelte per essere sostituite, all'inizio e' vuoto
		
		for (int count = 0; changed.size() < zone_change && count < node_size; count++) {
			
			Vector2i coords = randomZoneCoords();	// genera una zona
			
			if (changed.contains(coords))	// se e' gia stata scelta, tentane un'altra
				continue;
			
			getNode(coords).assignRandomZone();		// assegna una zona casuale (tra quelle della lista)
			getNode(coords).zone.spawnBlocks(getNode(coords).position);	// genera i blocchi nel campo
			
			changed.add(coords);	// aggiungi questa zona tra quelle gia' scelte, onde evitare ripetizioni
			
			/*
			 *  Controlla che i giocatori, durante lo switch di zona, 
			 *  non siano rimasti intrappolati in una parete
			 *  In caso contrario, li si trasporta in direzione del centro della zona 
			 *  finche' non sono al sicuro
			 */
			
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
	
	/**
	 *  Cambia la configurazione delle chest
	 */
	
	public void refreshChests() {
		
		List<Vector2i> changed = new ArrayList<Vector2i>();		// lista delle zone scelte da cambiare
		int csize = 0;											// numero di chest cambiate
		
		for (int count = 0; csize < chest_size && count < node_size; count++) {
			
			Vector2i coords = randomZoneCoords();	// genera casualmente una zona
			
			if (changed.contains(coords))			// verifica che non sia gia' stata scelta
				continue;
			
			int addsize = game.random.nextInt(max_chest_per_zone);	// chest da generare in quella zona
			csize += addsize;
			
			if (csize > chest_size)					// se il numero e' troppo grande, calibralo
				addsize = csize - chest_size;
			
			map[coords.x][coords.y].spawnRandomChests(addsize);		//	genera nel campo le chest
		}
	}
	
	/**
	 *  Riaggiorna l'intero campo
	 *  Questa funzione viene richiamata esternamente ogni periodo di tempo
	 *  Ovvero quando la configurazione del campo deve essere cambiata
	 */
	
	public void refreshAll() {
		
		// changes the configuration
		
		refreshZones();
		
		if (game.chestMode)
			refreshChests();
		
		refreshSpawns();
	}
	
	/**
	 *  Da chiamare esternamente quando comincia il gioco
	 *  Inizializza il campo da battaglia
	 */
	
	public boolean initialize() {
		
		// controlla se non ci sono variabili indefinite o nulle
		
		if (map == null)
			return false;
		
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
						
						// assegna una zona casuale ad ogni nodo
						
						j.assignRandomZone();
						j.spawnZone();
					}
				}
			}
		}
		
		// se il gioco e' in chestmode, genera le chest
		
		if (game.chestMode)
			refreshChests();
		
		// genera gli spawn
		
		refreshSpawns();
		
		// trasporta i player nel campo di battaglia ai loro relativi spawn
		
		for (Team i : game.lobby.teams) {
			
			for (DPlayer p : i.getPlayers())
				p.player.teleport(getCurrentSpawnLocation(i));
		}
		
		return true;
	}
	
	/**
	 *  Da chiamare esternamente quando termina il gioco
	 *  Finalizza il campo di battaglia e lo volatilizza
	 */
	
	public void destroy() {
		
		if (map == null)
			return;
		
		for (ZoneNode[] i : map) {
			
			for (ZoneNode j : i) {
				
				j.destroyZoneNode();
			}
		}
		
		spawnCoords.clear();
		
		for (Team i : game.lobby.teams) {
			
			for (DPlayer p : i.getPlayers())
				p.player.teleport(Lobby.location);	// trasporta tutti i player nella posizione nell'hub
		}
	}
	
	public void reload() {
		
		destroy();
		initialize();
	}
	
	/**
	 *  Carica la mappa
	 */
	
	public void loadMap() {
		
		Set<String> set = plugin.getInit().getConfigurationSection("map").getKeys(false);
		
		node_size = 0;
		
		map = new ZoneNode[set.size()][];
		
		int i = 0;
		
		for (String key : set) {
			
			Set<String> subset = plugin.getInit().getConfigurationSection("map." + key).getKeys(false);
			
			map[i] = new ZoneNode[subset.size()];
			
			int j = 0;
			
			for (String subkey : subset) {
				
				map[i][j] = new ZoneNode();
				Vector loc = plugin.getInit().getVector("map." + key + "." + subkey + ".position");
				World w = Bukkit.getServer().getWorld(plugin.getInit().getString("map." + key + "." + subkey + ".world"));
				List<String> psz = plugin.getInit().getStringList("map." + key + "." + subkey + ".possibleZones");
				map[i][j].set(loc,  w, psz);
				
				j++;
			}
					
			i++;
			node_size += subset.size();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	
	public static void writeField(Field field) {
		
		plugin.getInit().set("map", null);
		ZoneNode[][] map = field.getMap();
		
		for (int i = 0; i < map.length; i++) {
			
			plugin.getInit().createSection("map." + i);
			
			for (int j = 0; j < map[i].length; j++) {
				
				plugin.getInit().createSection("map." + i + "." + j);
				
				plugin.getInit().createSection("map." + i + "." + j + ".position");
				plugin.getInit().createSection("map." + i + "." + j + ".world");
				plugin.getInit().createSection("map." + i + "." + j + ".possibleZones");
				
				plugin.getInit().set("map." + i + "." + j + ".position", map[i][j].position.toVector());
				plugin.getInit().set("map." + i + "." + j + ".world", map[i][j].position.getWorld().getName());
				plugin.getInit().set("map." + i + "." + j + ".possibleZones", Arrays.asList(map[i][j].possibleZones));
			}
		}
	}
	
	/**
	 *  Carica le informazioni strutturali del campo da init.yml
	 */
	
	public static Field loadField() {
		
		Field out = new Field();
		out.loadMap();
		
		return out;
	}
	
	public class Vector2i {		// struttura che velocizza il calcolo vettoriale int a due dimensioni
		
		public int x;
		public int y;
		
		public double distance(Vector2i v) {
			
			int x = this.x - v.x;
			int y = this.y - v.y;
			
			return Math.sqrt(x * x + y * y);
		}
	}
	
	public class ZoneNode {
		
		public Location position;		// posizione reale del nodo
		public Zone zone;				// la zona scelta in questione
		public String[] possibleZones;	// i nomi delle possibili zone in questo nodo
		public int[] chestsIndices;		// Gli indici delle chest, nell'array zone.chets
		public Spawn spawn;			// un possibile spawn, nullo se non e' presente
		
		public void set(Vector vec, World w, List<String> psz) {
			
			position = vec.toLocation(w);
			possibleZones = psz.toArray(new String[psz.size()]);
		}
		
		/*
		 *  Seleziona casualmente una tra le zone possibili
		 */
		
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
		
		/*
		 *  Elimina le vecchie chest e ne genera altre
		 */
		
		public void spawnRandomChests(int size) {
			
			if (zone == null)
				return;
			
			destroyChests();
			
			chestsIndices = new int[size];
			
			for (int i = 0; i < size; i++) {
				
				chestsIndices[i] = game.random.nextInt(zone.chestsSize());	// random chest location
				int inv = game.random.nextInt(Zone.INVENTORIES.size());	// random chest inventory
				
				zone.spawnChest(position, inv, chestsIndices[i]);
			}
		}
		
		/*
		 *  Genera la zona nel campo
		 */
		
		public void spawnZone() {
			
			zone.spawnBlocks(position);
		}
		
		/*
		 *  Volatilizza tutto cio' appartenente alla zona
		 */
		
		public void destroyZoneNode() {
			
			if (zone == null)
				return;
			
			zone.destroyBlocks(position);
			
			destroyChests();
		}
		
		/*
		 *  Volatilizza le chest
		 */
		
		public void destroyChests() {
			
			if (chestsIndices == null)
				return;
			
			zone.destroyChests(position, chestsIndices);
			chestsIndices = null;
		}
		
		/*
		 * Restituisce una lista di player che si trova all'interno del nodo
		 */
		
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
		
		/*
		 *  Restituisce il centro del nodo
		 */
		
		public Location getCenter() {
			
			return zone.getCenter(position);
		}
		
		/*
		 *  Assegna uno spawn casuale, tra quelli della lista in zone
		 */
		
		public void setRandomSpawn() {
			
			spawn = zone.randomSpawn();
		}
		
		/*
		 *  Annulla uno spawn assegnato
		 */
		
		public void deleteSpawn() {
			
			spawn = null;
		}
		
		/*
		 *  Restituisce la posizione del proprio spawn
		 */
		
		public Location spawnLocation() {
			
			return position.clone().add(spawn.location);
		}
	}
}
