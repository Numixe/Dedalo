package numixe.atlas.dedalo.game;

import numixe.atlas.dedalo.entities.DPlayer;
import numixe.atlas.dedalo.entities.Team;
import static numixe.atlas.dedalo.Dedalo.game;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Reload implements Listener {
	
	public static final ItemStack RedReload =  genBlock("red");
	public static final ItemStack BlueReload = genBlock("blue");
	
	/**
	 * Ho gia' gestito questo evento tramite la classe DPlayer, PlayerEvents e ChargeEvent
	 * Quando il giocatore si piazza sul blocco di spawn, la barra dell'exp, che rappresenta la carica,
	 * inizia automaticamente a caricarsi finchè il giocatore non esce dal blocco o la barra non e' totalmente
	 * carica
	 */
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {	
		
		DPlayer p = game.lobby.getPlayer(e.getPlayer());
		Team team = game.lobby.ownedBy(e.getPlayer());
		
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			
			Block block = e.getClickedBlock();
			
			if (block.getDrops().toArray()[0].equals(RedReload)) {
				
				if (team.id == 0)
					p.chargeUp();
				
			} else if (block.getDrops().toArray()[0].equals(BlueReload)) {
				
				if (team.id == 1)
					p.chargeUp();
			}
		}
	}
	
	public static final ItemStack genBlock(String name) {
		
		ItemStack genBlock = null;
		
		if (name.equalsIgnoreCase("red")) {
			
			genBlock = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
			
		} else if (name.equalsIgnoreCase("blue")) {
			
			genBlock = new ItemStack(Material.HARD_CLAY, 1, (short) 11);
		}
		
		return genBlock;
	}
}
