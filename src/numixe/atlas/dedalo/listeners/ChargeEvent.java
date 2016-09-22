package numixe.atlas.dedalo.listeners;

import static numixe.atlas.dedalo.Dedalo.plugin;

import numixe.atlas.dedalo.entities.DPlayer;

import org.bukkit.Bukkit;

public class ChargeEvent implements Runnable {
	
	public static final int SEC = 20;
	int id = 0;
	public DPlayer player;
	
	public ChargeEvent(DPlayer player, float unit) {	// unit in seconds
		
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, (int)(unit * SEC));
		this.player = player;
	}
	
	public synchronized void destroy() {
		
		Bukkit.getServer().getScheduler().cancelTask(id);
	}

	@Override
	public void run() {
		
		player.chargeUp();	// charge up every second
	}

}
