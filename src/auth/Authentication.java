package auth;

import auth.Htaccess;
import auth.Htpassword;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;
import bin.obj.HTTPResponse;
import bin.obj.parser.HtaccessParser;
import bin.obj.parser.HtpasswordParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Authentication {

    private Htaccess htaccessObj = new Htaccess();
    private Htpassword htpasswordObj = new Htpassword();
    private HTTPResponse responseObj;
    private HTTPRequest requestObj;

    public Authentication(String htaccessPath) {
        HtaccessParser htaccessParser = new HtaccessParser(htaccessObj);
        HtpasswordParser htpasswordParser = new HtpasswordParser(htpasswordObj);
        htaccessParser.parse(convertPathToIS(htaccessPath));
        htpasswordParser.parse(convertPathToIS(htaccessObj.get("AuthUserFile")));
    }

    public int run(HTTPResponse responseObj, HTTPRequest requestObj) {
        // If the request did not have Authorization header, 401 and challenge client
        if (!requestObj.containsKey("Authorization")) {
            responseObj.setStatusCode(401);
            StringBuilder value = new StringBuilder();
            value.append(htaccessObj.get("AuthType")).append(" ");
            value.append("realm=").append(htaccessObj.get("AuthName")).append(", ");
            value.append("charset=\"UTF-8\"");
            responseObj.putResponseHeader("WWW-Authenticate",value.toString());
            return 401;
        }

        // Otherwise, check to see if client is forbidden & send 403
        if (!authCheck(requestObj.get("Authorization"))) {
            responseObj.setStatusCode(403);
            String html = "<html>\n<body>\n<h1>403 Forbidden</h1>\n</body>\n</html>";
            byte[] data = html.getBytes();
            responseObj.setBody(data);
            responseObj.putResponseHeader("Content-Length",Integer.toString(data.length));
            responseObj.putResponseHeader("Content-Type", "text/html");
            return 403;
        }

        // At this point, everything is a-ok
        return 200;
    }

    public boolean authCheck(String auth){
        String info = decryptAuth(auth);
        String[] tokens = info.split(":");
        if (tokens.length == 2){
            return verify(tokens[0], tokens[1]);
        }
        return false;
    }

    private String decryptAuth(String auth){
        String[] tokens = auth.split(" ");
        String info = new String(Base64.getDecoder().decode(tokens[1]), Charset.forName("UTF-8"));
        return info;
    }

    private boolean verify(String username, String password){
        String givenPassword = encryptClearPassword(password);
        String storedPassword = htpasswordObj.get(username);
        if (givenPassword.equals(storedPassword)){
            return true;
        }
        return false;
    }

    private String encryptClearPassword(String password) {
        // Encrypt the cleartext password (that was decoded from the Base64 String
        // provided by the client) using the SHA-1 encryption algorithm
        try {
            MessageDigest mDigest = MessageDigest.getInstance( "SHA-1" );
            byte[] result = mDigest.digest( password.getBytes() );

            return Base64.getEncoder().encodeToString( result );
        } catch( Exception e ) {
            return "";
        }
    }

    public boolean isProtectedDir(String path, String accessFile) {
        String contents[] = new File(path).list();

        for (String currFile : contents) {
            if (currFile.equals(accessFile)) return true;
        }

        return false;
    }

    InputStream convertPathToIS(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
