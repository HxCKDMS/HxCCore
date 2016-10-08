package hxckdms.hxccore.asm;

import hxckdms.hxccore.api.event.EmoteEvent;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

public class HxCHooks {
    public static ITextComponent onEmoteEvent(ICommandSender sender, ITextComponent component) {
        EmoteEvent event = new EmoteEvent(sender, component);

        if (MinecraftForge.EVENT_BUS.post(event)) return null;
        else if(component == event.getComponent()) return new TextComponentTranslation("chat.type.emote", sender.getDisplayName(), component);
        else return event.getComponent();
    }
}
