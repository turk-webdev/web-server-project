/**********************************************************************
 * File: MimeTypes.java
 * Description: This class runs once at ANTIPARAZI start up to parse
 * through the mime.types file and
 *********************************************************************/
package bin;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MimeTypes extends ConfigurationReader {

    private static HashMap<String, ArrayList<String>> mimeTypes;
    private static HashMap<String, String> extensions;

    public MimeTypes(InputStream file_name) {
        super(file_name);
        mimeTypes = new HashMap<>();
        extensions = new HashMap<>();
        execute();
    }

    // FOR DEBUGGING PURPOSES ONLY
    public void print() {
        for (String key : mimeTypes.keySet()) {
            System.out.printf("%s: %s\n",key,mimeTypes.get(key));
        }
    }

    /**
     * The execute() function for MimeTypes constructs the static local data
     * structure parsed from the mime.types file in conf.
     */
    @Override
    public void execute() {
        String line, type;
        ArrayList<String> extensions = new ArrayList<>();
        StringTokenizer st;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(configFile,"UTF-8"));

            // Read through each line, skip if it is empty line or has #
            // Tokenize & log away in our local data structures
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) continue;
                st = new StringTokenizer(line);
                String currTok = st.nextToken();

                if (currTok.equals("#")) continue;

                type = currTok;
                while (st.hasMoreTokens()) {
                    currTok = st.nextToken();
                    extensions.add(currTok);
                    this.extensions.put(currTok,type);
                }

                mimeTypes.put(type, (ArrayList) extensions.clone());
                extensions.clear();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param type - MIME type to check in table
     * @return list of extensions tied to that MIME type.
     * Will return an ArrayList of length 0 if no extensions
     */
    public ArrayList<String> getExtensions(String type) {
        return mimeTypes.get(type);
    }

    /**
     * @param type - MIME type to check in table
     * @return true if MIME type does exist
     */
    public boolean checkTypeExists(String type) {
        return mimeTypes.get(type) != null;
    }

    /**
     * @param ext - file extension
     * @return the String value, null if not found
     */
    public String getTypeForExtension(String ext) {
        return extensions.get(ext);
    }
}
