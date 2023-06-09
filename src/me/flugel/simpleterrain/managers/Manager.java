package me.flugel.simpleterrain.managers;

import me.flugel.simpleterrain.Main;
import me.flugel.simpleterrain.objects.Terrenos;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Manager {
    private ArrayList<Terrenos> terrenos;

    public Manager(){
        terrenos = new ArrayList<>();
    }

    public ArrayList<Terrenos> getTerrenos() {
        return terrenos;
    }

    public Terrenos getByTerreno(Chunk chunk){

        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());
        String chunkString = x + ">" + z;

        for (Terrenos terreno : this.terrenos) {
            for (String s : terreno.getChunksString()) {
                if(s.equalsIgnoreCase(chunkString)){
                    return terreno;
                }
            }
        }
        return null;

    }

    public Terrenos getByPlayer(String playerName){
        for (Terrenos terreno : this.terrenos) {
            if (terreno.getOwner().equalsIgnoreCase(playerName)) {
                return terreno;
            }
        }
        return null;
    }



    public void add(Terrenos terrenos){
        this.terrenos.add(terrenos);
    }

    public void remove(Terrenos terrenos){
        this.terrenos.remove(terrenos);
    }

}
