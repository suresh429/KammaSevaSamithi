package com.pivotalsoft.kammasevasamithi.Items;

/**
 * Created by Gangadhar on 11/1/2017.
 */

public class FamilyItem {

    private String familyid;
    private String membername;
    private String relation;
    private String qualification;
    private String bloodgroup;
    private String occassion;
    private String occdate;
    private String memberid;

    public FamilyItem(String familyid, String membername, String relation, String qualification, String bloodgroup, String occassion, String occdate, String memberid) {
        this.familyid = familyid;
        this.membername = membername;
        this.relation = relation;
        this.qualification = qualification;
        this.bloodgroup = bloodgroup;
        this.occassion = occassion;
        this.occdate = occdate;
        this.memberid = memberid;
    }

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getOccassion() {
        return occassion;
    }

    public void setOccassion(String occassion) {
        this.occassion = occassion;
    }

    public String getOccdate() {
        return occdate;
    }

    public void setOccdate(String occdate) {
        this.occdate = occdate;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }
}
