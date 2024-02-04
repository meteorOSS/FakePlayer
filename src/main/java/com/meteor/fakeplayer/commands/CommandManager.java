package com.meteor.fakeplayer.commands;

import com.meteor.fakeplayer.FakePlayer;
import com.meteor.jellylib.command.AbstractCommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager extends AbstractCommandManager {


    public CommandManager(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void init() {
        register(new AddFakePlayerCommand(FakePlayer.INSTANCE));
        register(new RemovePlayerCommand(FakePlayer.INSTANCE));
        register(new HelpCommand(FakePlayer.INSTANCE));
    }
}
