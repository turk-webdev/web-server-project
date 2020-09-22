package auth;

import bin.HTTPRequestThread;
import bin.obj.HTTPRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AuthDriver {
    /**
     * Work through the flow
     * @return 200 if everything went okay, 40x errors if something went wrong
     */


    public int run(HTTPRequest requestObj, String htaccessPath, HTTPRequestThread worker) {
        // TODO: This should prepare the WWW-Authenticate header first
        if (!requestObj.containsKey("Authorization")) {
            return 401;
        }

        String clientAuth = requestObj.get("Authorization");
        Authentication authObj = new Authentication(worker, convertPathToIS(htaccessPath));
        if (!authObj.authCheck(clientAuth)) {
            return 403;
        }

        return 200;
    }

    static InputStream convertPathToIS(String path) {
        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks if the given path is access protected
     * @param path - absolute path to a directory
     * @return true if protected, false if not
     */
    public boolean isProtectedDir(String path, String accessFile) {
        String contents[] = new File(path).list();

        for (String currFile : contents) {
            if (currFile.equals(accessFile)) return true;
        }

        return false;
    }

}
