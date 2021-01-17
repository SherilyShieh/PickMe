package com.petcity.pickme.data.remote;

/**
 * @ClassName ResultException
 * @Description TODO
 * @Author sherily
 * @Date 9/01/21 1:17 PM
 * @Version 1.0
 */
public class ResultException extends Exception {

    public int errorCode;


    public ResultException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
