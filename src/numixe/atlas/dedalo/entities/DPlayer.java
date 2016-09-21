package numixe.atlas.dedalo.entities;

import org.bukkit.entity.Player;

public class DPlayer {
	
	public Player player;
	public int kills, deaths;

	public DPlayer(Player p) {
		
		player = p;
		kills = 0;
		deaths = 0;
	}
	
	public String getName() {
		
		return player.getName();
	}
}
