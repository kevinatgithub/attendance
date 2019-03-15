package dev.kevin.app.attendance.models;

public class Attendance {

    private int id;
    private int member_id;
    private int event_id;
    private String created_at;
    private int day;
    private String session;
    private int type;
    private Event evnt;

    public Attendance(int id, int member_id, int event_id, String created_at, int day, String session, int type, Event evnt) {
        this.id = id;
        this.member_id = member_id;
        this.event_id = event_id;
        this.created_at = created_at;
        this.day = day;
        this.session = session;
        this.type = type;
        this.evnt = evnt;
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

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Event getEvnt() {
        return evnt;
    }

    public void setEvnt(Event evnt) {
        this.evnt = evnt;
    }
}
