package me.flugel.simpleterrain.listeners;

import me.flugel.simpleterrain.Main;
import me.flugel.simpleterrain.objects.Terrenos;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;

public class Interact implements Listener {
    @EventHandler
    public void interagir(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {

            if (!e.getPlayer().getWorld().equals(Bukkit.getWorld("world"))) {
                return;
            }

            Player player = e.getPlayer();
            Chunk chunk = e.getClickedBlock().getChunk();
            Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(chunk);
            if (byTerreno != null) {

                if (player.hasPermission("*"))
                    return;

                if (!byTerreno.getOwner().equalsIgnoreCase(player.getName())) {
                    if (!byTerreno.getFriends().contains(player.getName())) {
                        e.setCancelled(true);
                        e.getPlayer().sendMessage("§cVocê não tem permissões para mexer nesse terreno.");
                    }
                }
            }
        }
    }

    /*
        Verifica qual entidade está batendo em outra entidade
     */
    @EventHandler
    public void damage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {

            if (!e.getDamager().getWorld().equals(Bukkit.getWorld("world"))) {
                return;
            }

            Player player = (Player) e.getDamager();


            Chunk chunk = e.getEntity().getLocation().getChunk();
            Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(chunk);
            if (byTerreno != null) {

                if (player.hasPermission("*"))
                    return;

                if (e.getEntity() instanceof Player) {
                    e.setCancelled(true);
                    player.sendMessage("§cNão ha combate dentro de terrenos.");
                    return;
                }

                if (!byTerreno.getOwner().equalsIgnoreCase(player.getName())) {
                    if (!byTerreno.getFriends().contains(player.getName())) {
                        e.setCancelled(true);
                        player.sendMessage("§cVocê não permissão para bater em entidades nesse terreno.");
                        return;
                    }
                }
            }

        }
        if(e.getDamager() instanceof Arrow){
            Arrow arrow = (Arrow) e.getDamager();
            Chunk chunk = arrow.getLocation().getChunk();
            System.out.println(arrow.getLocation());

            Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(chunk);

            if(byTerreno != null){
                ProjectileSource shooter = arrow.getShooter();
                if(shooter instanceof Player){
                    Player player = ((Player) shooter).getPlayer();
                    Entity entity = e.getEntity();
                    if(player.getName().equals(byTerreno.getOwner())){

                        if(entity instanceof Player)
                            e.setCancelled(true);

                    }else{
                        e.setCancelled(true);
                    }
                }
            }


        }
    }

    @EventHandler
    public void entityinteract(PlayerInteractAtEntityEvent e) {
        Player player = e.getPlayer();

        if (!player.getWorld().equals(Bukkit.getWorld("world"))) {
            return;
        }

        Chunk chunk = e.getRightClicked().getLocation().getChunk();

        Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(chunk);

        if (byTerreno != null) {

            if (player.hasPermission("*"))
                return;

            if (!byTerreno.getOwner().equals(player.getName())) {
                if (!byTerreno.getFriends().contains(player.getName())) {
                    if (e.getRightClicked() instanceof ArmorStand) {
                        e.setCancelled(true);
                        player.sendMessage("§cVocê não tem permissão para mexer no suporte de armaduras");
                    } else e.setCancelled(true);
                }
            }
        }
    }

    public String returnChunkString(Chunk chunk) {
        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());

        return x + ">" + z;
    }
}
