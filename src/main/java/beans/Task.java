package beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;


public class Task {
    private int id;
    private String name;
    private boolean done;
    private int userId;
    private String author;
    private  Date start_date;
    private  Date finish_date;
    
    private  int priority;
    private  String description;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String user) {
        this.author = user;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @JsonProperty("start")
    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }
    @JsonProperty("finish")
    public Date getFinish_date() {
        return finish_date;
    }

    public void setFinish_date(Date finish_date) {
        this.finish_date = finish_date;
    }
    
    
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("done")
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @JsonIgnore
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", name=" + name + ", done=" + done 
        + ", userId=" + userId + ", start=" + start_date
        + ", finish=" + finish_date + ", priority=" + priority 
        + ", description=" + description + '}';
    }
    
}
