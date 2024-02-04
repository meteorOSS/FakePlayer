package com.meteor.fakeplayer.commands;

import com.meteor.fakeplayer.FakePlayer;
import com.meteor.fakeplayer.data.FakePlayerData;
import com.meteor.jellylib.command.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RemovePlayerCommand extends Icmd {
    public RemovePlayerCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "remove";
    }

    @Override
    public String getPermission() {
        return null;
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
        FakePlayerData fakePlayerData = FakePlayer.INSTANCE.getFakePlayerData();

        if(strings.length!=2) {
            commandSender.sendMessage("参数错误");
            return;
        }

        if(strings[1].equalsIgnoreCase("all")) {
            fakePlayerData.removeAll();
            commandSender.sendMessage("已移除所有假人");
            return;
        }

        if(fakePlayerData.remove(strings[1])!=-1){
            commandSender.sendMessage("移除成功");
        }else{
            commandSender.sendMessage("没有名为 "+strings[1]+" 的假玩家");
        }
    }
}
