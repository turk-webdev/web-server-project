package bin;

import java.io.*;

public abstract class ConfigurationReader {

    protected InputStream configFile;
    protected FileReader fileReader;
    protected BufferedReader bufferedReader;

    public ConfigurationReader(InputStream fileName){
        this.configFile = fileName;
    }

    public abstract void execute();
}

