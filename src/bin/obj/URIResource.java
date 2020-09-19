package bin.obj;

import bin.HttpdConf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class URIResource {
    private String originalPath;
    private ArrayList<String> modifiedUri = new ArrayList<>();
    private HttpdConf httpdConf;
    private boolean isScriptAliased = false;

    public URIResource(String originalPath, HttpdConf httpdConf) {
        this.originalPath = originalPath;
        this.httpdConf = httpdConf;
    }

    public void run() {
        // First resolve if the path is aliased, script aliased, or neither
        // if neither, append DOC_ROOT
        if (isAliased(originalPath)) {
            originalPath = httpdConf.getAlias(originalPath);
        } else if (isScriptAliased(originalPath)) {
            originalPath = httpdConf.getScriptAlias(originalPath);
        } else {
            originalPath = httpdConf.getHttpd("DocumentRoot") + originalPath;
        }

        // Parse it into our arraylist for easier checks
        parsePath(originalPath);
        if (!isFile()) {
            try {
                String contents[] = new File(getModifiedUriString()).list();
                for (String currFile : contents) {
                    if (currFile.contains("index")) modifiedUri.add(currFile);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public String getModifiedUriString() {
        String re = "";
        for (String curr : modifiedUri) {
            re += "/" + curr;
        }
        return re;
    }

    public boolean uriContains(String uri, String contains, boolean isFile) {
        // If the uri is a path to a file, we want to get the uri to just be the parent dir
        if (isFile) {
            ArrayList<String> tmp = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(uri,"/");
            while (st.hasMoreTokens())tmp.add(st.nextToken());

            uri = "";
            for (int i=0; i<tmp.size()-1; i++) {
                uri += "/" + tmp.get(i);
            }
        }

        // Query into the uri - if there is a match of the contains string, the file exists
        String contents[] = new File(uri).list();
        for (String currFile : contents) {
            if (currFile.contains(contains)) return true;
        }

        return false;
    }

    private void parsePath(String path) {
        modifiedUri.clear();
        StringTokenizer st = new StringTokenizer(path,"/");
        while (st.hasMoreTokens())  modifiedUri.add(st.nextToken());
    }

    private boolean isAliased(String path) {
        return httpdConf.aliasContainsKey(path);
    }

    private boolean isScriptAliased(String path) {
        isScriptAliased = httpdConf.scriptAliasContainsKey(path);
        return isScriptAliased;
    }

    public boolean isScriptAliased() {
        return isScriptAliased;
    }

    public boolean isFile() {
        return modifiedUri.get(modifiedUri.size()-1)
                .matches("\\w+\\.\\w+|\\W+\\.\\w+");
    }



}
