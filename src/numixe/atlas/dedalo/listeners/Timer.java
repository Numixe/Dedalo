package numixe.atlas.dedalo.listeners;

import org.bukkit.Bukkit;

import static numixe.atlas.dedalo.Dedalo.*;

public class Timer implements Runnable {
	
	int seconds;
	String command, broadcast;
	final int id;
	private volatile boolean sigint = false;
	
	public Timer(String command, String broadcast, int seconds) {
		
		this.seconds = seconds;
		this.command = command;
		this.broadcast = broadcast;
		id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, this, 0);
	}
	
	public synchronized void interrupt() {
		
		sigint = true;
	}

	@Override
	public void run() {
		
		for (int i = seconds; i > 0; i--) {
			
			Bukkit.getServer().broadcastMessage(broadcast.replaceAll("&sec", String.valueOf(i)));
			
			if (sigint) {
				
				Bukkit.getServer().broadcastMessage("Timer interrotto");
				Bukkit.getServer().getScheduler().cancelTask(id);
				return;
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
		Bukkit.getServer().getScheduler().cancelTask(id);
	}

}
