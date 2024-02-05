package com.meteor.fakeplayer.data;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.meteor.fakeplayer.FakePlayer;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FakePlayerData {

    private FakePlayer plugin;

    private Map<UUID,String> fakePlayers;

    // 假玩家数据包列表
    private List<PlayerInfoData> playerInfoDataList;

    public FakePlayerData(FakePlayer plugin){
        this.plugin = plugin;
        this.fakePlayers = new HashMap<>();
        this.playerInfoDataList = new ArrayList<>();
    }

    public String fakePapi(String format){
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        if(players.isEmpty()) return format;
        Collections.shuffle(players);
        return PlaceholderAPI.setPlaceholders(players.get(0),format);
    }

    private String format(String playerName){
        String format = plugin.getConfig().getString("format").replace("{player_name}",playerName);
        ConfigurationSection randomConfig = plugin.getConfig().getConfigurationSection("random");
        for (String key : randomConfig.getKeys(false)) {
            List<String> stringList = randomConfig.getStringList(key);
            Collections.shuffle(stringList);
            format = format.replace(String.format("{%s}",key),stringList.get(0));
        }
        return fakePapi(ChatColor.translateAlternateColorCodes('&',format));
    }

    /**
     * 添加一名假玩家
     * @param playerName
     * @return
     */
    public int add(String playerName){
        if(fakePlayers.values().contains(playerName)){
            return -1;
        }
        UUID uuid = UUID.randomUUID();
        WrappedGameProfile fakeProfile = new WrappedGameProfile(uuid, playerName);
        PlayerInfoData playerInfoData = new PlayerInfoData(fakeProfile, 1, EnumWrappers.NativeGameMode.SURVIVAL,WrappedChatComponent.fromText(format(playerName)));
        playerInfoDataList.add(playerInfoData);
        fakePlayers.put(uuid,playerName);
        return playerInfoDataList.size();
    }

    /**
     * 移除假玩家
     * @param playerName
     * @return
     */
    public int remove(String playerName){

        if(!fakePlayers.values().contains(playerName)) return -1;

        // 移除数据包
        sendAll((player -> removeFakePlayersToPlayerTab(player,
                playerInfoDataList.stream().filter(playerInfoData -> playerInfoData.getProfile().getName().equalsIgnoreCase(playerName)).collect(Collectors.toList()))));

        fakePlayers.entrySet().stream()
                .filter(entry -> playerName.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet()).forEach(fakePlayers::remove);

        playerInfoDataList.removeIf(playerInfoData -> {
            return playerInfoData.getProfile().getName().equalsIgnoreCase(playerName);
        });


        return fakePlayers.size();
    }

    private void sendAll(CallBack callback){
        Bukkit.getOnlinePlayers().forEach(player -> {
            callback.call(player);
        });
    }

    public void update(Player player){
        CallBack callBack = new CallBack() {
            @Override
            public void call(Player player) {
                removeFakePlayersToPlayerTab(player, playerInfoDataList);
                addFakePlayersToPlayerTab(player);
            }
        };

        if(player!=null) callBack.call(player);
        else sendAll(callBack);
    }

    public void removeAll(){
        sendAll((player)->{
            removeFakePlayersToPlayerTab(player,playerInfoDataList);
        });
        this.fakePlayers.clear();
        this.playerInfoDataList.clear();
    }

    /**
     * 发送数据包
     * @param player
     */
    public void addFakePlayersToPlayerTab(Player player) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        packet.getPlayerInfoDataLists().write(0, playerInfoDataList);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removeFakePlayersToPlayerTab(Player player,List<PlayerInfoData> playerInfoDataList) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet.getPlayerInfoDataLists().write(0, playerInfoDataList);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getAmount(){
        return fakePlayers.size();
    }
}
