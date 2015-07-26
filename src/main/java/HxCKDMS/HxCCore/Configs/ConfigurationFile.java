package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;
import scala.actors.threadpool.Arrays;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationFile {
    @Config.Integer(category = "Limits", description = "Don't Exceed 100 without Tinkers or a mod that changes Health Bar.", minValue = 10, maxValue = 2147000000)
    public static int HPMax = 100;

    @Config.Long(category = "Testing", description = "Testing the new blargh config System", minValue = 0, maxValue = 354879342746352789L)
    public static long sdagasdlk = 524363422432346L;

    @Config.String(category = "Testing", description = "Testing the new blargh config System")
    public static String test = "Blargh";

    @Config.Boolean(category = "Testing", description = "Testing the new blargh config System")
    public static boolean asdf = false;

    @Config.List(category = "Testing", description = "Testing the new blargh config System")
    public static List<String> qwerty = Arrays.asList(new String[]{"blarg", "asdf", "test", "niks"});

    @Config.Map(category = "Testing", description = "Testing the new blargh config System")
    public static Map<String, Boolean> zxcv = new LinkedHashMap<>();

    static {
        zxcv.put("asdf", true);
        zxcv.put("fdsa", false);
        zxcv.put("yourMom", false);

    }
}
