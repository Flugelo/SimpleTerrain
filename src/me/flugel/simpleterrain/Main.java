package me.flugel.simpleterrain;

import me.flugel.simpleterrain.commands.command;
import me.flugel.simpleterrain.files.Users;
import me.flugel.simpleterrain.listeners.Interact;
import me.flugel.simpleterrain.listeners.PistonMove;
import me.flugel.simpleterrain.listeners.TntExplosed;
import me.flugel.simpleterrain.managers.Manager;
import me.flugel.simpleterrain.objects.Terrenos;
import me.flugel.simpleterrain.schematic.AutoSave;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Main extends JavaPlugin {
    private static Main instance;
    private Manager manager;
    private Users users;


    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        manager = new Manager();
        users = new Users();
        onCommands();
        getUserConfigToPOO();

        new AutoSave().runTaskTimerAsynchronously(this, 0, 20);
        Bukkit.getPluginManager().registerEvents(new Interact(), this);
        Bukkit.getPluginManager().registerEvents(new TntExplosed(), this);
        Bukkit.getPluginManager().registerEvents(new PistonMove(), this);

    }

    @Override
    public void onDisable() {
        saveAllUsersreload();
    }

    public void onCommands() {
        getCommand("terreno").setExecutor(new command());
        getCommand("claim").setExecutor(new command());
        getCommand("unclaim").setExecutor(new command());
        getCommand("trust").setExecutor(new command());
        getCommand("untrust").setExecutor(new command());
        getCommand("info").setExecutor(new command());
        getCommand("chunk").setExecutor(new command());
        getCommand("chunkall").setExecutor(new command());
        getCommand("infoall").setExecutor(new command());
        getCommand("unclaimall").setExecutor(new command());

    }

    public static Main getInstance() {
        return instance;
    }

    public Manager getManager() {
        return manager;
    }

    public Users getUsers() {
        return users;
    }

    public void saveAllUsersAsync() {
        Bukkit.getScheduler().runTaskAsynchronously(this, () -> {
            FileConfiguration config = getUsers().getConfig();
            ArrayList<Terrenos> terrenos = getManager().getTerrenos();
            for (Terrenos terreno : terrenos) {
                List<String> friends = terreno.getFriends();
                ArrayList<String> chunkList = terreno.getChunksString();
                String dono = terreno.getOwner();
                int quatia = terreno.getAmount();

                ConfigurationSection section = config.createSection("Users." + dono);

                section.set("owner", dono);

                section.set("amount", quatia);

                section.set("friends", friends);

                section.set("chunks", chunkList);
            }

            getUsers().saveConfig();
        });
        getUsers().saveConfig();
        Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §2Chache foi salva com sucesso no arquivo Users.yml");
    }

    public void saveAllUsersreload() {
        FileConfiguration config = getUsers().getConfig();
        ArrayList<Terrenos> terrenos = getManager().getTerrenos();
        for (Terrenos terreno : terrenos) {
            List<String> friends = terreno.getFriends();
            ArrayList<String> chunk = terreno.getChunksString();
            String dono = terreno.getOwner();
            int quantia = terreno.getAmount();

            ConfigurationSection section = config.createSection("Users." + dono);

            section.set("owner", dono);
            section.set("amount", quantia);

            section.set("friends", friends);

            section.set("chunks", chunk);
        }

        getUsers().saveConfig();
        Bukkit.getConsoleSender().sendMessage("§aSimpleTerrainF: §2Chache foi salva com sucesso no arquivo Users.yml");
    }

    public void getUserConfigToPOO() {
        Bukkit.getScheduler().runTaskAsynchronously(this,() ->{
            FileConfiguration config = getUsers().getConfig();
            for (String key : config.getConfigurationSection("Users").getKeys(false)) {
                ConfigurationSection section = config.getConfigurationSection("Users." + key);
                String owner = section.getString("owner");
                int quantia = section.getInt("amount");
                List<String> friends1 = section.getStringList("friends");
                List<String> chunks = section.getStringList("chunks");

                ArrayList<String> chunkString = new ArrayList<>(chunks);

//                for (String s : chunkString) {
//                    String[] args = s.split(">");
//                    int x = Integer.parseInt(args[0]);
//                    int z = Integer.parseInt(args[1]);
//
//                    Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);
//                    World world = Bukkit.getWorld("world");
//
//                    Bukkit.getScheduler().runTask(this, () -> unLoadChunks(world, chunk));
//
//                }

                ArrayList<String> amigos = new ArrayList<>(friends1);

                Terrenos terrenos = new Terrenos(owner, amigos, chunkString);
                terrenos.setAmount(quantia);

            }
        });

        Bukkit.getConsoleSender().sendMessage("§aTerrenos: §eAs informações do arquivo Users.yml foi carregada com sucesso!");

    }

    public void removeUserToConfig(Player player) {
        FileConfiguration config = getUsers().getConfig();
        for (String key : config.getConfigurationSection("Users").getKeys(false)) {
            if (key.equals(player.getName())) {
                config.set("Users." + player.getName(), null);
                getUsers().saveConfig();
                return;
            }
        }

    }
    public void unLoadChunks(World world, Chunk chunk){
        net.minecraft.world.level.World nmsWorld = ((CraftWorld) world).getHandle();
        int x = chunk.getX();
        int z = chunk.getZ();

        net.minecraft.world.level.chunk.Chunk chunkAt = nmsWorld.getChunkAt(x, z);
        chunkAt.unloadCallback();
    }

}
