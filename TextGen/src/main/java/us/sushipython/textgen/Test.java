package us.sushipython.textgen;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import us.sushipython.textgen.events.EntityExplode;
import us.sushipython.textgen.events.Movement;
import us.sushipython.textgen.extras.ASCIIArtGenerator;

import javax.security.auth.login.LoginException;

public class Test extends ListenerAdapter implements Listener {
    public TextGen plugin;
    public JDA jda;
    public Player p;
    public Test(TextGen main) {
        this.plugin = main;
        startBot();
        jda.addEventListener(this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new EntityExplode(), this.plugin);
        Bukkit.getServer().getPluginManager().registerEvents(new Movement(), this.plugin);
    }
    public void run(String text, Material mat) {
        Bukkit.getScheduler().runTask(this.plugin, () -> {
            ASCIIArtGenerator artGen = new ASCIIArtGenerator();
            String output = null;
            try {
                output = artGen.printTextArt(text, ASCIIArtGenerator.ART_SIZE_SMALL).toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Location playerPos = this.p.getLocation();
            int playerXpos = (int) Math.round(playerPos.getX());
            int playerZpos = (int) Math.round(playerPos.getZ())-140;
            int up50 = (int) (Math.round(playerPos.getY()) + 30);
            assert output != null;
            String[] items = output.split("\\r?\\n");
            int textwidth = items.length;
            for (int i = 0; i < textwidth; i++) {
                String line = items[i];
                for (int b = 0; b < line.length(); b++) {
                    Location blockPos = new Location(playerPos.getWorld(), b+playerXpos-(line.length()/2), up50-i, playerZpos);
                    if (line.charAt(b) == '*') {
                        blockPos.getBlock().setType(mat);
                    } else {
                        blockPos.getBlock().setType(Material.AIR);
                    }
                }
            }
        });
    }
    private void startBot() {
        try {
            jda = JDABuilder.createDefault("").build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        this.p = Bukkit.getOnlinePlayers().stream().findFirst().get();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String msg = event.getMessage().getContentRaw();
        User user = event.getAuthor();
        String[] args = msg.split("\\s+");
        String type = args[0];
        if (type.equals("!explosion")) {
            for (int i = 0; i <= 24; i ++) {
                int finalI = i;
                Movement.mvmt = true;
                if (i == 24) {
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                        Location loc = p.getLocation();
                        loc.setYaw(finalI *15);
                        p.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                        Movement.mvmt = false;
                    }, i*5);
                } else {
                    Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                        Location loc = p.getLocation();
                        loc.setYaw(finalI *15);
                        p.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                    }, i*5);
                }
            }
        }
        if (type.equals("!normal")) {
            String material = args[1];
            args = (String[]) ArrayUtils.removeElement(args, args[0]);
            args = (String[]) ArrayUtils.removeElement(args, args[0]);
            String fixed = String.join(" ", args);
            run(user.getName() + ": " + fixed, Material.getMaterial(material));
        }
    }
}
