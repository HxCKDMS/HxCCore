package hxckdms.hxccore.api.event;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class EmoteEvent extends Event {
    private final String message;
    private final ICommandSender sender;
    private ITextComponent component;

    public EmoteEvent(ICommandSender sender, ITextComponent component) {
        this.message = component.getUnformattedText();
        this.sender = sender;
        this.component = component;
    }

    public void setComponent(ITextComponent component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public ICommandSender getSender() {
        return sender;
    }

    public ITextComponent getComponent() {
        return component;
    }
}
