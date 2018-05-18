package com.iicsdms.tris.iicsdms2;

/**
 * Created by Tris on 26/03/2018.
 */

class LoginPojo {

    private String status;
    private String email;
    private String fullName;
    private String userType;
    private String dept;

    String getLogLoginLink() {
        return logLoginLink;
    }

    void setLogLoginLink(String logLoginLink) {
        this.logLoginLink = logLoginLink;
    }

    private String logLoginLink;

    String getStatus() {
        return status;
    }

    void setStatus(String status) {
        this.status = status;
    }

    String getEmail() {
        return email;
    }

    void setEmail(String email) {
        this.email = email;
    }

    String getFullName() {
        return fullName;
    }

    void setFullName(String fullName) {
        this.fullName = fullName;
    }

    String getUserType() {
        return userType;
    }

    void setUserType(String userType) {
        this.userType = userType;
    }

    String getDept() {
        return dept;
    }

    void setDept(String dept) {
        this.dept = dept;
    }
}
