package hxckdms.hxccore.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.IChatComponent;

@Cancelable
public class EmoteEvent extends Event {
    private final String message;
    private final ICommandSender sender;
    private IChatComponent component;

    public EmoteEvent(ICommandSender sender, IChatComponent component) {
        this.message = component.getUnformattedText();
        this.sender = sender;
        this.component = component;
    }

    public void setComponent(IChatComponent component) {
        this.component = component;
    }

    public String getMessage() {
        return message;
    }

    public ICommandSender getSender() {
        return sender;
    }

    public IChatComponent getComponent() {
        return component;
    }
}
