package me.flugel.simpleterrain.commands;

import me.flugel.simpleterrain.Main;
import me.flugel.simpleterrain.objects.Terrenos;
import net.minecraft.world.level.TickList;
import net.minecraft.world.level.block.Block;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String lb, String[] args) {

        if (!(sender instanceof Player))
            return false;

        Player p = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("terreno")) {
            if (args.length == 0) {
                if (p.hasPermission("simpleterrain.staff")) {
                    p.sendMessage("");
                    p.sendMessage("       §eSimple Terrain Protection");
                    p.sendMessage("§f/Claim §ePara marca a area desejada.");
                    p.sendMessage("§f/terreno deletar §eDeleta o terreno de sua localização.");
                    p.sendMessage("§f/trust <Nick> §ePara adicionar uma pessoa em seu terreno.");
                    p.sendMessage("§f/untrust <Nick> §ePara remover uma pessoa de seu terreno.");
                    p.sendMessage("§f/unClaim §ePara deletar o terreno que você esta no local.");
                    return true;
                } else {
                    p.sendMessage("");
                    p.sendMessage("       §eSimple Terrain Protection");
                    p.sendMessage("§f/Claim §ePara marca a area desejada automatica.");
                    p.sendMessage("§f/trust <Nick> §ePara adicionar uma pessoa em seu terreno.");
                    p.sendMessage("§f/untrust <Nick> §ePara remover uma pessoa de seu terreno.");
                    p.sendMessage("§f/unClaim §ePara deletar o terreno que você esta no local.");
                    p.sendMessage("§f/info §ePara ver se sua localização tem algum terreno");
                    p.sendMessage("§f/Chunk §ePara ver o chunk que você se localiza");
                    p.sendMessage("§f/chunkall §ePara ver todos os chunks marcado.");
                    p.sendMessage("");
                    return false;
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("claim")) {
            if (!p.getWorld().equals(Bukkit.getWorld("world"))) {
                p.sendMessage("§cNão é possivel salvar terrenos nessa dimensão");
                return false;
            }
            Terrenos terrenoPlayer = Main.getInstance().getManager().getByPlayer(p.getName());
            if (terrenoPlayer == null) {
                Chunk chunk = p.getLocation().getChunk();
                Terrenos terrenoChunk = Main.getInstance().getManager().getByTerreno(chunk);
                if (terrenoChunk == null) {
                    ArrayList<String> chunks = new ArrayList<>();
                    String chunkString = returnChunkString(chunk);
                    chunks.add(chunkString);

                    Terrenos terrenos = new Terrenos(p.getName(), new ArrayList<>(), chunks);
                    terrenos.setAmount(1);

                    p.sendMessage("");
                    p.sendMessage("§2§lPARABENS: §eVocê claimou um terreno novo, com isso ninguem tem permissao alem de você de:");
                    p.sendMessage("§f- §7Quebrar ou colocar blocos");
                    p.sendMessage("§f- §7Abrir baus, fornalhas, crafttable, etc");
                    p.sendMessage("§f- §7Matar animais ou monstros");
                    p.sendMessage("§f- §7PvP dentro do terreno");
                    p.sendMessage("§eSe você quiser que alguem tenha permissões de interagir no seu terreno, use o comando: §6/trust <nome>");
                    p.sendMessage("§eSe você quiser tambem remover as permissões de interagir no seu terreno, use o comando: §6/untrust <nome>");
                    p.sendMessage("§eVocê pode claimar até 12 locais. No momento você claimou: §f" + terrenos.getAmount() + "§6/12");
                    p.sendMessage("");


                    return true;
                } else {
                    p.sendMessage("§aTerreno: §cEssa area ja pertence a um jogador.");
                    return false;
                }
            } else if (terrenoPlayer.getAmount() < 12) {
                Chunk chunk = p.getLocation().getChunk();
                Terrenos terrenoChunk = Main.getInstance().getManager().getByTerreno(chunk);
                if (terrenoChunk == null) {

                    terrenoPlayer.getChunksString().add(returnChunkString(chunk));
                    terrenoPlayer.setAmount(terrenoPlayer.getAmount() + 1);
                    p.sendMessage("");
                    p.sendMessage("§2§lPARABENS: §eVocê salvou mais um terreno. Total §6" + terrenoPlayer.getAmount() + "§e/12");
                    p.sendMessage("");
                    return true;
                } else if (terrenoChunk.getOwner().equals(p.getName()) || terrenoChunk.getFriends().contains(p.getName())) {
                    p.sendMessage("§aTerreno: §aEsse terreno ja lhe pertence.");
                    return false;
                } else p.sendMessage("§aTerreno: §cEsse terreno ja foi claimado por outro jogador");
            } else
                p.sendMessage("§aTerreno: §eVocê atingiu o maximo de claim. Total §f" + terrenoPlayer.getAmount() + "§e/12");
        }

        if (cmd.getName().equalsIgnoreCase("trust")) {
            if (args.length == 0) {
                p.sendMessage("§aTerreno: §eDigite §f/trust <Nome> §epara adicionar alguem no seu terreno");
                return true;
            }

            if (args.length == 1) {
                Terrenos terrenoChunk = Main.getInstance().getManager().getByPlayer(p.getName());
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {

                    if (target.equals(p)) {
                        p.sendMessage("§aTerreno: §eVocê não pode ser adicionado na lista de amigos do seu proprio terreno");
                        return false;
                    }

                    if (terrenoChunk != null) {
                        if (!terrenoChunk.getFriends().contains(target.getName())) {
                            terrenoChunk.getFriends().add(target.getName());
                            p.sendMessage("§aTerreno: §eVocê adicionou o jogador §a" + target.getName() + " §e no seus terrenos");
                            return true;
                        } else {
                            p.sendMessage("§aTerreno: §eEsse jogador ja esta adicionando na lista de amigos.");
                            return false;
                        }
                    } else {
                        p.sendMessage("§aTerreno: §eVocê não contem terrenos no momento para adicionar alguem.");
                        return false;
                    }
                } else {
                    p.sendMessage("§aTerreno: §cEsse jogador não esta online ou você digitou o nome errado.");
                    return false;
                }

            }

        }

        if (cmd.getName().equalsIgnoreCase("untrust")) {

            if(!p.getWorld().equals(Bukkit.getWorld("world")))
                return false;

            if (args.length == 0) {
                p.sendMessage("§aTerreno: §eUse §f/untrust <nome> §epara remover o jogador de seu terrneo");
                return true;
            } else if (args.length == 1) {
                String jogador = args[0];
                Terrenos terrenoPlayer = Main.getInstance().getManager().getByPlayer(p.getName());
                if (terrenoPlayer != null) {
                    if (terrenoPlayer.getFriends().contains(jogador)) {
                        terrenoPlayer.getFriends().remove(jogador);
                        p.sendMessage("§aTerreno: §eJogador §6" + jogador + " §efoi removido da lista de amigos dos terrenos");
                        return true;
                    } else {
                        p.sendMessage("§aTerreno: §cEsse jogador não contem na sua lista de terrenos");
                        return false;
                    }
                } else {
                    p.sendMessage("§aTerreno: §cVocê não tem terrenos para retirar alguem da lista de amigos");
                    return false;
                }

            }
        }

        if (cmd.getName().equalsIgnoreCase("unclaim")) {

            if(!p.getWorld().equals(Bukkit.getWorld("world")))
                return false;

            Chunk playerChunk = p.getLocation().getChunk();
            Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(playerChunk);

            if (byTerreno != null) {
                if (byTerreno.getOwner().equalsIgnoreCase(p.getName())) {
                    String chunkString = returnChunkString(playerChunk);
                    for (String s : byTerreno.getChunksString()) {
                        if (s.equalsIgnoreCase(chunkString)) {
                            if (byTerreno.getAmount() > 1) {

                                byTerreno.getChunksString().remove(chunkString);
                                byTerreno.setAmount(byTerreno.getAmount() - 1);
                                p.sendMessage("§2§lSUCESSO. §eSeu limite de terreno: §6" + byTerreno.getAmount() + "§e/12");
                                return true;
                            } else {
                                Main.getInstance().getManager().getTerrenos().remove(byTerreno);
                                p.sendMessage("§2§lSUCESSO. §eSeu limite de terreno: §6" + byTerreno.getAmount() + "§e/12");
                                p.sendMessage("§eVocê não contem mais terrenos.");
                                Main.getInstance().removeUserToConfig(p);
                                return true;
                            }
                        }
                    }
                } else {
                    if (p.hasPermission("*")) {

                        if(!p.getWorld().equals(Bukkit.getWorld("world")))
                            return false;

                        String chunkString = returnChunkString(playerChunk);
                        ArrayList<String> chunksString = byTerreno.getChunksString();
                        for (String s : chunksString) {
                            if (s.equalsIgnoreCase(chunkString)) {
                                if (byTerreno.getAmount() > 1) {
                                    byTerreno.getChunksString().remove(chunkString);
                                    p.sendMessage("§aVocê removeu um claim do jogador §f" + byTerreno.getOwner() + ".");
                                    return true;
                                }
                                p.sendMessage("§aVocê removeu o ultimo terreno do jogador §f" + byTerreno.getOwner());
                                Main.getInstance().removeUserToConfig(p);
                                Main.getInstance().getManager().getTerrenos().remove(byTerreno);
                                return false;

                            }
                        }
                    }
                    p.sendMessage("§cEsse terreno não lhe pertence para dar unclaim");
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("info")) {

            if(!p.getWorld().equals(Bukkit.getWorld("world")))
                return false;

            if (args.length == 0) {
                Chunk chunk = p.getLocation().getChunk();
                Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(chunk);
                if (byTerreno != null) {
                    if (!p.hasPermission("*")) {
                        String owner = byTerreno.getOwner();
                        p.sendMessage("§eEsse terreno pertece ao jogador: §a" + owner);
                        return true;
                    } else {
                        String owner = byTerreno.getOwner();
                        p.sendMessage("");
                        p.sendMessage("§eInformação do terreno:");
                        p.sendMessage("§fDono: §e" + owner);
                        p.sendMessage("§fAmigos: §e" + byTerreno.getFriends());
                        p.sendMessage("§fQuantidade: §e" + byTerreno.getAmount());
                        p.sendMessage("");
                        p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);

                    }
                } else {
                    p.sendMessage("§cNesse local não contem nem um terreno");
                    return false;
                }
            } else if (args.length == 1) {
                if (p.hasPermission("*")) {
                    Terrenos byPlayer = Main.getInstance().getManager().getByPlayer(args[0]);

                    if (byPlayer != null) {
                        List<String> friends = byPlayer.getFriends();
                        int amount = byPlayer.getAmount();

                        p.sendMessage("§eInformações do jogador: §f" + args[0]);
                        p.sendMessage("   §7Amigos:" + friends);
                        p.sendMessage("   §7Quantiade de terrenos: §f" + amount);
                        p.sendMessage("   §7IP: §f" + Bukkit.getPlayerExact(byPlayer.getOwner()).getAddress().getAddress().getHostAddress());
                        p.sendMessage("");
                        p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
                        return true;
                    } else {
                        p.sendMessage("§cEsse jogador não tem terrenos");
                        return false;
                    }

                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("chunk")) {
            Chunk chunk = p.getLocation().getChunk();
            int bx = chunk.getX() << 4;
            int bz = chunk.getZ() << 4;

            World world = chunk.getWorld();

            for (int xx = bx; xx < bx + 16; xx++) {
                for (int zz = bz; zz < bz + 16; zz++) {
                    for (int yy = 0; yy < 128; yy++) {
                        Location location = world.getBlockAt(xx, yy, zz).getLocation();
                        if (location.getY() == p.getLocation().getBlock().getY() + 5)
                            p.spawnParticle(Particle.BARRIER, location, 1);
                    }
                }
            }
        }

        if (cmd.getName().equalsIgnoreCase("chunkall")) {

            if(!p.getWorld().equals(Bukkit.getWorld("world")))
                return false;


            Chunk playerChunk = p.getLocation().getChunk();
            Terrenos byTerreno = Main.getInstance().getManager().getByTerreno(playerChunk);

            if (byTerreno != null) {
                ArrayList<String> chunksString = byTerreno.getChunksString();

                for (String s : chunksString) {
                    String[] arg = s.split(">");
                    int x = Integer.parseInt(arg[0]);
                    int z = Integer.parseInt(arg[1]);
                    Chunk chunk = Bukkit.getWorld("world").getChunkAt(x, z);

                    int bx = chunk.getX() << 4;
                    int bz = chunk.getZ() << 4;

                    World world = chunk.getWorld();

                    for (int xx = bx; xx < bx + 16; xx++) {
                        for (int zz = bz; zz < bz + 16; zz++) {
                            for (int yy = 0; yy < 128; yy++) {
                                Location location = world.getBlockAt(xx, yy, zz).getLocation();
                                if (location.getY() == p.getLocation().getBlock().getY() + 5)
                                    p.spawnParticle(Particle.BARRIER, location, 1);

                            }
                        }
                    }
                }
            } else {
                p.sendMessage("§cNão tem terreno aqui para ser verificado.");
                return true;
            }
        }

        if (cmd.getName().equalsIgnoreCase("infoall")) {
            if (p.hasPermission("*")) {
                int AmoutAccont = 0;
                int AmountSaveChunks = 0;
                int AmountLoadedChunks = 0;
                int AmountUnloadChunks = 0;
                for (Terrenos terreno : Main.getInstance().getManager().getTerrenos()) {
                    AmoutAccont++;
                    for (String s : terreno.getChunksString()) {
                        AmountSaveChunks++;
                        String[] arg = s.split(">");
                        Chunk world = Bukkit.getWorld("world").getChunkAt(Integer.parseInt(arg[0]), Integer.parseInt(arg[1]));
                        if (world.isLoaded()) {
                            AmountLoadedChunks++;
                        } else {
                            AmountUnloadChunks++;
                        }
                    }
                }
                p.sendMessage("");
                p.sendMessage("§eInformações gerais do plugin:");
                p.sendMessage("§7Contas criadas: §e" + AmoutAccont);
                p.sendMessage("§7Chunks salvos: §e" + AmountSaveChunks);
                p.sendMessage("§7Chunks carregados no momento: §e" + AmountLoadedChunks);
                p.sendMessage("§7Chunks descarregados no momento: §e" + AmountUnloadChunks);
                p.sendMessage("");
                p.playSound(p.getLocation(), Sound.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1, 1);
                return true;
            }
        }

        if (cmd.getName().equalsIgnoreCase("unclaimall")) {
            Terrenos byPlayer = Main.getInstance().getManager().getByPlayer(p.getName());

            if (byPlayer != null) {
                Main.getInstance().getManager().remove(byPlayer);
                p.sendMessage("§aTodos seus terrenos foram removido com sucesso.");
                Main.getInstance().removeUserToConfig(p);
            } else {
                p.sendMessage("§cVocê não contem terrenos para deletar");
                return false;
            }
        }

        return false;
    }

    public String returnChunkString(Chunk chunk) {
        String x = String.valueOf(chunk.getX());
        String z = String.valueOf(chunk.getZ());

        return x + ">" + z;
    }
}
