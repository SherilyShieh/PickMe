package com.petcity.pickme.common.utils;

/**
 * @ClassName RegexUtils
 * @Description RegexUtils
 * @Author sherily
 * @Date 11/01/21 2:52 PM
 * @Version 1.0
 */
public class RegexUtils {

    public static final String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z\\\\W]{6,20}$";
    public static final String EMAIL_REGEX = "^\\w[-\\w.+]*@([A-Za-z0-9]+\\.)+[A-Za-z]{2,14}$";
    public static final String NZ_PHONE_REGEX = "^[0][2][1579]{1}\\d{6,7}$";

    /**
     * Password must contain numbers and letters, can contain special characters, and have 6 to 20 characters
     *
     * @param str
     * @return
     */
    public static boolean isValidPwd(String str) {
        return str.matches(PASSWORD_REGEX);
    }

    /**
     * Check mailbox format
     *
     * @param str
     * @return
     */
    public static boolean isValidEmail(String str) {
        return str.matches(EMAIL_REGEX);
    }

    /**
     * Check NZ phone number format
     *
     * @param str
     * @return
     */
    public static boolean isValidNZPhone(String str) {
        return str.matches(NZ_PHONE_REGEX);
    }
}
