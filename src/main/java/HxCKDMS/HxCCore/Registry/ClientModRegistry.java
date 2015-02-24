package HxCKDMS.HxCCore.Registry;

import HxCKDMS.HxCCore.Api.EnumHxCRegistryType;
import HxCKDMS.HxCCore.Api.HxCClientRegistry;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ClientModRegistry {
    public static ArrayList<Class<HxCClientRegistry>> registryClasses = new ArrayList<Class<HxCClientRegistry>>();

    private static void registerObjects(Set<ASMDataTable.ASMData> asmData){
        if(asmData != null && !asmData.isEmpty()){
            for(final ASMDataTable.ASMData data : asmData){
                String className = data.getClassName();

                try{
                    @SuppressWarnings("unchecked")
                    final Class<HxCClientRegistry> registryClass = (Class<HxCClientRegistry>) Class.forName(className);
                    String type = registryClass.getAnnotation(HxCClientRegistry.class).registryType().toString().toLowerCase();
                    className = registryClass.getName().substring(registryClass.getName().lastIndexOf(".") + 1);
                    registryClasses.add(registryClass);
                    LogHelper.info("Registered " + type + ": " + className + " from class: " + className + ".", References.MOD_NAME);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void init(Set<ASMDataTable.ASMData> asmData, LoaderState.ModState modState){
        registerObjects(asmData);

        for (final Class<HxCClientRegistry> registryClass : registryClasses){
            String className = registryClass.getName().substring(registryClass.getName().lastIndexOf(".") + 1);
            String type = registryClass.getAnnotation(HxCClientRegistry.class).registryType().toString().toLowerCase();
            try{
                if(modState == LoaderState.ModState.PREINITIALIZED && preInit(registryClass)){
                    LogHelper.info(StringUtils.capitalize(type) + " initialized in PreInit: " + className + ".", References.MOD_NAME);
                }
            }catch(Exception e){
                LogHelper.error(StringUtils.capitalize(type) + " failed to initialize!", References.MOD_NAME);
                LogHelper.error("reason for failure: " + e.getLocalizedMessage(), References.MOD_NAME);
            }
        }

    }

    private static boolean preInit(Class<HxCClientRegistry> registryClass){
        boolean successful = false;
        try{
            EnumHxCRegistryType type = registryClass.getAnnotation(HxCClientRegistry.class).registryType();

            if(type == EnumHxCRegistryType.TILEENTITYSPECIALRENDERER){
                ClientRegistry.bindTileEntitySpecialRenderer(registryClass.getAnnotation(HxCClientRegistry.class).tileEntityForRenderer(), (TileEntitySpecialRenderer) registryClass.newInstance());
                successful = true;
            }else if(type == EnumHxCRegistryType.ITEMRENDERER){
                if(registryClass.getAnnotation(HxCClientRegistry.class).blockForRenderer() != Block.class){
                    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(CommonModRegistry.blockRegistry.get(registryClass.getAnnotation(HxCClientRegistry.class).blockForRenderer())), (IItemRenderer) registryClass.newInstance());
                    successful = true;
                }else if(registryClass.getAnnotation(HxCClientRegistry.class).itemForRenderer() != Item.class){
                    MinecraftForgeClient.registerItemRenderer(CommonModRegistry.itemRegistry.get(registryClass.getAnnotation(HxCClientRegistry.class).itemForRenderer()), (IItemRenderer) registryClass.newInstance());
                    successful = true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return successful;
    }
}
