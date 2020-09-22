package bin.obj.parser;

import auth.Htaccess;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HtaccessParser {

    private Htaccess htaccessObj;

    public HtaccessParser(Htaccess htaccessObj) {this.htaccessObj = htaccessObj;}


    public void parse(InputStream file) {
        String currentLine;
        String[] tokens;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file, "UTF-8"));
            while((currentLine = bufferedReader.readLine()) != null){
                tokens = currentLine.split(" ");
                if (tokens.length == 2){
                    tokens[1] = tokens[1].replace("\"", "").trim();
                    htaccessObj.put(tokens[0], tokens[1]);
                } else if (tokens.length > 2) {
                    String val = "";

                    for (int i=1; i<tokens.length; i++) {
                        val = String.join(" ", val, tokens[i]);
                    }

                    htaccessObj.put(tokens[0], val);
                } else {
                    htaccessObj.put(tokens[0], getDefaultValue(tokens[0]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDefaultValue(String key) {
        switch (key) {
            case "AuthType":
                return "Basic";
            case "AuthName":
                return "Page requires access";
            case "Require":
                return "valid-user";
            default:
                return "";
        }
    }
}
