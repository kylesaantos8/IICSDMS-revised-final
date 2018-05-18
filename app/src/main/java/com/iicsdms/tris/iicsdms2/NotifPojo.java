package com.iicsdms.tris.iicsdms2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tris on 03/03/2018.
 */

public class NotifPojo implements Parcelable {

    private String mailId, type, otherRecipient, subject, iso, message, senderName, sentBy, dateCreated, time;

    private String schoolYear, department, ack, timeAck;

    private String email, fullName, userType, dept, notifEmail;

    private int counter;

    String notifId, notifPage, notifDescription, notifTimeStamp, notifFlag;
    String notifThread, notifSourceRecipient;

    public NotifPojo(String notifId, String notifPage, String notifDescription, String notifTimeStamp, String notifFlag){

        this.notifId = notifId;
        this.notifPage = notifPage;
        this.notifDescription = notifDescription;
        this.notifTimeStamp = notifTimeStamp;
        this.notifFlag = notifFlag;
        }

     public NotifPojo(Parcel in) {
        this.senderName = in.readString();
        this.iso = in.readString();
        this.subject = in.readString();
        this.message = in.readString();
        this.time = in.readString();
    }

    public NotifPojo(String subject, String senderName, String type, String dateCreated, String mailId, String iso, String schoolYear,
                     String department, String ack, String sentBy) {
        this.subject = subject;
        this.senderName = senderName;
        this.type = type;
        this.dateCreated = dateCreated;
        this.mailId = mailId;
        this.iso = iso;
        this.schoolYear = schoolYear;
        this.department = department;
        this.ack = ack;
        this.sentBy = sentBy;
    }

    public NotifPojo(String subject, String senderName, String sentBy, String type, String dateCreated, String mailId, String iso, String schoolYear) {
        this.subject = subject;
        this.senderName = senderName;
        this.sentBy = sentBy;
        this.type = type;
        this.dateCreated = dateCreated;
        this.mailId = mailId;
        this.iso = iso;
        this.schoolYear = schoolYear;
    }

    public NotifPojo() {

    }

    public String getNotifEmail() {
        return notifEmail;
    }

    public void setNotifEmail(String notifEmail) {
        this.notifEmail = notifEmail;
    }

    public String getNotifThread() {
        return notifThread;
    }

    public void setNotifThread(String notifThread) {
        this.notifThread = notifThread;
    }

    public String getNotifSourceRecipient() {
        return notifSourceRecipient;
    }

    public void setNotifSourceRecipient(String notifSourceRecipient) {
        this.notifSourceRecipient = notifSourceRecipient;
    }

    public String getNotifId() {
        return notifId;
    }

    public void setNotifId(String notifId) {
        this.notifId = notifId;
    }

    public String getNotifPage() {
        return notifPage;
    }

    public void setNotifPage(String notifPage) {
        this.notifPage = notifPage;
    }

    public String getNotifDescription() {
        return notifDescription;
    }

    public void setNotifDescription(String notifDescription) {
        this.notifDescription = notifDescription;
    }

    public String getNotifTimeStamp() {
        return notifTimeStamp;
    }

    public void setNotifTimeStamp(String notifTimeStamp) {
        this.notifTimeStamp = notifTimeStamp;
    }

    public String getNotifFlag() {
        return notifFlag;
    }

    public void setNotifFlag(String notifFlag) {
        this.notifFlag = notifFlag;
    }

    //    ----------------------------------------------------------------------------------------------
//    ----------------------------------------------------------------------------------------------
//    ----------------------------------------------------------------------------------------------

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.senderName);
        parcel.writeString(this.iso);
        parcel.writeString(this.subject);
        parcel.writeString(this.message);
        parcel.writeString(this.time);
    }
    public static final Creator<NotifPojo> CREATOR = new Creator<NotifPojo>() {
        @Override
        public NotifPojo createFromParcel(Parcel in) {
            return new NotifPojo(in);
        }
        @Override
        public NotifPojo[] newArray(int size) {
            return new NotifPojo[size];
        }
    };
}

