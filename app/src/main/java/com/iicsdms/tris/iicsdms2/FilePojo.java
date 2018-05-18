package com.iicsdms.tris.iicsdms2;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Tris on 03/03/2018.
 */

public class FilePojo implements Serializable,Parcelable{

    private int extensionImg;
    private String docId;
    private String type;
    private String title;
    private String category;
    private String fileName;
    private String description;
    private String createdBy;
    private String email;
    private String timeCreated;
    private String thread;
    private String actionReq, actionDue, status;
    private String folderId, sourceRecipient, uploadDate, department, referenceNum, academicYear;
    private String note;
    private String size;


    public FilePojo ( String fileName, String size, String timeCreated, int extensionImg ){
        this.fileName = fileName;
        this.size = size;
        this.timeCreated = timeCreated;
        this.extensionImg = extensionImg;
    }

    public FilePojo (String title, String srcRecipient, String timeCreated, String category, String actionReq, String actionDue,
                      String status, String referenceNum, String fileName, String type, String thread, String docID)
    {
        this.title = title;
        this.sourceRecipient = srcRecipient;
        this.timeCreated = timeCreated;
        this.category = category;
        this.actionReq = actionReq;
        this.actionDue = actionDue;
        this.status = status;
        this.referenceNum = referenceNum;
        this.fileName = fileName;
        this.type = type;
        this.thread = thread;
        this.docId = docID;
    }

    public FilePojo (String title, String category, String timeCreated, String sourceRecipient, String fileName, String type, String thread, String docID)
    {
        this.docId = docID;
        this.thread = thread;
        this.type = type;
        this.sourceRecipient = sourceRecipient;
        this.fileName = fileName;
        this.timeCreated = timeCreated;
        this.category = category;
        this.title = title;
    }

    public FilePojo (String title, String type, String category, String sourceRecipient, String createdBy, String email,
                      String timeCreated, String academicYear, String fileName, String docId, String folderId)
    {
        this.title = title;
        this.type = type;
        this.category = category;
        this.sourceRecipient = sourceRecipient;
        this.createdBy = createdBy;
        this.email = email;
        this.timeCreated = timeCreated;
        this.academicYear = academicYear;
        this.fileName = fileName;
        this.docId = docId;
        this.folderId = folderId;
    }

    public FilePojo (String title, String category, String timeCreated,
                     String fileName, String type, String docId, String createdBy)
    {
        this.title = title;
        this.category = category;
        this.timeCreated = timeCreated;
        this.fileName = fileName;
        this.type = type;
        this.docId = docId;
        this.createdBy = createdBy;
    }

    public FilePojo (String title, String category, String createdBy, String email, String timeCreated, String sourceRecipient,
                          String fileName, String thread, String type, String docId, String status, String id, String id2, String id3)
    {
        this.title = title;
        this.category = category;
        this.createdBy = createdBy;
        this.email = email;
        this.timeCreated = timeCreated;
        this.sourceRecipient = sourceRecipient;
        this.fileName = fileName;
        this.thread = thread;
        this.type = type;
        this.docId = docId;
        this.status = status;

    }

    public FilePojo(Parcel in) {
        this.fileName = in.readString();
        this.size = in.readString();
        this.timeCreated = in.readString();
        this.extensionImg = in.readInt();
    }

    public FilePojo(String createdBy, String email, String upDate, String category, String fileName, String desc, String refNo,
                    String actionReq, String actionDue, String status, String title, String type, String thread)
    {
        this.createdBy = createdBy;
        this.email = email;
        this.timeCreated = upDate;
        this.category = category;
        this.fileName = fileName;
        this.description = desc;
        this.referenceNum = refNo;
        this.actionReq = actionReq;
        this.actionDue = actionDue;
        this.status = status;
        this.title = title;
        this.type = type;
    }

    public FilePojo() {

    }


    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getSize() {
        return size;
    }

    public int getExtensionImg(){
        return extensionImg;
    }

    String getThread() {
        return thread;
    }

    void setThread(String thread) {
        this.thread = thread;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    String getFolderId() {
        return folderId;
    }

    void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    String getSourceRecipient() {
        return sourceRecipient;
    }

    void setSourceRecipient(String sourceRecipient) {
        this.sourceRecipient = sourceRecipient;
    }

    String getUploadDate() {
        return uploadDate;
    }

    void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    String getDepartment() {
        return department;
    }

    void setDepartment(String department) {
        this.department = department;
    }

    String getReferenceNum() {
        return referenceNum;
    }

    void setReferenceNum(String referenceNum) {
        this.referenceNum = referenceNum;
    }

    String getAcademicYear() {
        return academicYear;
    }

    void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getActionReq() {
        return actionReq;
    }

    public void setActionReq(String actionReq) {
        this.actionReq = actionReq;
    }

    public String getActionDue() {
        return actionDue;
    }

    public void setActionDue(String actionDue) {
        this.actionDue = actionDue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.fileName);
        parcel.writeString(this.size);
        parcel.writeString(this.timeCreated);
        parcel.writeInt(this.extensionImg);
    }
    public static final Creator<FilePojo> CREATOR = new Creator<FilePojo>() {
        @Override
        public FilePojo createFromParcel(Parcel in) {
            return new FilePojo(in);
        }
        @Override
        public FilePojo[] newArray(int size) {
            return new FilePojo[size];
        }
    };
}
