package com.meteor.fakeplayer.commands;

import com.meteor.jellylib.command.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.List;

public class HelpCommand extends Icmd {
    public HelpCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "help";
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
        Arrays.asList("§aFakePlayer",
                "§f/fakeplayer add [name] §e添加假人",
                "§f/fakeplayer add [name/all] §e移除假人"
        ).forEach(s->commandSender.sendMessage(s));
    }
}
