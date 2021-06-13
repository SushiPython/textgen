package us.sushipython.textgen.events;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class Movement implements Listener {
    public static boolean mvmt = false;
    @EventHandler

    public void move(PlayerMoveEvent e) {
        if (mvmt) {
            e.setCancelled(true);
        }
    }
}
