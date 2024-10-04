package xyz.tcbuildmc.minecraft.grassbackup.bukkit.util;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.GrassBackupBukkitPlugin;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.config.GrassBackupConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class BackupUtils {
    public static final AtomicBoolean PROCESSING = new AtomicBoolean(false);

    // Async
    public static void makeBackup(CommandSender commandSender) {
        if (PROCESSING.get() || !GrassBackupBukkitPlugin.getInstance().isEnabled()) {
            return;
        }

        PROCESSING.set(true);

        GrassBackupConfig config = GrassBackupBukkitPlugin.getInstance().config;
        Server server = commandSender.getServer();

        // Prepare Backup dir
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

        File backupDirRoot = new File(config.getBackupDir());
        if (!backupDirRoot.exists()) {
            backupDirRoot.mkdirs();
        }

        File targetBackupDir = backupDirRoot.toPath().resolve(dateFormat.format(new Date())).toFile();

        // Save players
        Bukkit.getScheduler().runTask(GrassBackupBukkitPlugin.getInstance(), server::savePlayers);

        // Save worlds
        for (World world : server.getWorlds()) {
            AtomicBoolean saved = new AtomicBoolean(false);

            Bukkit.getScheduler().runTask(GrassBackupBukkitPlugin.getInstance(), () -> {
                if (config.isTurn_off_auto_save()) {
                    world.setAutoSave(false);
                }

                world.save();
                saved.set(true);
            });

            try {
                if (!saved.get()) {
                    Thread.sleep(3000);
                }

                File worldDir = world.getWorldFolder().getAbsoluteFile();
                FileUtils.copyDirectoryToDirectory(worldDir, targetBackupDir);
            } catch (Exception e) {
                GrassBackupBukkitPlugin.getInstance().logger.severe("Failed to backup world " + world.getName() + " " + e);
            }

            Bukkit.getScheduler().runTask(GrassBackupBukkitPlugin.getInstance(), () -> world.setAutoSave(true));
        }

        PROCESSING.set(false);
    }
}
