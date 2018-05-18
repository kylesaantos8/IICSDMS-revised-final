package com.iicsdms.tris.iicsdms2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tris on 03/03/2018.
 */

public class InvitePojo implements Parcelable {

    private String eventId;
    private String eventTitle;
    private String eventLocation;
    private String eventAllDay;
    private String eventStart;
    private String eventEnd;
    private String eventDescription;
    private String eventCreatedBy;
    private String eventStatus;
    private String eventResponse;
    private String eventDateResponse;


    private String fullName, type, dept;


    public InvitePojo (String eventTitle, String eventLocation, String eventStart, String eventEnd, String eventDescription, String eventResponse, String eventDateResponse){
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventDescription = eventDescription;
        this.eventResponse = eventResponse;
        this.eventDateResponse = eventDateResponse;
//        this.eventStatus = eventStatus;
    }



    public InvitePojo(Parcel in) {
        this.eventTitle = in.readString();
        this.eventLocation = in.readString();
        this.eventStart = in.readString();
        this.eventEnd = in.readString();
        this.eventDescription = in.readString();
        this.eventResponse = in.readString();
        this.eventDateResponse = in.readString();
//        this.eventStatus = in.readInt();
    }

    public InvitePojo(String eventTitle, String eventLocation, String eventStart, String eventEnd, String eventDescription,
                      String eventId, String eventAllDay, String eventCreatedBy, String eventStatus, String eventResponse,
                      String eventDateResponse){
        this.eventTitle = eventTitle;
        this.eventLocation = eventLocation;
        this.eventStart = eventStart;
        this.eventEnd = eventEnd;
        this.eventDescription = eventDescription;
        this.eventId = eventId;
        this.eventAllDay = eventAllDay;
        this.eventCreatedBy = eventCreatedBy;
        this.eventStatus = eventStatus;
        this.eventResponse = eventResponse;
        this.eventDateResponse = eventDateResponse;

    }

    public InvitePojo()
    {

    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventAllDay() {
        return eventAllDay;
    }

    public void setEventAllDay(String eventAllDay) {
        this.eventAllDay = eventAllDay;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public String getEventEnd() {
        return eventEnd;
    }

    public void setEventEnd(String eventEnd) {
        this.eventEnd = eventEnd;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }


    public String getEventCreatedBy() {
        return eventCreatedBy;
    }

    public void setEventCreatedBy(String eventCreatedBy) {
        this.eventCreatedBy = eventCreatedBy;
    }

    public String getEventResponse() {
        return eventResponse;
    }

    public void setEventResponse(String eventResponse) {
        this.eventResponse = eventResponse;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventDateResponse() {
        return eventDateResponse;
    }

    public void setEventDateResponse(String eventDateResponse) {
        this.eventDateResponse = eventDateResponse;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        parcel.writeString(this.eventTitle);
        parcel.writeString(this.eventLocation);
        parcel.writeString(this.eventStart);
        parcel.writeString(this.eventEnd);
        parcel.writeString(this.eventDescription);
        parcel.writeString(this.eventResponse);
        parcel.writeString(this.eventDateResponse);
//        parcel.writeInt(this.eventStatus);

    }

    public static final Creator<InvitePojo> CREATOR = new Creator<InvitePojo>() {
        @Override
        public InvitePojo createFromParcel(Parcel in) {
            return new InvitePojo(in);
        }
        @Override
        public InvitePojo[] newArray(int size) {
            return new InvitePojo[size];
        }
    };
}
