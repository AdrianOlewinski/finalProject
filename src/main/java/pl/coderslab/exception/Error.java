package pl.coderslab.exception;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class Error {
    private int httpStatus;
    private String message;
    private long timestamp;
    private String path;

    public Error(int httpStatus, String message, String path) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.timestamp = new Date().getTime();
        this.path = path;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
