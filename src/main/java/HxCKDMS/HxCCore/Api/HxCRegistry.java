package HxCKDMS.HxCCore.Api;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.IItemRenderer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HxCRegistry {
    String unlocalizedName();
    
    EnumHxCRegistryType registryType();
    
    Class<? extends ItemBlock> itemBlock() default ItemBlock.class;
    
    Class<? extends TileEntitySpecialRenderer> tileEntitySpecialRenderer() default TileEntitySpecialRenderer.class;
    
    Class<? extends IItemRenderer> itemRenderer() default IItemRenderer.class;
}
