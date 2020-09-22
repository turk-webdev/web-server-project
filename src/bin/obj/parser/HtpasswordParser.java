package bin.obj.parser;

import auth.Htpassword;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtpasswordParser {

    private Htpassword htpasswordObj;

    public HtpasswordParser(Htpassword htpasswordObj) { this.htpasswordObj = htpasswordObj; }

    public void parse(InputStream file) {
        String currentLine;
        String[] tokens;
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            while((currentLine = bufferedReader.readLine()) != null){
                tokens = currentLine.split(":");
                if (tokens.length == 2){
                    htpasswordObj.put(tokens[0], tokens[1].replace("{SHA}", "").trim());
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
