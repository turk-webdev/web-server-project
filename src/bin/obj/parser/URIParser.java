/**********************************************************************
 * File: URIParser.java
 * Description: This object is responsible for populating the URIResource
 * object.
 *********************************************************************/

package bin.obj.parser;

import bin.HTTPRequestThread;
import bin.obj.URIResource;

import java.io.File;
import java.util.StringTokenizer;

public class URIParser {
    public void parseURI(String uri, URIResource uriObject, HTTPRequestThread worker) {
        // First, we want to check if the given URI is aliased
        if (worker.aliasContainsKey(uri)) {
            uri = worker.getAlias(uri);
            uriObject.isAliased(true);
        } else if (worker.scriptAliasContainsKey(uri)) {
            uri = worker.getScriptAlias(uri);
            uriObject.isScriptAliased(true);
        } else {
            uri = worker.getHttpd("DocumentRoot") + uri.substring(1);
        }

        // Now that we've got the directory path, we want to parse that into
        // the URIResource for easier reference
        StringTokenizer dirs = new StringTokenizer(uri, "/");

        // First we split the string based on "dirs" with the "/" delim
        // to get each "level"
        while (dirs.hasMoreTokens()) {
            String currDirsToken = dirs.nextToken();

            if (currDirsToken.contains("?")) {
                // If we encounter a "?", then we have params - and thus are at the destination
                StringTokenizer params = new StringTokenizer(currDirsToken,"?&");
                String currParamsToken = params.nextToken();
                uriObject.setDestination(currParamsToken);
                while (params.hasMoreTokens()) {
                    String args[] = params.nextToken().split("=");
                    uriObject.putArgs(args[0],args[1]);
                }
            } else if (!dirs.hasMoreTokens()) {
                // If we've come to the end of the list, we've got our destination
                uriObject.setDestination(currDirsToken);
            } else {
                uriObject.addPath(currDirsToken);
            }
        }

        // If the destination is NOT a file, then we want to get the
        // index file of that directory and set that as the destination
        if (!isFile(uriObject.getDestination())) {
            String dir = uriObject.getPathWithDest();
            String contents[] = new File(dir).list();
            for (String currFile : contents) {
                if (currFile.equals(worker.getHttpd("DirectoryIndex"))) {
                    uriObject.addPath(uriObject.getDestination());
                    uriObject.setDestination(currFile);
                    break;
                }
            }
        }

        // At this point, the URIResource object has the absolute path mapped to it

    }

    public boolean isFile(String name) {
        return name.matches("^[\\w,\\s-#]+\\.[A-Za-z]+$");
    }
}
