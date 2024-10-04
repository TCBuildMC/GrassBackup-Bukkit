package xyz.tcbuildmc.minecraft.grassbackup.bukkit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import xyz.tcbuildmc.common.powerfullib.config.v0.api.IConfigApi;
import xyz.tcbuildmc.common.powerfullib.config.v0.impl.GsonConfigApi;

public class Constants {
    public static final Gson GSON = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
    public static final IConfigApi CONFIG_API = GsonConfigApi.create(Constants.GSON);
    public static final String CONFIG_FILE = "config.json";
}
