package us.sushipython.textgen;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class TextGen extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        System.out.println("Plugin Starting");
        new Test(this);
    }
}