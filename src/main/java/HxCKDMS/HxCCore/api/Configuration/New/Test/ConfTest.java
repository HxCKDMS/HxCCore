package HxCKDMS.HxCCore.api.Configuration.New.Test;

import HxCKDMS.HxCCore.api.Configuration.New.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Config
public class ConfTest {
    @Config.category("29")
    public static String lol = "525324";
    public static String test = "gasdva";
    public static String asdf = "5253fdsavas24";
    public static String hrtesd = "\u00a7asdf";
    @Config.category("test")
    public static String zxcv = "\u534D";

    public static int asdvasd = 45;
    @Config.category("asdf")
    @Config.comment("This is an Integer!")
    public static Integer lolasdf = 59;
    public static Double asfasdffdsa = 45.12;
    public static double asdfasdfasdf = 5123.5235;
    public static boolean troll = false;

    public static List<String> listTest = new ArrayList(){{
        add("test");
        add("lol");
    }};

    public static List<Integer> listTest2 = new ArrayList(){{
        add(1);
        add(2);
        add(3);
    }};

    public static Map<String, Integer> lololol = new HashMap(){{
        put("test",5);
        put("lol",45);
    }};
}
