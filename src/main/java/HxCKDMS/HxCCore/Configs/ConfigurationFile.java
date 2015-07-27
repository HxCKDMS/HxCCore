package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationFile {
    @Config.Integer(description = "Testing this pile of shit")
    public static int test  = 4325;
    @Config.String(description = "Testing this pile of shit")
    public static String test2 = "test";
    @Config.List(description = "Testing this pile of shit")
    public static ArrayList<Integer> test5 = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
    @Config.Boolean(description = "Testing this pile of shit")
    public static Boolean test3 = false;
    @Config.Long(description = "Testing this pile of shit")
    public static Long test4 = 532138543264L;
    @Config.Map(description = "Testing this pile of shit")
    public static Map<String, Integer> test6 = new HashMap<>();

    static {
        test6.put("test1", 1);
        test6.put("test2", 2);
        test6.put("test3", 3);
        test6.put("test4", 4);
        test6.put("test5", 5);
        test6.put("test6", 6);
    }
}
