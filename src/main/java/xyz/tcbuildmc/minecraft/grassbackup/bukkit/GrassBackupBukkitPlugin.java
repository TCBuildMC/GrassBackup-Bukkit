package xyz.tcbuildmc.minecraft.grassbackup.bukkit;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.tcbuildmc.common.powerfullib.i18n.v0.Translations;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.command.GrassBackupCommand;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.config.GrassBackupConfig;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import static xyz.tcbuildmc.minecraft.grassbackup.bukkit.util.Constants.*;

public class GrassBackupBukkitPlugin extends JavaPlugin {
    public Logger logger;
    public File dataDir;
    public File configFile;
    public Translations translations;
    public GrassBackupConfig config;
    private static GrassBackupBukkitPlugin INSTANCE;

    @Override
    public void onLoad() {
        this.logger = getLogger();
        this.logger.info("Initializing...");

        this.dataDir = getDataFolder();
        try {
            this.translations = Translations.loadFromClasspathFile(CONFIG_API, "json");
        } catch (IOException e) {
            this.logger.severe("Failed to get Translations." + e);
            this.logger.severe("Disabling the plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        this.configFile = new File(this.dataDir, CONFIG_FILE);
        if (!this.configFile.exists()) {
            CONFIG_API.write(new GrassBackupConfig(), this.configFile);
            this.config = new GrassBackupConfig();
        } else {
            this.config = CONFIG_API.read(GrassBackupConfig.class, this.configFile);
        }

        CommandAPI.onLoad(new CommandAPIBukkitConfig(this));
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        GrassBackupCommand.register();
    }

    @Override
    public void onDisable() {
        this.logger.info("Shutting down...");
        CONFIG_API.write(this.config, this.configFile);
    }

    public void onReload() {
        this.logger.info("Reloading...");

        CONFIG_API.write(this.config, this.configFile);
        this.config = CONFIG_API.read(GrassBackupConfig.class, this.configFile);
    }

    public static GrassBackupBukkitPlugin getInstance() {
        return INSTANCE;
    }
}
