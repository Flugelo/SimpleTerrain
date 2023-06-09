package me.flugel.simpleterrain.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;

import java.util.List;

public class TntExplosed implements Listener {
    @EventHandler
    public void tnt(EntityExplodeEvent e){
       if(e.getEntity().getWorld().equals(Bukkit.getWorld("world")))
           e.setCancelled(true);
    }
}
