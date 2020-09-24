package bin.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.http.HttpRequest;
import java.util.Iterator;
import java.util.Map;

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
}

