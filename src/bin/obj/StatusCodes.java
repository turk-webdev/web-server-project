/**********************************************************************
 * File: StatusCodes.java
 * Description: Dictionary that will help us manage the various status
 * codes that will be sent back to the client
 *********************************************************************/

package bin.obj;

import java.util.HashMap;

public class StatusCodes {
    private static HashMap<Integer,String> statusCodes;

    static {
        statusCodes = new HashMap<>();
        statusCodes.put(200,"OK");
        statusCodes.put(201,"Created");
        statusCodes.put(204,"No Content");
        statusCodes.put(304,"Not Modified");
        statusCodes.put(400,"Bad Request");
        statusCodes.put(401,"Unauthorized");
        statusCodes.put(403,"Forbidden");
        statusCodes.put(404,"Not Found");
        statusCodes.put(500,"Internal Server Error");
    }

    public static String getTitleFromCode(int code) {
        return statusCodes.get(code);
    }

    /**
     * Reverse searches through map to find key from value
     * @param title value to compare
     * @return code on success, -1 if match not found
     */
    public static int getCodeFromTitle(String title) {
        for (int key: statusCodes.keySet()) {
            if ((statusCodes.get(key).toUpperCase()).equals(title.toUpperCase())) {
                return key;
            }
        }

        return -1;
    }
}
