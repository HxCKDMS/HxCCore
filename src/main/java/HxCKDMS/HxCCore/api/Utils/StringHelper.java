package HxCKDMS.HxCCore.api.Utils;

public class StringHelper {
    public static <T> String repeat(T input, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) builder.append(input);
        return builder.toString();
    }
}
