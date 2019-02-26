package dev.kevin.app.attendance.models;

public class Member {

    private int id;
    private String fname;
    private String lname;
    private String mi;
    private String affiliation;
    private String prc_no;

    public Member(int id, String fname, String lname, String mi, String affiliation, String prc_no) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.mi = mi;
        this.affiliation = affiliation;
        this.prc_no = prc_no;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getPrc_no() {
        return prc_no;
    }

    public void setPrc_no(String prc_no) {
        this.prc_no = prc_no;
    }
}
