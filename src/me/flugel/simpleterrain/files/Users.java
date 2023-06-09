package me.flugel.simpleterrain.files;

import com.mojang.authlib.yggdrasil.response.User;
import me.flugel.simpleterrain.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Users {
    private File users;
    private FileConfiguration fileConfiguration;

    public Users() {
        users = new File(Main.getInstance().getDataFolder(), "Users.yml");
        fileConfiguration = YamlConfiguration.loadConfiguration(users);
        if (!(users.exists())) {
            try {
                users.createNewFile();
                Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §eArquivo Users.yml foi criado com sucesso");
                getConfig().createSection("Users");
                saveConfig();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §cOuve um erro ao criar o arquivo Users.yml");
            }
        } else {
            Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §eArquivo Users.yml foi recarregada com sucesso.");
        }
    }

    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(users);
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §cOuve um erro ao salvar o arquivo Users.yml");
        }
    }

    public void loadConfiguration() {
        saveConfig();
    }
}
