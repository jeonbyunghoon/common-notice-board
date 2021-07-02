package io.common.hoony.noticeboard.error;

import lombok.Getter;

@Getter
public class DkargoException extends RuntimeException {

    //http status
    private int status;

    //custom code
    private int code;

    public DkargoException(ErrorCode error) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.code = error.getCode();
    }

    public DkargoException(int status, int code, String message) {
        super(message);
        this.status = status;
        this.code = code;
    }

}
