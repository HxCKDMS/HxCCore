package HxCKDMS.HxCCore.api.Configuration.New.Test;

import HxCKDMS.HxCCore.api.Configuration.New.Exceptions.UnhandledConfigTypeException;
import HxCKDMS.HxCCore.api.Configuration.New.HxCConfig;

import java.io.File;
import java.io.IOException;

public class asdfTest {
    public static void main(String[] args) throws UnhandledConfigTypeException, IOException {
        HxCConfig config = new HxCConfig(ConfTest.class, "testasdf", new File("C://test/"), "cfg");
        config.initConfiguration();
    }
}
