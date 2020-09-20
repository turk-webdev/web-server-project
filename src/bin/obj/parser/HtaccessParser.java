package bin.obj.parser;

import auth.Htaccess;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtaccessParser {

    private Htaccess htaccessObj;

    public HtaccessParser(Htaccess htaccessObj){this.htaccessObj = htaccessObj;}


    public void parse(InputStream file) {
        String currentLine;
        String[] tokens;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            while((currentLine = bufferedReader.readLine()) != null){
                tokens = currentLine.split(" ");
                if (tokens.length == 2){
                    htaccessObj.put(tokens[0], tokens[1].replace("\"", "").trim());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
