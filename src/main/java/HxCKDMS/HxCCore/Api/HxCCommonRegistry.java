package HxCKDMS.HxCCore.Api;

import net.minecraft.item.ItemBlock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SuppressWarnings("unused")
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HxCCommonRegistry {
    String unlocalizedName();
    
    EnumHxCRegistryType registryType();
    
    Class<? extends ItemBlock> itemBlock() default ItemBlock.class;
}
