package com.meteor.fakeplayer;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PapiHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "serverinfo";
    }

    @Override
    public String getAuthor() {
        return "meteor";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        return String.valueOf(Bukkit.getOnlinePlayers().size()+FakePlayer.INSTANCE.getFakePlayerData().getAmount());
    }
}
