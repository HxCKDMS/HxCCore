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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandSmite extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 3;
    }

    @Override
    public String getName() {
        return "smite";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        switch (args.size()) {
            case 0:
                if (sender instanceof EntityPlayerMP) {
                    EntityPlayerMP player = (EntityPlayerMP) sender;

                    Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
                    Vec3d vec3d1 = player.getLook(1);
                    Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * 4096, vec3d1.yCoord * 4096, vec3d1.zCoord * 4096);
                    RayTraceResult rayTraceResult = player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
                    if (rayTraceResult == null) return;
                    BlockPos pos = rayTraceResult.getBlockPos();
                    player.world.addWeatherEffect(new EntityLightningBolt(player.world, pos.getX(), pos.getY(), pos.getZ(), false));
                    sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.smite.world", pos.getX(), pos.getY(), pos.getZ()).setStyle(new Style().setColor(TextFormatting.GOLD)));
                }
                break;
            case 1:
                EntityPlayerMP target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(0));
                target.world.addWeatherEffect(new EntityLightningBolt(target.world, target.posX, target.posY, target.posZ, false));
                sender.sendMessage(ServerTranslationHelper.getTranslation(sender, "commands.smite.other.sender", target.getDisplayName()).setStyle(new Style().setColor(TextFormatting.GRAY)));
                target.sendMessage(ServerTranslationHelper.getTranslation(target, "commands.smite.other.target", sender.getDisplayName()).setStyle(new Style().setColor(TextFormatting.RED)));
                break;
        }
    }

    @Override
    public List<String> addTabCompletions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return args.size() == 1 ? CommandBase.getListOfStringsMatchingLastWord(args.toArray(new String[args.size()]), GlobalVariables.server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
