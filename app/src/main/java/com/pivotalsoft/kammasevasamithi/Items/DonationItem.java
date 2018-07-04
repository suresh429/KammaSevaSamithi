package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 10/31/2017.
 */

public class DonationItem {
    private String donationid;
    private String memberidmemberid;
    private String amount;
    private String paymentmode;
    private String description;
    private String paiddate;
    private String status;
    private String membershipno;
    private String fullname;
    private String role;
    private String profilepic;
    private String fcmkey;

    public DonationItem(String donationid, String memberidmemberid, String amount, String paymentmode, String description, String paiddate, String status, String membershipno, String fullname, String role, String profilepic, String fcmkey) {
        this.donationid = donationid;
        this.memberidmemberid = memberidmemberid;
        this.amount = amount;
        this.paymentmode = paymentmode;
        this.description = description;
        this.paiddate = paiddate;
        this.status = status;
        this.membershipno = membershipno;
        this.fullname = fullname;
        this.role = role;
        this.profilepic = profilepic;
        this.fcmkey = fcmkey;
    }

    public String getDonationid() {
        return donationid;
    }

    public void setDonationid(String donationid) {
        this.donationid = donationid;
    }

    public String getMemberidmemberid() {
        return memberidmemberid;
    }

    public void setMemberidmemberid(String memberidmemberid) {
        this.memberidmemberid = memberidmemberid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPaymentmode() {
        return paymentmode;
    }

    public void setPaymentmode(String paymentmode) {
        this.paymentmode = paymentmode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPaiddate() {
        return paiddate;
    }

    public void setPaiddate(String paiddate) {
        this.paiddate = paiddate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMembershipno() {
        return membershipno;
    }

    public void setMembershipno(String membershipno) {
        this.membershipno = membershipno;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getFcmkey() {
        return fcmkey;
    }

    public void setFcmkey(String fcmkey) {
        this.fcmkey = fcmkey;
    }
}
