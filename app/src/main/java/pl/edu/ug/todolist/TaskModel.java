package pl.edu.ug.todolist;

public class TaskModel {

    private int id;
    private String name;
    private String deadlineDate;
    private String deadlineTime;

    public TaskModel(int id, String name, String deadlineDate, String deadlineTime) {
        this.id = id;
        this.name = name;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadlineTime;
    }

    public TaskModel(String name, String deadlineDate, String deadLineTime) {
        this.name = name;
        this.deadlineDate = deadlineDate;
        this.deadlineTime = deadLineTime;
    }

    @Override
    public String toString() {
        return name + '\n' + deadlineDate + ' ' + deadlineTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(String deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public String getDeadlineTime() {
        return deadlineTime;
    }

    public void setDeadlineTime(String deadlineTime) {
        this.deadlineTime = deadlineTime;
    }
}
