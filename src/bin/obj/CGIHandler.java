package bin.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpRequest;
import java.util.Iterator;
import java.util.Map;

//this is off the assumption that cgi script kind of breaks the normal work flow
//this step would come in between request handling and response creation
//assume basic verb and uri processing but afterwards
//basic workflow:
//receive verb, processed url/i
//receive a bunch of header variables that are env variables
//hook up stdin for POST requests
//capture output stream of process as string and append it to end of response
//so this should just return a body for the responsemaker to append
//based off test script first line is also the content type header
public class CGIHandler {

    public CGIHandler(){}

    public byte[] handle(URIResource res, HTTPRequest req){
        ProcessBuilder procBuilder = new ProcessBuilder("perl", "-T", res.getFullUri());
        Map<String, String> env = procBuilder.environment();
        for (String key : req.envKeySet()){
            env.put(key, req.get(key));
        }
        if (res.hasQueryString()){
            env.put("QUERY_STRING", res.getQueryString());
        }
        try {
            Process process = procBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                if (line.equals("") || line.equals("\r\n")) continue;
                stringBuilder.append(line);
                stringBuilder.append("\n");
            }
            return stringBuilder.toString().getBytes();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error".getBytes();
    }
/*
    public String handleTest(HTTPRequest req){
        try {
            File testFile = new File("C:/Users/adamj/Documents/GitHub/web-server-ufkun-adam/src/public_html/cgi-bin/perl_env");
            ProcessBuilder procBuilder = new ProcessBuilder("perl", "-T", testFile.getAbsolutePath());
            Map<String, String> env = procBuilder.environment();
            for (String key : req.envKeySet()){
                env.put(key, req.get(key));
            }
            Process process = procBuilder.start();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null){
                stringBuilder.append(line);
                stringBuilder.append(System.getProperty("line.separator"));
                System.out.println(stringBuilder.toString());
            }
            return stringBuilder.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }

    public static void main(String[] args){
        CGIHandler test = new CGIHandler();
        HTTPRequest req =  new HTTPRequest();
        req.put("TESTING", "! @ #");
        System.out.println("output: " + test.handleTest(req));
    }
*/

}

