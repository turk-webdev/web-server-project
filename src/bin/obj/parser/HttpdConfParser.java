package bin.obj.parser;

import bin.obj.HttpdConf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HttpdConfParser {
    private enum Defaults {
        AccessFile(".htaccess"),
        DirIndex("index.html"),
        Listen("8080");

        public final String value;

        Defaults(String value) {
            this.value = value;
        }
    }

    private HttpdConf httpdConfObj;

    public HttpdConfParser(HttpdConf httpdConfObj){this.httpdConfObj = httpdConfObj;}

    public void parse(InputStream file){
        String currLine, key, value;
        StringTokenizer tokenizer;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file,"UTF-8"));
            // read the file line by line
            while ((currLine = bufferedReader.readLine()) != null) {
                tokenizer = new StringTokenizer(currLine);
                String currToken = tokenizer.nextToken();

                // Ignore comments & blank lines
                if(currLine.equals("") || currToken.charAt(0) == '#'){
                    continue;
                }

                // parsing to handle several exceptions within the httpd.conf file
                if (currToken.equals("ScriptAlias")) {
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    httpdConfObj.putScriptAlias(key, value);
                    //second check is if its just an alias, they are also listed one by one
                } else if (currToken.equals("Alias")){
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    httpdConfObj.putAlias(key, value);
                    //third check is for Directory Index, special handling involved for multipled listed files
                } else if (currToken.equals("DirectoryIndex")){
                    key = currToken;
                    while(tokenizer.hasMoreTokens()){
                        value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                        httpdConfObj.putHttpd(key, value);
                    }
                    //if its none of those, then it goes into the httpd list
                } else {
                    key = currToken;
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    httpdConfObj.putHttpd(key, value);
                }
            }
            checkDefaults();
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkDefaults() {
        for (Defaults e : Defaults.values()) {
            if (!httpdConfObj.httpdContainsKey(e.toString())) {
                httpdConfObj.putHttpd(e.toString(),e.value);
            } else if (httpdConfObj.getHttpd(e.toString()) == null ||
                    httpdConfObj.getHttpd(e.toString()) == "") {
                httpdConfObj.putHttpd(e.toString(),e.value);
            }
        }
    }
}
