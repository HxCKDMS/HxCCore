package hxckdms.hxccore.asm;

import com.google.common.eventbus.EventBus;
import hxckdms.hxccore.libraries.Constants;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

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
}
