package numixe.atlas.dedalo.entities;

import static numixe.atlas.dedalo.Dedalo.game;

import numixe.atlas.dedalo.listeners.ChargeEvent;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class DPlayer {
	
	public static final int MAX_CHARGE = 99;	// leggermente inferiore al 100% per evitare che salga di livello
	public static final int CHARGE_PER_UNIT = 10;
	public static final String MAX_CHARGE_MSG = "�9Dedalo> �7Ricarica completata!";
	public static final String CHARGE_MSG = "�9Dedalo> �7Ricarica...!";
	
	public Player player;
	public int kills, deaths;
	private int charge;			// sistema di ricarica ancora da decidere
	private boolean charging;
	private ChargeEvent chargeEvent;
	
	/*
	 * 	Suppongo che la carica dipenda dal giocatore
	 * 	e che usando l'arma si scarichi ad ogni colpo
	 * 	chiamando il metodo chargeDown()
	 */
	
	/*
	 * Per ricaricare e' sufficiente piazzarsi sul blocco di spawn
	 * L'evento e' gestito dalla classe PlayerEvents
	 * Il loop di ricarica e' invece gestito da ChargeEvent
	 */

	public DPlayer(Player p) {
		
		player = p;
		kills = 0;
		deaths = 0;
		charge = MAX_CHARGE;
		charging = false;
		chargeEvent = null;
		
		/*
		 * 	!!! Prima di modificare l'esperienza è meglio salvarla prima in uno .yml !!!
		 */
		
		player.setLevel(0);
		player.setExp(0.99f);
	}
	
	public String getName() {
		
		return player.getName();
	}
	
	public void chargeUp() {
		
		if (charge == MAX_CHARGE) {
			
			player.sendMessage(MAX_CHARGE_MSG);
			return;
		}
		
		charge += CHARGE_PER_UNIT;
		
		if (charge > MAX_CHARGE)
			charge = MAX_CHARGE;
		
		player.setExp((float)charge / 100);
		player.sendMessage(CHARGE_MSG);
	}
	
	public void chargeDown(int amount) {
		
		charge -= amount;
		
		if (charge < 0)
			charge = 0;
		
		player.setExp((float)charge / 100);
	}
	
	public void setCharging(boolean value) {
		
		if (charging == value)
			return;
			
		if (value) {
			
			chargeEvent = new ChargeEvent(this, 0.5f);	// inizia a caricare ogni mezzo secondo
			
		} else {
			
			chargeEvent.destroy();
			chargeEvent = null;
		}
		
		charging = value;
	}
	
	public int getCharge() {
		
		return charge;
	}
	
	public boolean isCharging() {
		
		return charging;
	}
	
	public void spawn() {
		
		player.teleport(game.field.spawnLocation(game.lobby.ownedBy(player)));
	}
	
	public boolean isOnSpawn() {
		
		Location pl_loc = player.getLocation();
		Location spawn_loc = game.field.spawnLocation(game.lobby.ownedBy(player));
		
		if (pl_loc.getBlockX() != spawn_loc.getBlockX())	// compare int to int
			return false;
		
		if (pl_loc.getBlockY() != spawn_loc.getBlockY())
			return false;
		
		if (pl_loc.getBlockZ() != spawn_loc.getBlockZ())
			return false;
		
		return true;
	}
	
	public void onDeath() {
		
		player.sendMessage("You Died!");
		
		spawn();
        player.getInventory().clear();
        player.setHealth(10);
        player.setFoodLevel(10);
	}
}
