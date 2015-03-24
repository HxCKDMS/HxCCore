package HxCKDMS.HxCCore.Registry;

import HxCKDMS.HxCCore.Api.EnumHxCRegistryType;
import HxCKDMS.HxCCore.Api.HxCCommonRegistry;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;
import cpw.mods.fml.common.LoaderState;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class CommonModRegistry {
    public static ArrayList<Class<HxCCommonRegistry>> registryClasses = new ArrayList<>();
    public static HashMap<Class, Block> blockRegistry = new HashMap<>();
    public static HashMap<Class, Item> itemRegistry = new HashMap<>();
    
    private static void registerObjects(Set<ASMDataTable.ASMData> asmData){
        if(asmData != null && !asmData.isEmpty()){
            for(final ASMDataTable.ASMData data : asmData){
                String className = data.getClassName();

                try{
                    @SuppressWarnings("unchecked")
                    final Class<HxCCommonRegistry> registryClass = (Class<HxCCommonRegistry>) Class.forName(className);
                    String unlocalizedName = registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName();
                    String type = registryClass.getAnnotation(HxCCommonRegistry.class).registryType().toString().toLowerCase();
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
        
        for (final Class<HxCCommonRegistry> registryClass : registryClasses){
            final String unlocalizedName = registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName();
            String type = registryClass.getAnnotation(HxCCommonRegistry.class).registryType().toString().toLowerCase();
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

    private static boolean preInit(Class<HxCCommonRegistry> registryClass){
        boolean successful = false;
        try{
            EnumHxCRegistryType type = registryClass.getAnnotation(HxCCommonRegistry.class).registryType();
            
            if(type == EnumHxCRegistryType.BLOCK){
                Block block = (Block)registryClass.newInstance();
                block.setBlockName(registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());
                blockRegistry.put(registryClass, block);
                
                if(registryClass.getAnnotation(HxCCommonRegistry.class).itemBlock() != ItemBlock.class){
                    GameRegistry.registerBlock(block, registryClass.getAnnotation(HxCCommonRegistry.class).itemBlock(), registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());
                    successful = true;
                }else{
                    GameRegistry.registerBlock(block, registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());
                    successful = true;
                }

            }else if(type == EnumHxCRegistryType.ITEM){
                Item item = (Item)registryClass.newInstance();
                item.setUnlocalizedName(registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());
                itemRegistry.put(registryClass, item);
                GameRegistry.registerItem(item, registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());

                successful = true;
            }else if(type == EnumHxCRegistryType.TILEENTITY) {
                TileEntity tileEntity = (TileEntity) registryClass.newInstance();
                GameRegistry.registerTileEntity(tileEntity.getClass(), registryClass.getAnnotation(HxCCommonRegistry.class).unlocalizedName());

                successful = true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return successful;
    }
}
