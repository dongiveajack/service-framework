package org.trips.service_framework.codes;

/**
 * Created By Abhinav Tripathi
 */
public enum ErrorCodes implements StatusCode {
    GENERIC_ERROR_OCCURRED(101, "Error Occurred!"),
    NOT_FOUND(102, "Data not found"),
    BAD_REQUEST(400, "Internal Server Error");
    Integer code;
    String message;

    ErrorCodes(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
