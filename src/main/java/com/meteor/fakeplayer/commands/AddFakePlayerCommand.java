package com.meteor.fakeplayer.commands;

import com.meteor.fakeplayer.FakePlayer;
import com.meteor.fakeplayer.data.FakePlayerData;
import com.meteor.jellylib.command.Icmd;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class AddFakePlayerCommand extends Icmd {
    public AddFakePlayerCommand(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public String label() {
        return "add";
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
        return "添加假人";
    }

    @Override
    public void perform(CommandSender commandSender, String[] strings) {
        if(strings.length!=2) {
            commandSender.sendMessage("参数错误");
            return;
        }
        FakePlayerData fakePlayerData = FakePlayer.INSTANCE.getFakePlayerData();

        if(fakePlayerData.add(strings[1])!=-1){
            commandSender.sendMessage("添加成功,当前数量："+fakePlayerData.getAmount());
        }else {
            commandSender.sendMessage("已有重复名字的玩家");
        }
        fakePlayerData.update(null);
    }
}
