package com.stemy.mobileandroid.ui.employeeslist;

import android.util.Log;

import com.stemy.mobileandroid.data.ListCallbackGeneric;
import com.stemy.mobileandroid.data.model.AccountUser;
import com.stemy.mobileandroid.type.Role;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class GetAllEmpployeesSuccessInUserView {

    private List<AccountUser> employeeUsers;

    public GetAllEmpployeesSuccessInUserView(List<AccountUser> employeeUsers) {
        this.employeeUsers = employeeUsers;
    }

    public List<AccountUser> getEmployeeUsers() {
        return this.employeeUsers.parallelStream()
                .filter(user -> !user.getRole().rawValue.equals(Role.CUSTOMER.rawValue) && !user.getUserMail().equalsIgnoreCase("removed"))
                .collect(Collectors.toList());
    }


    public AccountUser getEmployeeUsersById(int userId) {
        return this.employeeUsers.parallelStream()
                .filter(user -> !user.getRole().rawValue.equals(Role.CUSTOMER.rawValue) && user.getUserId() == userId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User not found"));    }

    public List<AccountUser> getFilteredEmployees(String query) {
        String lowercaseQuery = query.toLowerCase();
        Log.d("GetAllEmpployeesSuccessInUserView Query", lowercaseQuery );

        List<AccountUser> filteredList = this.employeeUsers.parallelStream()
                .filter(user ->
                        !user.getRole().rawValue.equals(Role.CUSTOMER.rawValue) && !user.getUserMail().equalsIgnoreCase("removed") && (
                        user.getFullName().toLowerCase().contains(lowercaseQuery)
                                || user.getUserMail().toLowerCase().contains(lowercaseQuery) ||
                                user.getRole().rawValue.equalsIgnoreCase(lowercaseQuery) ||
                                user.getStatus().rawValue.equalsIgnoreCase(lowercaseQuery))
                )
                .collect(Collectors.toList());
        Log.d("GetAllEmpployeesSuccessInUserView filter", String.valueOf(filteredList.size()) );
        return filteredList;
    }

}
