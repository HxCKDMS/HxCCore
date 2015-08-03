package HxCKDMS.HxCCore.Asm;

import HxCKDMS.HxCCore.lib.References;
import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

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