package HxCKDMS.HxCCore.Configs;

import HxCKDMS.HxCCore.api.Configuration.Config;

public class ConfigurationFile {
    @Config.Integer(description = "Testing this pile of shit")
    public static int test = 4325;
    @Config.String(description = "Testing this pile of shit")
    public static String test2 = "test";
    @Config.Boolean(description = "Testing this pile of shit")
    public static Boolean test3 = false;
    @Config.Long(description = "Testing this pile of shit")
    public static Long test4 = 532138543264L;
}
