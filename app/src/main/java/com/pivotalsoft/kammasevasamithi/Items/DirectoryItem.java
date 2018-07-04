package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 10/30/2017.
 */

public class DirectoryItem {

    private String memberid;
    private String fullname;
    private String fathername;
    private String dob;
    private String gothram;
    private String mobile;
    private String email;
    private String bloodgroup;
    private String referalno;
    private String presentaddress;
    private String nativeaddress;
    private String profilepic;
    private String status;
    private String role;
    private String membershipNo;
    private String password;
    private String regdate;
    private String fcmkey;


    public DirectoryItem(String memberid, String fullname, String fathername, String dob, String gothram, String mobile, String email, String bloodgroup, String referalno, String presentaddress, String nativeaddress, String profilepic, String status, String role, String membershipNo, String password, String regdate, String fcmkey) {
        this.memberid = memberid;
        this.fullname = fullname;
        this.fathername = fathername;
        this.dob = dob;
        this.gothram = gothram;
        this.mobile = mobile;
        this.email = email;
        this.bloodgroup = bloodgroup;
        this.referalno = referalno;
        this.presentaddress = presentaddress;
        this.nativeaddress = nativeaddress;
        this.profilepic = profilepic;
        this.status = status;
        this.role = role;
        this.membershipNo = membershipNo;
        this.password = password;
        this.regdate = regdate;
        this.fcmkey = fcmkey;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGothram() {
        return gothram;
    }

    public void setGothram(String gothram) {
        this.gothram = gothram;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getReferalno() {
        return referalno;
    }

    public void setReferalno(String referalno) {
        this.referalno = referalno;
    }

    public String getPresentaddress() {
        return presentaddress;
    }

    public void setPresentaddress(String presentaddress) {
        this.presentaddress = presentaddress;
    }

    public String getNativeaddress() {
        return nativeaddress;
    }

    public void setNativeaddress(String nativeaddress) {
        this.nativeaddress = nativeaddress;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getMembershipNo() {
        return membershipNo;
    }

    public void setMembershipNo(String membershipNo) {
        this.membershipNo = membershipNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public String getFcmkey() {
        return fcmkey;
    }

    public void setFcmkey(String fcmkey) {
        this.fcmkey = fcmkey;
    }
}
