package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandSmite extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getCommandName() {
        return "smite";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    Vec3 vec3d = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
                    Vec3 vec3d1 = player.getLook(1);
                    Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * 4096, vec3d1.yCoord * 4096, vec3d1.zCoord * 4096);
                    MovingObjectPosition rayTraceResult = player.worldObj.func_147447_a(vec3d, vec3d2, false, false, true);
                    if (rayTraceResult == null) return;
                    player.worldObj.addWeatherEffect(new EntityLightningBolt(player.worldObj, rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ));
                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.smite.world", rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(sender, args.get(0));
                target.worldObj.addWeatherEffect(new EntityLightningBolt(target.worldObj, target.posX, target.posY, target.posZ));
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.smite.other.sender", target.getDisplayName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GRAY)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.smite.other.target", sender.getCommandSenderName()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getAllUsernames()) : Collections.emptyList();
    }
}
