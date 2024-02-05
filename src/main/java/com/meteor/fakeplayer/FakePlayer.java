package com.meteor.fakeplayer;

import com.meteor.fakeplayer.commands.CommandManager;
import com.meteor.fakeplayer.data.FakePlayerData;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FakePlayer extends JavaPlugin implements Listener {

    public static FakePlayer INSTANCE;

    private FakePlayerData fakePlayerData;
    private CommandManager commandManager;

    @Override
    public void onEnable() {

        INSTANCE = this;

        saveDefaultConfig();

        this.fakePlayerData = new FakePlayerData(this);

        this.commandManager = new CommandManager(this);
        this.commandManager.init();

        getCommand("fakeplayer").setExecutor(commandManager);
        (new PapiHook()).register();
        getServer().getPluginManager().registerEvents(this,this);
    }

    public FakePlayerData getFakePlayerData() {
        return fakePlayerData;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        fakePlayerData.removeAll();
    }

    @EventHandler
    void onPlayerJoin(PlayerJoinEvent joinEvent){
        Bukkit.getScheduler().runTaskLater(this,()->{
          fakePlayerData.update(joinEvent.getPlayer());
        },20L);
    }


}
