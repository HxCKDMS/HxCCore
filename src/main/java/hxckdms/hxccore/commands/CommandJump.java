package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class  CommandJump extends AbstractSubCommand<CommandHxC> {
    {
        permissionLevel = 2;
    }

    @Override
    public String getCommandName() {
        return "jump";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP)) throw new TranslatedCommandException(sender, "commands.exception.playersOnly");
        EntityPlayerMP player = (EntityPlayerMP) sender;

        Vec3 vec3d = Vec3.createVectorHelper(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3 vec3d1 = player.getLook(1);
        Vec3 vec3d2 = vec3d.addVector(vec3d1.xCoord * 4096, vec3d1.yCoord * 4096, vec3d1.zCoord * 4096);
        MovingObjectPosition rayTraceResult = player.worldObj.func_147447_a(vec3d, vec3d2, false, false, true);
        if (rayTraceResult == null) return;
        if (rayTraceResult.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            TeleportHelper.teleportSafely(player, rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ, player.dimension);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args) {
        return Collections.emptyList();
    }
}
