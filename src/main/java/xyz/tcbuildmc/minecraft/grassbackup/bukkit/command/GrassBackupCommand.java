package xyz.tcbuildmc.minecraft.grassbackup.bukkit.command;

import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.GrassBackupBukkitPlugin;
import xyz.tcbuildmc.minecraft.grassbackup.bukkit.util.BackupUtils;

public class GrassBackupCommand {
    public static void register() {
        new CommandTree("grassbackup")
                .withPermission("grassbackup.command")
                .withAliases("gb")
                .withHelp("GrassBackup main command", "The command to use GrassBackup Plugin.")
                .then(new LiteralArgument("reload")
                        .withPermission("grassbackup.command.reload")
                        .executes(GrassBackupCommand::reload))
                .then(new LiteralArgument("help")
                        .withPermission("grassbackup.command.help")
                        .executes(GrassBackupCommand::help))
                .then(new LiteralArgument("make")
                        .withPermission("grassbackup.command.make")
                        .executes(GrassBackupCommand::make))
                .register(GrassBackupBukkitPlugin.getInstance());
    }

    public static void reload(CommandSender commandSender, CommandArguments commandArguments) {
        GrassBackupBukkitPlugin.getInstance().onReload();
    }

    public static void help(CommandSender commandSender, CommandArguments commandArguments) {
        commandSender.sendMessage(GrassBackupBukkitPlugin.getInstance().translations.tr("command.help"));
    }

    public static void make(CommandSender commandSender, CommandArguments commandArguments) {
        if (commandSender instanceof Player || commandSender instanceof ConsoleCommandSender) {
            Bukkit.getScheduler().runTaskAsynchronously(GrassBackupBukkitPlugin.getInstance(), () ->
                    BackupUtils.makeBackup(commandSender));
        }
    }
}
