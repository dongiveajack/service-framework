package org.trips.service_framework.codes;

/**
 * Created By Abhinav Tripathi
 */
public enum SuccessCodes implements StatusCode {
    DATA_RETRIEVED_SUCCESSFULLY(101, "Data retrieved Successfully"),
    APP_CONFIG_CREATED_SUCCESSFULLY(102, "App config created successfully"),
    CREATED(201, "Created Successfully"),
    OK(200, "Success");

    Integer code;
    String message;

    SuccessCodes(Integer code, String message) {
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
