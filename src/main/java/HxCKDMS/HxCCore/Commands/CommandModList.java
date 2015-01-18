package HxCKDMS.HxCCore.Commands;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

public class CommandModList implements ISubCommand {
    public static CommandModList instance = new CommandModList();

    @Override
    public String getCommandName() {
        return "modList";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] args) {
        int listSize = Loader.instance().getModList().size();
        int modsPerPage = 7;
        int pages = (int)Math.ceil(listSize / (float)modsPerPage);

        int page = args.length == 1 ? 0 : Integer.parseInt(args[1])-1;
        int min = Math.min(page * modsPerPage, listSize);

        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + String.format("ModList page: %1$d/%2$d.", page + 1, pages)));
        for(int i = page * modsPerPage; i < modsPerPage + min; i++){
            if(i >= listSize)
                break;

            ModContainer mod = Loader.instance().getModList().get(i);

            EnumChatFormatting color = isPrime(i) ? EnumChatFormatting.DARK_AQUA : EnumChatFormatting.AQUA;

            sender.addChatMessage(new ChatComponentText(color + "MOD_NAME: " + mod.getName() + ", VERSION: " + mod.getVersion() + "."));
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return null;
    }

    public boolean isPrime(int i){
        return (i % 2) == 0;
    }

}