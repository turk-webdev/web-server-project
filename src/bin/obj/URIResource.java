/**********************************************************************
 * File: URIResource.java
 * Description: This object is responsible for storing the final URI
 * to be referenced throughout the request/response workflow.
 *********************************************************************/

package bin.obj;

import bin.HTTPRequestThread;

import java.io.File;
import java.util.*;

public class URIResource {
    private HashMap<String, String> args;
    private ArrayList<String> path;
    private String destination;
    private boolean isAliased, isScriptAliased;

    public URIResource() {
        args = new HashMap<>();
        path = new ArrayList<>();
        destination = "";
        isAliased = false;
        isScriptAliased = false;
    }

    // Setters & Builders
    public void setDestination(String destination) { this.destination = destination; }
    public String getDestination() { return destination; }
    public void putArgs(String key, String val) { args.put(key,val); }
    public void addPath(String token) { path.add(token); }
    public void addPath(int i, String token) { path.add(i, token); }

    public String getPathToDest() {
        StringBuilder re = new StringBuilder();

        for (String curr : path) {
            re.append(File.separator).append(curr);
        }

        re.append(File.separator);

        return re.toString();
    }

    public String getPathWithDest() {
        StringBuilder re = new StringBuilder();

        for (String curr : path) {
            re.append(File.separator).append(curr);
        }

        re.append(File.separator).append(destination);

        return re.toString();
    }

    public String getFullUri() {
        StringBuilder uri = new StringBuilder();

        // Add each level in the path, then add the destination
        for (String curr : path) uri.append(File.separator).append(curr);
        uri.append(File.separator).append(destination);

        // If we have any params/args, add those too
        if (args.size() > 0) {
            uri.append("?");

            // There is a more elegant solution using Iterator, but I am too lazy/burnt right now - come back to it
            int iterations = 0;
            for (String key : args.keySet()) {
                if (iterations == 0) {
                    uri.append(key).append("=").append(args.get(key));
                } else {
                    uri.append("&").append(key).append("=").append(args.get(key));
                }

                iterations++;
            }
        }

        return uri.toString();
    }

    // Public methods
    public void clearPath() { path.clear(); }
    public void isAliased(boolean isAliased) { this.isAliased = isAliased; }
    public void isScriptAliased(boolean isScriptAliased) { this.isScriptAliased = isScriptAliased; }
    public boolean isAliased() { return isAliased; }
    public boolean isScriptAliased() { return isScriptAliased; }
    public String getFileExt() {
        if (destination.contains(".")) {
            String tokens[] = destination.split("\\.");
            return tokens[1];
        }

        return "";
    }
    public boolean checkFileExists() {
        String contents[] = new File(getPathToDest()).list();

        for (String currFile : contents) {
            if (currFile.equals(destination)) return true;
        }

        return false;
    }

    public String getQueryString() {
        StringBuilder re = new StringBuilder();
        re.append("?");

        for (String key : args.keySet()) {
            re.append(key).append("=").append(args.get(key)).append("&");
        }

        // Remove the extra "&" at the end
        if (re.length() > 0) {
            re.setLength(re.length()-1);
        }

        return re.toString();
    }

    public boolean hasQueryString() { return args.size() > 0; }

}
