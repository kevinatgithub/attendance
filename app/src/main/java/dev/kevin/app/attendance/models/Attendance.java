package dev.kevin.app.attendance.models;

public class Attendance {

    private int id;
    private int member_id;
    private String qrcode;
    private String created_at;
    private Event event_a_m;
    private Event event_p_m;

    public Attendance(int id, int member_id, String qrcode, String created_at, Event event_a_m, Event event_p_m) {
        this.id = id;
        this.member_id = member_id;
        this.qrcode = qrcode;
        this.created_at = created_at;
        this.event_a_m = event_a_m;
        this.event_p_m = event_p_m;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Event getEvent_a_m() {
        return event_a_m;
    }

    public void setEvent_a_m(Event event_a_m) {
        this.event_a_m = event_a_m;
    }

    public Event getEvent_p_m() {
        return event_p_m;
    }

    public void setEvent_p_m(Event event_p_m) {
        this.event_p_m = event_p_m;
    }
}
