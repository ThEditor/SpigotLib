package me.theditor.spigot.api.bungee;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

public class BungeeMessagingApi implements PluginMessageListener {

    private HashMap<String, LinkedBlockingQueue<Consumer<?>>> callbacks;
    private JavaPlugin plugin;

    public BungeeMessagingApi(JavaPlugin plugin) {
        this.plugin = plugin;
        this.callbacks = new HashMap<>();
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, "BungeeCord", this);
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if(!channel.equals("BungeeCord")) return;
        ByteArrayDataInput input = ByteStreams.newDataInput(message);
        String subchannel = input.readUTF();

        try {
            switch (subchannel) {
                case "PlayerCount": {
                    String server = input.readUTF();
                    int count = input.readInt();
                    LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("PlayerCount-" + server);
                    if(queue != null) {
                        Consumer<Integer> back = (Consumer<Integer>) queue.poll();
                        if (back != null) {
                            back.accept(count);
                        }
                        if(queue.isEmpty()) callbacks.remove("PlayerCount-" + server);
                    }
                    break;
                }

                case "GetServers": {
                    List<String> servers = Arrays.asList(input.readUTF().split(", "));
                    LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("GetServers");
                    if(queue != null) {
                        Consumer<List<String>> back = (Consumer<List<String>>) queue.poll();
                        if (back != null) {
                            back.accept(servers);
                        }
                        if(queue.isEmpty()) callbacks.remove("GetServers");
                    }
                    break;
                }

                case "GetServer": {
                    String server = input.readUTF();
                    LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("GetServer");
                    if(queue != null) {
                        Consumer<String> back = (Consumer<String>) queue.poll();
                        if (back != null) {
                            back.accept(server);
                        }
                        if(queue.isEmpty()) callbacks.remove("GetServer");
                    }
                    break;
                }

                default:
                    break;
            }
        } catch (Exception e) {
        }
    }

    public void getPlayerCount(String serverName, Consumer<Integer> callback) {
        try {
            Player player = getFirstPlayer();

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("PlayerCount");
            output.writeUTF(serverName);
            player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
            LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("PlayerCount-" + serverName);
            if (queue != null) {
                queue.put(callback);
            } else {
                queue = new LinkedBlockingQueue<>();
                queue.put(callback);
                callbacks.put("PlayerCount-" + serverName, queue);
            }
        } catch (InterruptedException e) {
            plugin.getLogger().severe("Failed to put in queue: PlayerCount-" + serverName);
            e.printStackTrace();
        }
    }

    public void getServers(Consumer<List<String>> callback) {
        try {
            Player player = getFirstPlayer();

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("GetServers");
            player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
            LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("GetServers");
            if (queue != null) {
                queue.put(callback);
            } else {
                queue = new LinkedBlockingQueue<>();
                queue.put(callback);
                callbacks.put("GetServers", queue);
            }
        } catch (InterruptedException e) {
            plugin.getLogger().severe("Failed to put in queue: GetServers");
            e.printStackTrace();
        }
    }

    public void getServer(Consumer<String> callback) {
        try {
            Player player = getFirstPlayer();

            ByteArrayDataOutput output = ByteStreams.newDataOutput();
            output.writeUTF("GetServer");
            player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
            LinkedBlockingQueue<Consumer<?>> queue = callbacks.get("GetServer");
            if (queue != null) {
                queue.put(callback);
            } else {
                queue = new LinkedBlockingQueue<>();
                queue.put(callback);
                callbacks.put("GetServer", queue);
            }
        } catch (InterruptedException e) {
            plugin.getLogger().severe("Failed to put in queue: GetServers");
            e.printStackTrace();
        }
    }

    public void connect(Player player, String serverName) {
        ByteArrayDataOutput output = ByteStreams.newDataOutput();
        output.writeUTF("Connect");
        output.writeUTF(serverName);
        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    public void connectOther(String playerName, String server) {
        Player player = getFirstPlayer();
        ByteArrayDataOutput output = ByteStreams.newDataOutput();

        output.writeUTF("ConnectOther");
        output.writeUTF(playerName);
        output.writeUTF(server);

        player.sendPluginMessage(this.plugin, "BungeeCord", output.toByteArray());
    }

    private Player getFirstPlayer() {

        Player player = Iterables.getFirst(plugin.getServer().getOnlinePlayers(), null);

        if (player == null) {
            throw new IllegalArgumentException("Bungee Messaging Api requires at least one player to be online.");
        }

        return player;
    }
}
