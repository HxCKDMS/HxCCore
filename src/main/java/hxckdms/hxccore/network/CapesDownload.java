package hxckdms.hxccore.network;

import hxckdms.hxccore.libraries.GlobalVariables;

import java.io.*;
import java.net.URL;

public class CapesDownload implements Runnable {
    @Override
    public void run() {
        try {
            URL capeList = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCLib/master/HxCNewCapes");
            InputStream capeListInputStream = capeList.openStream();
            BufferedReader capeListReader = new BufferedReader(new InputStreamReader(capeListInputStream));

            String line;
            while ((line = capeListReader.readLine()) != null) {
                String uuid = line.split("=")[0];
                String url = line.split("=")[1];
                URL imageURL = new URL(url);

                BufferedInputStream inputStream = new BufferedInputStream(imageURL.openStream());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                int b;
                while ((b = inputStream.read()) != -1) outputStream.write(b);
                GlobalVariables.playerCapes.put(uuid, outputStream.toByteArray());
                outputStream.close();
                inputStream.close();
            }

            capeListReader.close();
            capeListInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
