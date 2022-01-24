package project.models;

public class Announcement {
    private int id;
    private String title;
    private String dateTime;
    private String body;
    private int posted_by;

    public Announcement() {
    }

    public Announcement(String title, String dateTime, String body, int posted_by) {
        this.title = title;
        this.dateTime = dateTime;
        this.body = body;
        this.posted_by = posted_by;
    }

    public Announcement(int id, String title, String dateTime, String body, int posted_by) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.body = body;
        this.posted_by = posted_by;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
