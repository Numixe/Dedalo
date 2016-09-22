package numixe.atlas.dedalo.entities;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;

public class Zone {
	
	List<Block> blocks;
	String name;

	public Zone(String name) {
		
		blocks = new ArrayList<Block>();
		this.name = name;
	}
	
	public static void writeZone(Zone zone) {
		
		// write zone to init.yml
	}
	
	public static Zone loadZone(String name) {
		
		Zone out = new Zone(name);
		
		//load zone from init.yml
		
		return out;
	}
}
