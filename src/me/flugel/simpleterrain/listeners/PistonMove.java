package me.flugel.simpleterrain.listeners;

import me.flugel.simpleterrain.Main;
import me.flugel.simpleterrain.objects.Terrenos;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class PistonMove implements Listener {
    @EventHandler
    public void piston(final BlockPistonExtendEvent e){
        Location location = e.getBlock().getLocation();
        Terrenos terrenoChunk = Main.getInstance().getManager().getByTerreno(location.getChunk());
        if(terrenoChunk == null){
            e.setCancelled(true);
        }
    }
}
