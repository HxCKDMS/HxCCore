package HxCKDMS.HxCCore.api.Configuration.Test;

import HxCKDMS.HxCCore.api.Configuration.HxCConfig;

import java.io.File;
import java.io.IOException;

public class asdfTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException {
        HxCConfig config = new HxCConfig(ConfTest.class, "testasdf", new File("C://test/"), "cfg");
        config.initConfiguration();

        System.out.println(ConfTest.lololol.get("asfd"));
    }
}
