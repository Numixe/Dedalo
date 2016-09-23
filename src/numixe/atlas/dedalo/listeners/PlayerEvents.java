package numixe.atlas.dedalo.listeners;

import numixe.atlas.dedalo.entities.DPlayer;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import static numixe.atlas.dedalo.Dedalo.*;

public class PlayerEvents implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player p = event.getPlayer();
		DPlayer dp = game.lobby.getPlayer(p);
		
		// controlla se si trova allo spawn
		
		try {
			
			dp.setCharging(dp.isOnSpawn());
			
		} catch (NullPointerException ex) {
			
			return;
		}
	}
	
}
