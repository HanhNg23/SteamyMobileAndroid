package com.stemy.mobileandroid.ui.employee.ValidatorViewModelDefine;

import android.util.Patterns;

import com.stemy.mobileandroid.type.Role;
import com.stemy.mobileandroid.type.UserStatus;

public class AccountUserValidator {
    private static String messageError = "";

    public static String getMessageError(){
        return messageError;
    }

    public static boolean isEmailValid(String email) {
        if (email == null || email.trim().isEmpty() ) {
            messageError = "Email must be filled !";
            return false;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            messageError = "The email address must contain the symbol @";
            return false;
        }

            return true;

    }

    public static boolean isFullNameValid(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            messageError = "Fullname must be filled !";
            return false;
        }
           return true;

    }

    public static boolean isAccountRoleValid(Role role) {
        if (role == null || role.toString().trim().isEmpty()) {
            messageError = "Role must be selected !";
            return false;
        }
            return true;

    }

    public static boolean isAccountStatusValid(UserStatus status) {
        if (status == null || status.toString().trim().isEmpty()) {
            messageError = "Status account must be selected !";
            return false;
        }
            return true;

    }

    public static boolean isPhoneValid(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            messageError = "Phone must be filled !";
            return false;
        }else if(!phone.matches("^\\+?[0-9]{7,15}$")){
            messageError = "The phone is incorrect format";
            return false;
        }
            return true;

    }
}
