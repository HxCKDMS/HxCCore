package hxckdms.hxccore.utilities;

import java.util.Arrays;

public final class MathHelper {
    public static long mean(long[] values) {
        return Arrays.stream(values).parallel().sum() /values.length;
    }
}
