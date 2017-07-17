package hxckdms.hxccore.commands;

import hxckdms.hxccore.api.command.AbstractSubCommand;
import hxckdms.hxccore.api.command.HxCCommand;
import hxckdms.hxccore.api.command.TranslatedCommandException;
import hxckdms.hxccore.libraries.GlobalVariables;
import hxckdms.hxccore.registry.CommandRegistry;
import hxckdms.hxccore.utilities.PermissionHandler;
import hxckdms.hxccore.utilities.ServerTranslationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

//parameters = /HxC protect <regionName> <addUser/removeUser/transfer/create/delete>
// create = /HxC protect <regionName> create <x1> <y1> <z1> <x2> <y2> <z2> <dimension>

@HxCCommand
public class CommandProtect extends AbstractSubCommand<CommandHxC> {
    public static final ArrayList<EntityPlayer> override = new ArrayList<>();

    {
        permissionLevel = 4;
    }

    @Override
    public String getName() {
        return "protect";
    }

    @Override
    public void execute(ICommandSender sender, LinkedList<String> args) throws CommandException {
        NBTTagCompound protectedLands = GlobalVariables.customWorldData.getTagCompound("protectedLands", new NBTTagCompound());
        NBTTagCompound land = protectedLands.getCompoundTag(args.get(0));

        switch (args.get(1).toLowerCase()) {
            case "adduser":
                if (!protectedLands.hasKey(args.get(0))) throw new TranslatedCommandException(sender, "commands.protect.error.noSuchLand");

                EntityPlayer target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(2));
                NBTTagList userList = land.getTagList("userList", 8);
                boolean match = false;
                for (int i = 0; i < userList.tagCount(); ++i) {
                    match = userList.getStringTagAt(i).equals(target.getUniqueID().toString());
                    if (match) break;
                }
                if (!match) userList.appendTag(new NBTTagString(target.getUniqueID().toString()));
                land.setTag("userList", userList);
                protectedLands.setTag(args.get(0), land);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.protect.adduser.sender", target.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.BLUE)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.protect.adduser.target", sender.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.BLUE)));
                break;
            case "removeuser":
                if (!protectedLands.hasKey(args.get(0))) throw new TranslatedCommandException(sender, "commands.protect.error.noSuchLand");

                target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(2));
                userList = land.getTagList("userList", 8);

                match = false;
                for (int i = 0; i < userList.tagCount(); ++i) {
                    match = userList.getStringTagAt(i).equals(target.getUniqueID().toString());
                    if (match) {
                        userList.removeTag(i);
                        --i;
                    }
                }
                if (!match) throw new TranslatedCommandException(sender, "commands.protect.error.playerNotPresent");
                land.setTag("userList", userList);
                protectedLands.setTag(args.get(0), land);
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.protect.removeuser.sender", target.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.protect.removeuser.target", sender.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.RED)));

                break;
            case "transfer":
                target = CommandBase.getPlayer(GlobalVariables.server, sender, args.get(2));
                land.setBoolean("playerOwned", true);
                land.setString("owner", target.getUniqueID().toString());
                sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.protect.transfer.sender", target.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                target.addChatMessage(ServerTranslationHelper.getTranslation(target, "commands.protect.transfer.target", sender.getDisplayName(), args.get(0)).setStyle(new Style().setColor(TextFormatting.YELLOW)));
                protectedLands.setTag(args.get(0), land);
                break;
            case "create":
                land.setBoolean("playerOwned", sender instanceof EntityPlayerMP);
                land.setString("owner", sender instanceof EntityPlayer ? ((EntityPlayer) sender).getUniqueID().toString() : sender.getName());
                String name = args.get(0);

                int x1 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getX(), args.get(2), true).getResult());
                int y1 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getY(), args.get(3), false).getResult());
                int z1 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getZ(), args.get(4), true).getResult());
                int x2 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getX(), args.get(5), true).getResult());
                int y2 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getY(), args.get(6), false).getResult());
                int z2 = (int) Math.round(CommandBase.parseCoordinate(sender.getPosition().getZ(), args.get(7), true).getResult());
                int dimension = CommandBase.parseInt(args.get(8));
                int blocksNeeded = Math.abs((x1 - x2) * (y1 - y2) * (z1 - z2));

                if (CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount == -1 || getSenderUsedBlocks(sender) + blocksNeeded <= CommandRegistry.CommandConfig.commandPermissions.get(PermissionHandler.getPermissionLevel(sender)).homeAmount) {
                    if (!landAvailabilityCheck(x1, y1, z1, x2, y2, z2 ,dimension)) throw new TranslatedCommandException(sender, "commands.protect.error.alreadyClaimed");

                    land.setInteger("x1", x1);
                    land.setInteger("y1", y1);
                    land.setInteger("z1", z1);
                    land.setInteger("x2", x2);
                    land.setInteger("y2", y2);
                    land.setInteger("z2", z2);
                    land.setInteger("dimension", dimension);
                    protectedLands.setTag(args.get(0), land);

                    sender.addChatMessage(ServerTranslationHelper.getTranslation(sender, "commands.protect.create", x1, y1, z1, x2, y2, z2, blocksNeeded, name));
                } else throw new TranslatedCommandException(sender, "commands.protect.error.outOfLand");
                break;
            case "delete":
                if (!protectedLands.hasKey(args.get(0))) throw new TranslatedCommandException(sender, "commands.protect.error.noSuchLand");
                protectedLands.removeTag(args.get(0));

                break;
            default:
                throw new TranslatedCommandException(sender, "commands.protect.error.noSuchMode");
        }
        GlobalVariables.customWorldData.setTagCompound("protectedLands", protectedLands);
    }

    private static boolean landAvailabilityCheck(int x1, int y1, int z1, int x2, int y2, int z2, int dimension) {
        NBTTagCompound protectedLands = GlobalVariables.customWorldData.getTagCompound("protectedLands", new NBTTagCompound());
        for (String landName : protectedLands.getKeySet()) {
            System.out.println(landName);
            NBTTagCompound land = protectedLands.getCompoundTag(landName);
            boolean x1Check = land.getInteger("x1") > land.getInteger("x2") ? (x1 > x2 ? land.getInteger("x1") >= x1 && x1 >= land.getInteger("x2") : land.getInteger("x1") >= x2 && x2 >= land.getInteger("x2")) : (x1 > x2 ? land.getInteger("x2") >= x1 && x1 >= land.getInteger("x1") : land.getInteger("x2") >= x2 && x2 >= land.getInteger("x1"));
            boolean y1Check = land.getInteger("y1") > land.getInteger("y2") ? (y1 > y2 ? land.getInteger("y1") >= y1 && y1 >= land.getInteger("y2") : land.getInteger("y1") >= y2 && y2 >= land.getInteger("y2")) : (y1 > y2 ? land.getInteger("y2") >= y1 && y1 >= land.getInteger("y1") : land.getInteger("y2") >= y2 && y2 >= land.getInteger("y1"));
            boolean z1Check = land.getInteger("z1") > land.getInteger("z2") ? (z1 > z2 ? land.getInteger("z1") >= z1 && z1 >= land.getInteger("z2") : land.getInteger("z1") >= z2 && z2 >= land.getInteger("z2")) : (z1 > z2 ? land.getInteger("z2") >= z1 && z1 >= land.getInteger("z1") : land.getInteger("z2") >= z2 && z2 >= land.getInteger("z1"));
            
            boolean x2Check = land.getInteger("x1") > land.getInteger("x2") ? (x1 > x2 ? land.getInteger("x1") < x1 && x2 < land.getInteger("x2") : land.getInteger("x1") < x2 && x1 < land.getInteger("x2")) : (x1 > x2 ? land.getInteger("x2") < x1 && x2 < land.getInteger("x1") : land.getInteger("x2") < x2 && x1 < land.getInteger("x1"));
            boolean y2Check = land.getInteger("y1") > land.getInteger("y2") ? (y1 > y2 ? land.getInteger("y1") < y1 && y2 < land.getInteger("y2") : land.getInteger("y1") < y2 && y1 < land.getInteger("y2")) : (y1 > y2 ? land.getInteger("y2") < y1 && y2 < land.getInteger("y1") : land.getInteger("y2") < y2 && y1 < land.getInteger("y1"));
            boolean z2Check = land.getInteger("z1") > land.getInteger("z2") ? (z1 > z2 ? land.getInteger("z1") < z1 && z2 < land.getInteger("z2") : land.getInteger("z1") < z2 && z1 < land.getInteger("z2")) : (z1 > z2 ? land.getInteger("z2") < z1 && z2 < land.getInteger("z1") : land.getInteger("z2") < z2 && z1 < land.getInteger("z1"));

            boolean dimensionCheck = land.getInteger("dimension") == dimension;

            if ((x1Check || x2Check) && (y1Check || y2Check) && (z1Check || z2Check) && dimensionCheck) return false;
        }
        return true;
    }

    public static boolean isPlayerAllowedToEdit(EntityPlayer player, int x, int y, int z, int dimension) {
        if (override.contains(player)) return true;

        String name;
        if ((name = getLand(x, y, z, dimension)) == null) return true;
        NBTTagCompound protectedLands = GlobalVariables.customWorldData.getTagCompound("protectedLands", new NBTTagCompound());
        NBTTagCompound land = protectedLands.getCompoundTag(name);
        boolean allowed = land.getString("owner").equals(player.getUniqueID().toString());

        if (!allowed) {
            NBTTagList userList = land.getTagList("userList", 8);
            for (int i = 0; i < userList.tagCount(); ++i) {
                allowed = userList.getStringTagAt(i).equals(player.getUniqueID().toString());
                if (allowed) break;
            }
        }
        return allowed;
    }

    private static String getLand(int x, int y, int z, int dimension) {
        NBTTagCompound protectedLands = GlobalVariables.customWorldData.getTagCompound("protectedLands", new NBTTagCompound());
        for (String landName : protectedLands.getKeySet()) {
            NBTTagCompound land = protectedLands.getCompoundTag(landName);
            boolean xCheck = land.getInteger("x1") > land.getInteger("x2") ? land.getInteger("x1") >= x && x >= land.getInteger("x2") : land.getInteger("x2") >= x && x >= land.getInteger("x1");
            boolean yCheck = land.getInteger("y1") > land.getInteger("y2") ? land.getInteger("y1") >= y && y >= land.getInteger("y2") : land.getInteger("y2") >= y && y >= land.getInteger("y1");
            boolean zCheck = land.getInteger("z1") > land.getInteger("z2") ? land.getInteger("z1") >= z && z >= land.getInteger("z2") : land.getInteger("z2") >= z && z >= land.getInteger("z1");

            boolean dimensionCheck = land.getInteger("dimension") == dimension;

            if (xCheck && yCheck && zCheck && dimensionCheck) return landName;
        }
        return null;
    }

    private static int getSenderUsedBlocks(ICommandSender sender) {
        if (sender instanceof EntityPlayerMP) {
            int playerOwnedBlocks = 0;
            NBTTagCompound protectedLands = GlobalVariables.customWorldData.getTagCompound("protectedLands", new NBTTagCompound());
            for (String landName : protectedLands.getKeySet()) {
                NBTTagCompound land = protectedLands.getCompoundTag(landName);
                if (land.getBoolean("playerOwned") && land.getString("owner").equals(((EntityPlayerMP) sender).getUniqueID().toString()))
                    playerOwnedBlocks += Math.abs((land.getInteger("x1") - land.getInteger("x2")) * (land.getInteger("y1") - land.getInteger("y2")) * (land.getInteger("z1") - land.getInteger("z2")));
            }
            return playerOwnedBlocks;
        } else return -1;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, LinkedList<String> args, @Nullable BlockPos pos) {
        if (args.size() == 2) Arrays.asList("addUser", "removeUser", "transfer", "create", "delete");
        return Collections.emptyList();
    }
}
