package hxckdms.hxccore.asm;

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import hxckdms.hxccore.configs.Configuration;
import hxckdms.hxccore.libraries.Constants;
import hxckdms.hxccore.utilities.CapeResourcePack;

import java.util.Collections;

public class HxCContainer extends DummyModContainer {
    public HxCContainer() {
        super(new ModMetadata());
        ModMetadata metadata = getMetadata();
        metadata.modId = Constants.MOD_ID + "-asm";
        metadata.name = Constants.MOD_NAME + " ASM";
        metadata.description = "The HxC-Core ASM";
        metadata.version = Constants.VERSION;
        metadata.authorList = Collections.singletonList("HxCKDMS");
        metadata.parent = Constants.MOD_ID;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Override
    public Class<?> getCustomResourcePackClass() {
        return Configuration.enableCapes ? CapeResourcePack.class : null;
    }
}
