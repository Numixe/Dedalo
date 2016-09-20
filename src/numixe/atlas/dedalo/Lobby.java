package numixe.atlas.dedalo;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class Lobby implements Listener {

	public List<Player> blue, red;
	public static final int MAX_LOBBY = 16;
	
	public Lobby() {
		
		blue = new ArrayList<Player>();
		red = new ArrayList<Player>();
	}
	
	public void addToBlue(Player p) {
		
		if (blue.size() >= MAX_LOBBY)
			return;
		
		blue.add(p);
	}
	
	public void addToRed(Player p) {
		
		if (red.size() >= MAX_LOBBY)
			return;
		
		red.add(p);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		
		Player p = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if (block.getType() == Material.SIGN) {
			
			// verifica il testo
		}
	}
}
