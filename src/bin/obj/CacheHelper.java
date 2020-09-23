package bin.obj;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CacheHelper {
    /**
     * Checks to see if the user's cached version of the requested resource needs to be updated
     * @param filePath path to the requested resource
     * @param ifModifiedSince String from the request header of the client
     * @return 1 if the user's cache is out of date
     *         0 if the user's cached version is fine
     *        -1 if some internal error happens
     */
    public static int cacheIsOk(String filePath, String ifModifiedSince) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
            Date lastModifiedTime = new Date (Files.getLastModifiedTime(Paths.get(filePath)).toMillis());
            Date cacheDate = formatter.parse(ifModifiedSince);

            if (lastModifiedTime.compareTo(cacheDate) > 0) {
                return 1;
            }

            return 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    public static String getLastModifiedDate(String filePath) throws IOException {
        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date(Files.getLastModifiedTime(Paths.get(filePath)).toMillis()));
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z").format(new Date());
    }
}
