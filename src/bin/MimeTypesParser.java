package bin;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MimeTypesParser {
    private MimeTypes mimeTypesObj;

    public MimeTypesParser(MimeTypes mimeTypesObj) {
        this.mimeTypesObj = mimeTypesObj;
    }

    public void parse(InputStream file) {
        BufferedReader br = new BufferedReader(new InputStreamReader(file));
        try {
            String currLine;

            while ((currLine = br.readLine()) != null) {
                if (currLine.equals("")) continue;  // ignore empty lines
                if (currLine.charAt(0) == '#') continue;    // ignore comments
                StringTokenizer st = new StringTokenizer(currLine);
                String currToken = st.nextToken();
                String type = currToken;    // first token is always the type
                while (st.hasMoreTokens()) {
                    currToken = st.nextToken();
                    mimeTypesObj.put(currToken,type);
                }
            }

            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
