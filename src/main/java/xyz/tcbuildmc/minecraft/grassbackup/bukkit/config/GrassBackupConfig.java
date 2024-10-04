package xyz.tcbuildmc.minecraft.grassbackup.bukkit.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
public class GrassBackupConfig {
    public GrassBackupConfig() {}

    private String backupDir = "gbm-backups";
    private boolean backup_on_shutdown = true;
    private boolean turn_off_auto_save = true;

//    private String backupDirectory = "backups";
//    private String backupName = "backup-%d-%t";
//    private String backupExtension = ".zip";
//    private String backupFormat = "yyyy-MM-dd_HH-mm-ss";
}
