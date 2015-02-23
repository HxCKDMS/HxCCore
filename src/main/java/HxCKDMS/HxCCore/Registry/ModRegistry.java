package HxCKDMS.HxCCore.Registry;

import HxCKDMS.HxCCore.Api.EnumHxCRegistryType;
import HxCKDMS.HxCCore.Api.HxCRegistry;
import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class ModRegistry {
    public static ArrayList<Class<HxCRegistry>> registryClasses = new ArrayList<Class<HxCRegistry>>();
    public static HashMap<Class, Block> blockRegistry = new HashMap<Class, Block>();
    public static HashMap<Class, Item> itemRegistry = new HashMap<Class, Item>();
    
    private static void registerObjects(Set<ASMDataTable.ASMData> asmData){
        if(asmData != null && !asmData.isEmpty()){
            for(final ASMDataTable.ASMData data : asmData){
                String className = data.getClassName();

                try{
                    @SuppressWarnings("unchecked")
                    final Class<HxCRegistry> registryClass = (Class<HxCRegistry>) Class.forName(className);
                    String unlocalizedName = registryClass.getAnnotation(HxCRegistry.class).unlocalizedName();
                    String type = registryClass.getAnnotation(HxCRegistry.class).registryType().toString().toLowerCase();
                    className = registryClass.getName().substring(registryClass.getName().lastIndexOf(".") + 1);
                    registryClasses.add(registryClass);
                    LogHelper.info("Registered " + type + ": " + unlocalizedName + " from class: " + className + ".", References.MOD_NAME);
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void init(Set<ASMDataTable.ASMData> asmData, LoaderState.ModState modState){
        registerObjects(asmData);
        
        for (final Class<HxCRegistry> registryClass : registryClasses){
            final String unlocalizedName = registryClass.getAnnotation(HxCRegistry.class).unlocalizedName();
            String type = registryClass.getAnnotation(HxCRegistry.class).registryType().toString().toLowerCase();
            try{
                if(modState == LoaderState.ModState.PREINITIALIZED && preInit(registryClass)){
                    LogHelper.info(StringUtils.capitalize(type) + " initialized in PreInit: " + unlocalizedName + ".", References.MOD_NAME);
                }
            }catch(Exception e){
                LogHelper.error(StringUtils.capitalize(type) + " failed to initialize!", References.MOD_NAME);
                LogHelper.error("reason for failure: " + e.getLocalizedMessage(), References.MOD_NAME);
            }
        }

    }

    private static boolean preInit(Class<HxCRegistry> registryClass){
        boolean successful = false;
        try{
            EnumHxCRegistryType type = registryClass.getAnnotation(HxCRegistry.class).registryType();
            
            if(type == EnumHxCRegistryType.BLOCK){
                Block block = (Block)registryClass.newInstance();
                block.setBlockName(registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                blockRegistry.put(registryClass, block);
                
                if(registryClass.getAnnotation(HxCRegistry.class).itemBlock() != ItemBlock.class){
                    GameRegistry.registerBlock(block, registryClass.getAnnotation(HxCRegistry.class).itemBlock(), registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                    successful = true;
                }else{
                    GameRegistry.registerBlock(block, registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                    successful = true;
                }
                
                if(registryClass.getAnnotation(HxCRegistry.class).itemRenderer() != IItemRenderer.class && HxCCore.proxy.getSide() == Side.CLIENT){
                    MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), registryClass.getAnnotation(HxCRegistry.class).itemRenderer().newInstance());
                }
                
            }else if(type == EnumHxCRegistryType.ITEM){
                Item item = (Item)registryClass.newInstance();
                item.setUnlocalizedName(registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                itemRegistry.put(registryClass, item);
                GameRegistry.registerItem(item, registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                
                if(registryClass.getAnnotation(HxCRegistry.class).itemRenderer() != IItemRenderer.class && HxCCore.proxy.getSide() == Side.CLIENT){
                    MinecraftForgeClient.registerItemRenderer(item, registryClass.getAnnotation(HxCRegistry.class).itemRenderer().newInstance());
                    successful = true;
                }
                
                successful = true;
            }else if(type == EnumHxCRegistryType.TILEENTITY) {
                TileEntity tileEntity = (TileEntity) registryClass.newInstance();
                GameRegistry.registerTileEntity(tileEntity.getClass(), registryClass.getAnnotation(HxCRegistry.class).unlocalizedName());
                
                if(registryClass.getAnnotation(HxCRegistry.class).tileEntitySpecialRenderer() != TileEntitySpecialRenderer.class && HxCCore.proxy.getSide() == Side.CLIENT){
                    ClientRegistry.bindTileEntitySpecialRenderer(tileEntity.getClass(), registryClass.getAnnotation(HxCRegistry.class).tileEntitySpecialRenderer().newInstance());
                }
                
                successful = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return successful;
    }
}
