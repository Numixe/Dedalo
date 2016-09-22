package numixe.atlas.dedalo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static numixe.atlas.dedalo.Dedalo.*;

public class PlayerEvents implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player p = event.getPlayer();
		
		// controlla se si trova allo spawn
		// ho usato Block.equals(Block) per ignorare yaw e pitch
		
		boolean onSpawn = p.getLocation().getBlock().equals( game.field.spawnLocation(game.lobby.ownedBy(p)).getBlock() );
		
		game.lobby.getPlayer(p).setCharging(onSpawn);
	}
	
}
