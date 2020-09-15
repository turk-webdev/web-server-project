/**********************************************************************
 * File: WebServer.java
 * Description: This is the main class for our ANTIPARAZI web server
 *********************************************************************/

import bin.RequestHandler;
import bin.HttpdConf;
import bin.MimeTypes;
import bin.obj.HTTPRequest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebServer {
    private HttpdConf httpdConf;
    private MimeTypes mimeTypes;
    private ServerSocket server;
    private Socket client;
    private int port;
    private final int MAX_NUM_THREADS = 32;  // Max num threads on a quad core CPU

    public static void main(String[] args) {
        WebServer web_server = new WebServer();
        web_server.start();
    }

    private void start(){
        loadConfigs();
        port = Integer.parseInt(httpdConf.getHttpdConf("Listen"));

        Executor service = Executors.newFixedThreadPool(MAX_NUM_THREADS);

        try{
            server = new ServerSocket(port);
            while(true){
                //todo: this is where threading is going to go, threads/Workers.
                client = server.accept();
                String request = requestToString(client.getInputStream());

                // If we receive a connection from a client, create a new thread
                // to listen for and process their request
                if (client != null) service.execute(new HTTPRequest(request));
            }
        } catch (IOException e){
            System.out.println("IOException");
        }
    }

    private String requestToString(InputStream client) {
        String request = "", currLine;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(client));
            while ((currLine = br.readLine()) != null) {
                request += currLine;
            }
        } catch (Exception e) {
            // TODO: Return 400 error
            e.printStackTrace();
            return "ERROR";
        }

        return request;
    }

    private void loadConfigs() {
        // TODO: Use relative pathing for this
        InputStream mimeTypesIS = getClass().getClassLoader().getResourceAsStream("conf/mime.types");
        InputStream httpdConfIS = getClass().getClassLoader().getResourceAsStream("conf/httpd.conf");

        this.mimeTypes = new MimeTypes(mimeTypesIS);
        this.httpdConf = new HttpdConf(httpdConfIS);

    }

    private void test(){
        System.out.println("MIME TYPES HASHMAP\n========================");
        mimeTypes.print();
        String testAlias = "Listen";
        System.out.println("\n" + testAlias + ": " + port);

    }

}
