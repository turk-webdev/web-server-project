/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/
package conf;

import conf.configsetup.MimeTypes;

public class WebServer {
    public static void main(String[] args) {
        // TEST ONLY
        MimeTypes mt = new MimeTypes("/Users/turkerdin/Desktop/School/CSC667/web-server-ufkun-adam/src/conf/mime.types");
        mt.execute();
        System.out.println("MIME TYPES HASHMAP\n========================");
        mt.print();
    }
}
