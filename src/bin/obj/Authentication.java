package bin.obj;

import java.util.Base64;
import java.nio.charset.Charset;
import java.security.MessageDigest;

public class Authentication {

    private Htpassword htpasswordObj;
    private Htaccess htaccessObj;

    public Authentication(Htpassword htpasswordObj, Htaccess htaccessObj){
        this.htpasswordObj = htpasswordObj;
        this.htaccessObj = htaccessObj;
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
        String storedPassword = htpasswordObj.get(username);
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
