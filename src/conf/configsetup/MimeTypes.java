package conf.configsetup;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.HashMap;

import java.io.FileReader;
import java.io.BufferedReader;

public class MimeTypes {
    private ArrayList<String> extensions;
    private static HashMap<String, ArrayList<String>> mimeTypesTable;
    private static FileReader MIME_TYPES_FILEREADER;

    public MimeTypes() {
        extensions = new ArrayList<>();
        mimeTypesTable = new HashMap<>();

        try {
            MIME_TYPES_FILEREADER = new FileReader("/conf/mime.types");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void parseMimeTypesFile() {

    }
}
