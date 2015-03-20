package HxCKDMS.HxCCore.Asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class HxCAccessTransformer extends AccessTransformer {
    public HxCAccessTransformer() throws IOException {
        super("HxC_at.cfg");
    }
}
