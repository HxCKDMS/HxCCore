package HxCKDMS.HxCCore.Contributers;

import HxCKDMS.HxCCore.HxCCore;
import HxCKDMS.HxCCore.Utils.LogHelper;
import HxCKDMS.HxCCore.lib.References;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class CodersCheck implements Runnable {
    @Override
    public void run() {
        try{
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCCore/master/coders.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null){
                HxCCore.coders.add(UUID.fromString(inputLine));
            }


        }catch(MalformedURLException exception){
            LogHelper.error("Can not resolve coders.txt", References.MOD_NAME);
            exception.printStackTrace();
        }catch(IOException exception){
            LogHelper.error("Can not read coders.txt", References.MOD_NAME);
        }
    }
}
