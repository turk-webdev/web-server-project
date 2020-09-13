package parsers;

import java.io.*;

public abstract class ConfigurationReader {

    protected String configFile;
    protected FileReader fileReader;
    protected BufferedReader bufferedReader;

    public ConfigurationReader(String fileName){
        this.configFile = fileName;
    }

    public abstract void execute();
}

