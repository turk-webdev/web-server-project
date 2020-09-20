package auth;

import java.io.File;

public class AuthDriver {
    /**
     * Work through the flow
     * @return 200 if everything went okay, 40x errors if something went wrong
     */
    public int run() {
        return 200;
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
