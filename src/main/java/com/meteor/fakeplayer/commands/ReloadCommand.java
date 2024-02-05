package com.meteor.fakeplayer.commands;

import com.meteor.jellylib.command.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand extends Icmd {
    public ReloadCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "reload";
    }

    @Override
    public String getPermission() {
        return "fakeplayer.admin";
    }

    @Override
    public boolean playersOnly() {
        return false;
    }

    @Override
    public String usage() {
        return null;
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        plugin.reloadConfig();
        commandSender.sendMessage("reload done!");
    }
}
