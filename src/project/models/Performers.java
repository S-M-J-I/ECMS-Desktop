package project.models;

public class Performers {
    private int id;
    private int tasksDone;
    private int tasksGiven;
    private double prod;

    public Performers(int id) {
        this.id = id;
    }

    public Performers(int id, int tasksDone, int tasksGiven) {
        this.id = id;
        this.tasksDone = tasksDone;
        this.tasksGiven = tasksGiven;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTasksDone(int tasksDone) {
        this.tasksDone = tasksDone;
    }

    public void setTasksGiven(int tasksGiven) {
        this.tasksGiven = tasksGiven;
    }

    public int getTasksDone() {
        return tasksDone;
    }

    public int getTasksGiven() {
        return tasksGiven;
    }

    public double getProd() {
        return ((double) this.tasksDone / this.tasksGiven) * 100;
    }

    @Override
    public String toString() {
        return "Performers{" +
                "id=" + id +
                ", tasksDone=" + tasksDone +
                ", tasksGiven=" + tasksGiven +
                ", prod=" + prod +
                '}';
    }
}
