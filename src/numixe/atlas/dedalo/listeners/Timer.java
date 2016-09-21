package numixe.atlas.dedalo.listeners;

import org.bukkit.Bukkit;

public class Timer extends Thread {
	
	int seconds;
	String command, broadcast;
	
	public Timer(String command, String broadcast, int seconds) {
		
		this.seconds = seconds;
		this.command = command;
		this.broadcast = broadcast;
	}

	@Override
	public void run() {
		
		for (int i = seconds; i > 0; i--) {
			
			Bukkit.getServer().broadcastMessage(broadcast.replaceAll("&sec", String.valueOf(i)));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
	}

}
