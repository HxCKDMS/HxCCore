package HxCKDMS.HxCCore.api.Configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Integer {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        int minValue() default java.lang.Integer.MIN_VALUE;
        int maxValue() default java.lang.Integer.MAX_VALUE;
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Long {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        long minValue() default java.lang.Long.MIN_VALUE;
        long maxValue() default java.lang.Long.MAX_VALUE;
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Float {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        float minValue() default java.lang.Float.MIN_VALUE;
        float maxValue() default java.lang.Float.MAX_VALUE;
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Double {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        double minValue() default java.lang.Double.MIN_VALUE;
        double maxValue() default java.lang.Double.MAX_VALUE;
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface String {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        java.lang.String[] validValues() default "";
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Boolean {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        boolean forceReset() default false;
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Map {
        java.lang.String category() default "General";
        java.lang.String description() default "";
        boolean forceReset() default false;
    }
}