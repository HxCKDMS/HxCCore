package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.utilities.TeleportHelper;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@HxCCommand
public class CommandJump extends AbstractSubCommand<CommandHxC> {
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

        Vec3d vec3d = new Vec3d(player.posX, player.posY + (double)player.getEyeHeight(), player.posZ);
        Vec3d vec3d1 = player.getLook(1);
        Vec3d vec3d2 = vec3d.addVector(vec3d1.xCoord * 4096, vec3d1.yCoord * 4096, vec3d1.zCoord * 4096);
        RayTraceResult rayTraceResult = player.world.rayTraceBlocks(vec3d, vec3d2, false, false, true);
        if (rayTraceResult == null) return;
        BlockPos pos = rayTraceResult.getBlockPos();
        System.out.println(player.world.getBlockState(rayTraceResult.getBlockPos()).getMaterial().isSolid());
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK)
            TeleportHelper.teleportEntityToDimension(player, pos.up(), player.dimension);
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        return Collections.emptyList();
    }
}
