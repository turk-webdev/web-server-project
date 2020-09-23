/**********************************************************************
 * File: URIParser.java
 * Description: This object is responsible for populating the URIResource
 * object.
 *********************************************************************/

package bin.obj.parser;

import bin.HTTPRequestThread;
import bin.obj.URIResource;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;

public class URIParser {
    public void parseURI(String uri, URIResource uriObject, HTTPRequestThread worker) {
        // Build our path data structure
        StringTokenizer dirs = new StringTokenizer(uri, File.separator);

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

        switch (worker.checkAlias(uriObject.getPathToDest())) {
            case -1:
                // Aliased
                uriObject.isAliased(true);
                uri = worker.getAlias(uriObject.getPathToDest()) + uriObject.getDestination();
                break;
            case 1:
                // Script Aliased
                uriObject.isScriptAliased(true);
                uri = worker.getScriptAlias(uriObject.getPathToDest()) + uriObject.getDestination();
                break;
            case 0:
                uri = worker.getHttpd("DocumentRoot") + uri.substring(1);
                break;
        }

        // Now we want to rebuild the path object in the uriObj with the modified uri
        uriObject.clearPath();
        dirs = new StringTokenizer(uri, File.separator);

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
        if (!(new File(uri).isFile())) {
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
}
