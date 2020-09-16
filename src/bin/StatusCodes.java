/**********************************************************************
 * File: StatusCodes.java
 * Description: Enum that will help us manage the various status codes
 * that will be sent back to the client
 *********************************************************************/

package bin;
/*
XXX Label           Description
------------------------------------------------------
2xx SUCCESS ==========================================
200 OK              Success

201 Created         Success, new resource created

204 No Content      Success, does not need to return an entity-body, may want
                    to return updated metainfo

3xx REDIRECTION =======================================
304 Not Modified    If client performed conditional GET, and access allowed,
                    but doc not modified, server responds with this code.
                    Must not contain a message-body, always terminated by first
                    empty line after header fields


4xx CLIENT ERROR ======================================
400 Bad Request     Could not understand client request due to malformed syntax
                    Client should not repeat request without modifications

401 Unauthorized    Request requires user auth, response must include WWW-Authenticate
                    header field. Client may repeat request with suitable Authorization
                    header field. If request included Authorization, then 401 indicates
                    the credentials were refused.

403 Forbidden       Server understood request but is refusing to fulfill - request should
                    not be repeated. If request method was NOT HEAD & server wishes to make
                    it public why the request has not been fulfilled, it should describe reason.
                    If server does not wish to make it public, 404 can be used instead.

404 Not Found       No match found to the requested URI

5xx SERVER ERROR =======================================
500 Internal Server Error   Something happened, we don't know what
*/

import java.util.HashMap;

public class StatusCodes {
    private static HashMap<Integer,String> statusCodes;

    public StatusCodes() {
        statusCodes = new HashMap<>();
        statusCodes.put(200,"Ok");
        statusCodes.put(201,"Created");
        statusCodes.put(204,"No Content");
        statusCodes.put(304,"Not Modified");
        statusCodes.put(400,"Bad Request");
        statusCodes.put(401,"Unauthorized");
        statusCodes.put(403,"Forbidden");
        statusCodes.put(404,"Not Found");
        statusCodes.put(500,"Internal Server Error");
    }

    public String getTitleFromCode(int code) {
        return statusCodes.get(code);
    }

    /**
     * Reverse searches through map to find key from value
     * @param title value to compare
     * @return code on success, -1 if match not found
     */
    public int getCodeFromTitle(String title) {
        for (int key: statusCodes.keySet()) {
            if ((statusCodes.get(key).toUpperCase()).equals(title.toUpperCase())) {
                return key;
            }
        }

        return -1;
    }
}
