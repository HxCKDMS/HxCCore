package hxckdms.hxccore.network;

import hxckdms.hxccore.libraries.GlobalVariables;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class CodersCheck implements Runnable {

    @Override
    public void run() {
        try {
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCLib.txt");
            InputStream inputStream = url.openStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                if (inputLine.startsWith("//")) continue;

                String[] textParts = inputLine.split(":");
                GlobalVariables.devTags.put(UUID.fromString(textParts[1]), textParts[0]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
