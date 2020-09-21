package auth;

import bin.HTTPRequestThread;
import bin.obj.parser.HtaccessParser;

import java.io.InputStream;
import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Authentication {

    private HTTPRequestThread worker;

    public Authentication(HTTPRequestThread worker, InputStream file){
        this.worker = worker;
        Htaccess htaccessObj = new Htaccess();
        HtaccessParser htaccessParser = new HtaccessParser(htaccessObj);
        htaccessParser.parse(file);
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
        String info = new String(Base64.getDecoder().decode(auth), Charset.forName("UTF-8"));
        return info;
    }

    private boolean verify(String username, String password){
        String givenPassword = encryptClearPassword(password);
        String storedPassword = worker.getHtpassword(username);
        if (givenPassword == storedPassword){
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
}
