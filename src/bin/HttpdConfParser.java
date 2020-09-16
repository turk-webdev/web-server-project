package bin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class HttpdConfParser {

    private HttpdConf httpdConfObj;

    public HttpdConfParser(HttpdConf httpdConfObj){this.httpdConfObj = httpdConfObj;}

    public void parse(InputStream file){
        String currLine, key, value;
        StringTokenizer tokenizer;
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file,"UTF-8"));
            // read the file line by line
            while ((currLine = bufferedReader.readLine()) != null) {
                // Ignore blank lines
                if (currLine.equals("")) {
                    continue;
                }
                tokenizer = new StringTokenizer(currLine);
                String currToken = tokenizer.nextToken();
                // ignore comments
                if(currToken.equals("#") || currToken.charAt(0) == '#'){
                    continue;
                }
                // parsing to handle several exceptions within the httpd.conf file
                if (currToken.equals("ScriptAlias")) {
                    key = tokenizer.nextToken();
                    value = tokenizer.nextToken().replaceAll("^\"|\"$", "");
                    httpdConfObj.putScriptAlias(key, value);
                    //second check is if its just an alias, they are also listed one by one
                }else if (currToken.equals("Alias")){
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
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
