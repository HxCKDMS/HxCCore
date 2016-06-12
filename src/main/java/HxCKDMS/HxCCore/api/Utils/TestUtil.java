package HxCKDMS.HxCCore.api.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TestUtil {
    public String readLangOnServer(String mod, String line) {
        try {
            BufferedReader readIn = new BufferedReader(new InputStreamReader(getClass().getClassLoader()
                    .getResourceAsStream("assets/" + mod.toLowerCase() + "/lang/en_US.lang"), "UTF-8"));
            final String[] returnVal = {"Error"};
            readIn.lines().forEach(l -> {
                if (l.contains(line)) {
                    returnVal[0] = l.replaceFirst((line + "="), "");
                }
            });
            readIn.close();
            return returnVal[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
