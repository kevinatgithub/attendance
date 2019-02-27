package dev.kevin.app.attendance.models;

public class Event {
    private String qrcode_am;
    private String qrcode_pm;
    private String title;

    public Event(String qrcode_am, String qrcode_pm, String title) {
        this.qrcode_am = qrcode_am;
        this.qrcode_pm = qrcode_pm;
        this.title = title;
    }

    public String getQrcode_am() {
        return qrcode_am;
    }

    public void setQrcode_am(String qrcode_am) {
        this.qrcode_am = qrcode_am;
    }

    public String getQrcode_pm() {
        return qrcode_pm;
    }

    public void setQrcode_pm(String qrcode_pm) {
        this.qrcode_pm = qrcode_pm;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
