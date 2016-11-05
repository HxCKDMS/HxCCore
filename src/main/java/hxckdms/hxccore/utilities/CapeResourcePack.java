package hxckdms.hxccore.utilities;

import cpw.mods.fml.common.FMLContainerHolder;
import cpw.mods.fml.common.ModContainer;
import hxckdms.hxccore.libraries.GlobalVariables;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CapeResourcePack extends FolderResourcePack implements FMLContainerHolder {
    private ModContainer container;

    @SuppressWarnings("ConstantConditions")
    public CapeResourcePack(ModContainer container) {
        super(null);
        this.container = container;
    }

    @Override
    public ModContainer getFMLContainer() {
        return container;
    }

    @Override
    public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException {
        return null;
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return GlobalVariables.playerCapes.containsKey(location.getResourcePath());
    }

    @Override
    protected InputStream getInputStreamByName(String name) throws IOException {
        return new ByteArrayInputStream(GlobalVariables.playerCapes.get(name.replace("assets/hxccapes/", "")));
    }

    @Override
    public Set<String> getResourceDomains() {
        return new HashSet<>(Collections.singleton("hxccapes"));
    }

    @Override
    public String getPackName() {
        return "HxCCapeResourcePack:HxC-Core";
    }
}
