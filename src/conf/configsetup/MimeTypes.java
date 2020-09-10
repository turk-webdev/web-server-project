package conf.configsetup;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MimeTypes extends ConfigurationReader {

    private static HashMap<String, ArrayList<String>> mimeTypes;

    public MimeTypes(String file_name) {
        super(file_name);
        mimeTypes = new HashMap<>();
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
            file_reader = new FileReader(config_file);
            buffer_reader = new BufferedReader(file_reader);

            while ((line = buffer_reader.readLine()) != null) {
                if (line.equals("")) continue;
                st = new StringTokenizer(line);
                String currTok = st.nextToken();

                if (currTok.equals("#")) continue;

                type = currTok;
                while (st.hasMoreTokens()) {
                    currTok = st.nextToken();
                    extensions.add(currTok);
                }

                mimeTypes.put(type, (ArrayList) extensions.clone());
                extensions.clear();
            }
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

    public String getTypeForExtension() {
        // TODO: Implement reverse lookup
        return "";
    }
}
