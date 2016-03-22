package HxCKDMS.HxCCore.api.Configuration.New.Test;

import HxCKDMS.HxCCore.api.Configuration.New.HxCConfig;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public class asdfTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchFieldException {
        HxCConfig config = new HxCConfig(ConfTest.class, "testasdf", new File("C://test/"), "cfg");
        config.initConfiguration();

        if (ConfTest.troll) System.out.println(ConfTest.listTest);

        ParameterizedType parameterizedType = (ParameterizedType)ConfTest.class.getField("listTest2").getGenericType();
        System.out.println(parameterizedType.getActualTypeArguments()[0]);
    }
}
