package HxCKDMS.HxCCore.Commands;

import HxCKDMS.HxCCore.Configs.CommandsConfig;
import HxCKDMS.HxCCore.Events.EventProtection;
import HxCKDMS.HxCCore.api.Handlers.CommandsHandler;
import HxCKDMS.HxCCore.api.Handlers.NBTFileIO;
import HxCKDMS.HxCCore.api.Handlers.PermissionsHandler;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.api.Command.HxCCommand;
import HxCKDMS.HxCCore.api.Command.ISubCommand;
import HxCKDMS.HxCCore.lib.References;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

@SuppressWarnings({"unchecked", "unused"})
@HxCCommand(defaultPermission = 5, mainCommand = CommandsHandler.class, isEnabled = true)
public class CommandProtect implements ISubCommand {
    private static CommandProtect instance = new CommandProtect();

    @Override
    public String getCommandName() {
        return "Protect";
    }

    @Override
    public int[] getCommandRequiredParams() {
        return new int[]{2, -1, -1};
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args, boolean isPlayer) throws WrongUsageException {
        if (isPlayer) {
            EntityPlayerMP player = (EntityPlayerMP)sender;
            boolean CanSend = PermissionsHandler.canUseCommand(CommandsConfig.CommandPermissions.get(getCommandName()), player);
            if (CanSend) {
                //parameters = /HxC protect <regionName> <add/transfer/create/delete>
                // create = /HxC protect <regionName> create <x1> <y1> <z1> <x2> <y2> <z2>
                ChatComponentText t;
                NBTTagList land;
                NBTTagCompound protectedLandList = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "protectedLand");
                NBTTagCompound protectedLands = NBTFileIO.getNbtTagCompound(HxCCore.CustomWorldData, "protectedLands");
                NBTTagList lands = protectedLands.getTagList("lands", 8);
                switch (args[2]) {
                    case("add") :
                        land = protectedLandList.getTagList(args[1], 8);
                        land.appendTag(new NBTTagString(args[3]));
                        protectedLandList.setTag(args[1], land);
                        NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLand", protectedLandList);
                        break;
                    case("remove") :
                        land = protectedLandList.getTagList(args[1], 8);
                        for (int i = 0; i < land.tagCount(); i++) {
                            if (land.getStringTagAt(i).equals(args[3])) {
                                land.removeTag(i);
                                protectedLandList.setTag(args[1], land);
                                break;
                            }
                        }
                        NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLand", protectedLandList);
                        break;
                    case("create") :
                        if ((References.PROTECT_SIZE[PermissionsHandler.permLevel(player)] == -1) || (Integer.parseInt(args[3]) * Integer.parseInt(args[4]) * Integer.parseInt(args[5]) * Integer.parseInt(args[6]) * Integer.parseInt(args[7]) * Integer.parseInt(args[8])) < References.PROTECT_SIZE[PermissionsHandler.permLevel(player)]) {
                            land = protectedLandList.getTagList(args[1], 8);
                            lands.appendTag(new NBTTagString(args[1] + "=" + player.dimension + ", " + args[3] + ", " + args[4] + ", " + args[5] + ", " + args[6] + ", " + args[7] + ", " + args[8]));
                            land.appendTag(new NBTTagString(player.getCommandSenderName()));
                            protectedLandList.setTag(args[1], land);
                            protectedLands.setTag("lands", lands);
                            NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLand", protectedLandList);
                            NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLands", protectedLands);
                            EventProtection.protectedZones.put(args[1], new int[]{player.dimension, Integer.valueOf(args[3]), Integer.valueOf(args[4]), Integer.valueOf(args[5]), Integer.valueOf(args[6]), Integer.valueOf(args[7]), Integer.valueOf(args[8])});
                            t = new ChatComponentText("Successfully protected coordinates minX(" + args[3] + ") minY(" + args[4] + ") minZ(" + args[5] + ") to maxX(" + args[6] + ") maxY(" + args[7] + ") maxZ(" + args[8] + ").");
                            t.getChatStyle().setColor(EnumChatFormatting.AQUA);
                            player.addChatMessage(t);
                        }
                        break;
                    case("delete") :
                        EventProtection.protectedZones.remove(args[1]);
                        for (int i = 0; i < lands.tagCount(); i++) {
                            if (lands.getStringTagAt(i).startsWith(args[1])) {
                                lands.removeTag(i);
                                break;
                            }
                        }
                        protectedLandList.removeTag(args[1]);
                        protectedLands.setTag("lands", lands);
                        NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLand", protectedLandList);
                        NBTFileIO.setNbtTagCompound(HxCCore.CustomWorldData, "protectedLands", protectedLands);
                        t = new ChatComponentText("Successfully deleted " + args[1] + ".");
                        t.getChatStyle().setColor(EnumChatFormatting.RED);
                        player.addChatMessage(t);
                        break;
                    default:
                        System.out.println(args[1]);
                        sender.addChatMessage(new ChatComponentText("Not a valid mode"));
                        break;
                }
            } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.permission"));
        } else throw new WrongUsageException(HxCCore.util.getTranslation((isPlayer ? ((EntityPlayerMP) sender).getUniqueID() : java.util.UUID.randomUUID()), "commands.exception.playersonly"));
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }
}
