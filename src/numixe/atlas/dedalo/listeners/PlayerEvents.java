package numixe.atlas.dedalo.listeners;

import numixe.atlas.dedalo.entities.DPlayer;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		
        Entity entity = event.getEntity();
        
        if (!(entity instanceof Player))
        	return;
        	
        DPlayer dp = game.lobby.getPlayer((Player) entity);
            
            
        double damage = event.getDamage();
        double pHealth = dp.player.getHealth();
        
        switch (event.getCause()) {
        
        case ENTITY_ATTACK:
        	
        	// attacco corpo a corpo
        	
        	break;
        	
        case PROJECTILE:
        	
        	// proiettile
        	
        	break;
        	
        default:
        	break;
        	
        }
            
            
        if (pHealth - damage <= 0) {
            	
            event.setCancelled(true);	// skip spawn prompt 
            dp.onDeath();
        }
    }
}
