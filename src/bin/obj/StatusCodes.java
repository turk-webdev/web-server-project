/**********************************************************************
 * File: StatusCodes.java
 * Description: Enum that will help us manage the various status codes
 * that will be sent back to the client
 *********************************************************************/

package bin.obj;
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
401 Unauthorized    Request requires auth
403 Forbidden
404 Not Found

5xx SERVER ERROR =======================================
500 Internal Server Error
 */
public enum StatusCodes {

}
