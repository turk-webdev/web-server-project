/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/
package conf;

import conf.configsetup.HttpdConf;
import conf.configsetup.MimeTypes;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private ServerSocket server;
    private Socket client;
    private int port;

    public static void main(String[] args) throws IOException {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    public void start(){
        this.loadConfigs();
        this.port = Integer.parseInt(this.httpdConf.getHttpdConf("Listen"));

        this.test();
        try{
            server = new ServerSocket(port);
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    public void loadConfigs() {
        // TODO: Use relative pathing for this
        this.mimeTypes = new MimeTypes("conf/mime.types");
        this.httpdConf = new HttpdConf("conf/httpd.conf");

    }

    public void test(){
        System.out.println("MIME TYPES HASHMAP\n========================");
        this.mimeTypes.print();
        String testAlias = "Listen";
        System.out.println("\n" + testAlias + ": " + this.port);

    }

}
