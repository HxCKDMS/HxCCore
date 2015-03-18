package HxCKDMS.HxCCore.Contributors;

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
        loadCoders();
        loadSupporters();
        loadHelpers();
        loadArtists();
    }

    public void loadCoders(){
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

    public void loadSupporters(){
        try{
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCCore/master/supporters.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null){
                HxCCore.supporters.add(UUID.fromString(inputLine));
            }


        }catch(MalformedURLException exception){
            LogHelper.error("Can not resolve supporters.txt", References.MOD_NAME);
            exception.printStackTrace();
        }catch(IOException exception){
            LogHelper.error("Can not read supporters.txt", References.MOD_NAME);
        }
    }

    public void loadHelpers(){
        try{
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCCore/master/helpers.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null){
                HxCCore.helpers.add(UUID.fromString(inputLine));
            }


        }catch(MalformedURLException exception){
            LogHelper.error("Can not resolve helpers.txt", References.MOD_NAME);
            exception.printStackTrace();
        }catch(IOException exception){
            LogHelper.error("Can not read helpers.txt", References.MOD_NAME);
        }
    }

    public void loadArtists(){
        try{
            URL url = new URL("https://raw.githubusercontent.com/HxCKDMS/HxCCore/master/artists.txt");
            InputStream inputStream = url.openStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String inputLine;
            while((inputLine = bufferedReader.readLine()) != null){
                HxCCore.artists.add(UUID.fromString(inputLine));
            }


        }catch(MalformedURLException exception){
            LogHelper.error("Can not resolve artists.txt", References.MOD_NAME);
            exception.printStackTrace();
        }catch(IOException exception){
            LogHelper.error("Can not read artists.txt", References.MOD_NAME);
        }
    }
}
