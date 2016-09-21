package numixe.atlas.dedalo.listeners;

import static numixe.atlas.dedalo.Dedalo.*;

import org.bukkit.Bukkit;

public class TimeEvent implements Runnable {
	
	public static final int MAX_ITER = 5;	// stabilisce il tempo di gioco
	public static final int SEC = 20;
	int iter = 0;
	final int id;
	
	public TimeEvent(int range) {
		
		id = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 0, range * SEC);
	}
	
	public synchronized void destroy() {
		
		Bukkit.getServer().getScheduler().cancelTask(id);
	}

	@Override
	public void run() {
		
		// handle game events here
		iter++;
		
		if (iter >= MAX_ITER) {
			
			game.finish();	// end game
			return;
		}
	}

}
