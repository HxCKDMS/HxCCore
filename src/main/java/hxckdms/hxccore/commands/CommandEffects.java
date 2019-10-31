package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.event.EventXPBuffs;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandEffects extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 0;
    }

    @Override
    public String getCommandName() {
        return "effects";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (sender instanceof EntityPlayerMP) {
            if (EventXPBuffs.enabled) {
                sender.addChatMessage(new ChatComponentText("You have the following effects..."));
                EntityPlayerMP player = (EntityPlayerMP) sender;
                if (((EntityPlayerMP) sender).experienceLevel > Configuration.lifestealLevel) {
                    float lifesteal = ((float)(player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.lifestealPerBuff);
                    sender.addChatMessage(new ChatComponentText("Lifesteal = " + (int) (Math.min(lifesteal, Configuration.maxLifeSteal) * 100) + "%"));
                }
                sender.addChatMessage(new ChatComponentText("Bonus Health = " + Math.min(Configuration.maxBonusHealth, (player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.healthPerBuff)));
                sender.addChatMessage(new ChatComponentText("Bonus Damage = " + Math.min(Configuration.maxBonusDamage, (player.experienceLevel / Configuration.LevelsPerBuff) * Configuration.damagePerBuff)));
                if (((EntityPlayerMP) sender).experienceLevel > 10) {
					sender.addChatMessage(new ChatComponentText("Mining Speed = " + (int) ((Configuration.miningSpeedPerBuff * (float)(player.experienceLevel / Configuration.LevelsPerBuff)) * 100f) + "% of Block Harvest Time"));
				}
            } else {
                sender.addChatMessage(new ChatComponentText("Server has disabled XP Buffs!"));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
