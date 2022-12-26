package me.paint;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;

import su.nightexpress.excellentcrates.api.event.CrateEvent;
import su.nightexpress.excellentcrates.opening.PlayerOpeningData;

import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CrateOpenListener implements Listener {
  CrateCloseOnEnd app;

  public CrateOpenListener(CrateCloseOnEnd app) {
    this.app = app;
  }

  @EventHandler
  public void onCrate(CrateEvent e) {

    Player p = e.getPlayer();

    if (p == null) {
      return;
    }
    if (e.getCrate() == null) {
      return;
    }

    Bukkit.getScheduler().runTaskLaterAsynchronously(app, (() -> {
      long startTime = new Date().getTime();
      final long threadMaxLife = 100000L;

      PlayerOpeningData data = PlayerOpeningData.get(p);

      if (data == null) {
        return;
      }

      boolean isThreadMaxTimeReached = false;

      if (!data.isCompleted()) {

        while (!data.isCompleted() && !isThreadMaxTimeReached) {
          isThreadMaxTimeReached = (new Date().getTime() - startTime) > threadMaxLife;

          try {
            Thread.sleep(25);
          } catch (InterruptedException e1) {
            e1.printStackTrace();
          }
        }

        if (isThreadMaxTimeReached) {
          return;
        }

        Bukkit.getScheduler().runTaskLater(app, (() -> {
          InventoryView v = p.getOpenInventory();
          if (v != null) {
            v.close();
          }
        }), 40); // Run 40 ticks later 2 seconds closes menu
      }

      return;
    }), 2); // Run 2 tick later wait for data creation
  }
}
