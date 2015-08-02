package HxCKDMS.HxCCore.Asm;

import HxCKDMS.HxCCore.lib.References;
import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

import java.util.Collections;

public class HxCContainer extends DummyModContainer {
    public HxCContainer() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = References.MOD_ID + "-ASM";
        metadata.name = References.MOD_NAME + " ASM";
        metadata.description = "The HxC-Core ASM";
        metadata.version = References.VERSION;
        metadata.authorList = Collections.singletonList("HxCKDMS");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }
}