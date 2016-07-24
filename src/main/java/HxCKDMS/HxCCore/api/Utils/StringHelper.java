package HxCKDMS.HxCCore.api.Utils;

public class StringHelper {
    @SuppressWarnings("unchecked")
    public static <T> T repeat(T input, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) builder.append(input);
        return (T) builder.toString();
    }
    //Dafuq? Who made this? What's this for? Why's this exist?
}
