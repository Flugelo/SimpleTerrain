package me.flugel.simpleterrain.schematic;

import me.flugel.simpleterrain.Main;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {

    public static int tempo = 1800;
    @Override
    public void run() {
        if(tempo == 0){
            Bukkit.broadcastMessage("§cAviso: §eServidor esta salvando as informações no momento, isso pode gerar algum lag...");
            Main.getInstance().saveAllUsersAsync();
            Bukkit.broadcastMessage("§cAviso: §eInformações salva com sucesso...");
            tempo = 1800;
        }else tempo--;
    }
}
