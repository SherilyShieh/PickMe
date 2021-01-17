package com.petcity.pickme.base;

/**
 * @ClassName Status
 * @Description: Status of a resource that is provided to the UI.
 * <p>
 * These are usually created by the Repository classes where they return
 * {@code LiveData<Resource<T>>} to pass back the latest data to the UI with its fetch status.
 * @Author sherily
 * @Date 9/01/21 1:34 PM
 * @Version 1.0
 */
public enum Status {
    SUCCESS,
    ERROR,
    LOADING
}
