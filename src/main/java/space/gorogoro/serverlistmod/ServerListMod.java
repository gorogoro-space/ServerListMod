package space.gorogoro.serverlistmod;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import net.kyori.adventure.text.Component;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ServerListMod extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("The Plugin Has Been Enabled!");

        // Create the configuration file if it does not exist.
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
        }

        // Register the event listener.
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("[ServerListMod] It started up successfully.");
    }

    // Use PaperServerListPingEvent and Adventure Components to avoid deprecation warnings.
    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        String versionText = getConfig().getString("version.text", "");
        String motdLine1 = getConfig().getString("motd.line1", "");
        String motdLine2 = getConfig().getString("motd.line2", "");

        // Combine the lines with a newline character.
        String combinedMotd = motdLine1 + "\n" + motdLine2;

        // Convert the String to an Adventure Component to satisfy the modern Paper API.
        event.motd(Component.text(combinedMotd));

        // Set the version text.
        event.setVersion(versionText);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("slm")) {
            // Check if the sender has OP privileges.
            if (sender.isOp()) {
                // Verify the array index to prevent exceptions.
                if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                    reloadConfig();
                    sender.sendMessage("[ServerListMod] Successfully reloaded config.yml.");
                    return true;
                }
                sender.sendMessage("[ServerListMod] Usage: /slm reload");
                return true;
            } else {
                sender.sendMessage("You do not have permission (OP) to execute this command.");
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        getLogger().info("The Plugin Has Been Disabled!");
    }
}
