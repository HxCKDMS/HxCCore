package hxckdms.hxccore.registry.command;

import hxckdms.hxccore.api.command.AbstractCommandMain;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface HxCCommand {
    int defaultPermission();
    Class<? extends AbstractCommandMain> mainCommand();
    boolean isEnabled();
}
