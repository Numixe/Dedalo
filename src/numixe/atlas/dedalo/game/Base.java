package numixe.atlas.dedalo.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static numixe.atlas.dedalo.Dedalo.*;

public class Base implements Listener {
	
	public void spawn(Player p) {
		//if (Game.start) {
		if (game.lobby.ownedBy(p).equals(game.lobby.teams[0])) {
			// spawn base rossa
		} else if (game.lobby.ownedBy(p).equals(game.lobby.teams[1])) {
			// spawn base blu
		}
	// }
	}

}
