package HxCKDMS.HxCCore.api.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

    @Retention(RetentionPolicy.RUNTIME)
    @interface ignore {}

    @Retention(RetentionPolicy.RUNTIME)
    @interface category {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface comment {
        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface flags {
        int value() default 0b0;
    }
}
