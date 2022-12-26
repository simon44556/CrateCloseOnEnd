package me.paint;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitWorker;

public class CrateCloseOnEnd extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new CrateOpenListener(this), this);
    }

    @Override
    public void onDisable() {

        List<BukkitWorker> workers = Bukkit.getScheduler().getActiveWorkers();
        for (BukkitWorker w : workers) {
            if (w.getOwner() instanceof CrateCloseOnEnd && w.getThread().isAlive()) {
                w.getThread().interrupt();
            }
        }
    }

}
