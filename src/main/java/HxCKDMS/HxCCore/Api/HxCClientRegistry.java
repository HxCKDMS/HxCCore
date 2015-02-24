package HxCKDMS.HxCCore.Api;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SideOnly(Side.CLIENT)
@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HxCClientRegistry {
    EnumHxCRegistryType registryType();
    
    Class<? extends Item> itemForRenderer() default Item.class;

    Class<? extends Block> blockForRenderer() default Block.class;

    Class<? extends TileEntity> tileEntityForRenderer() default TileEntity.class;
}
