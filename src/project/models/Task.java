package project.models;

public class Task {
    private int taskId;
    private String name;
    private String description;
    private int assigned_to;
    private int assignee;
    private boolean pending;
    private boolean completed;
    private String deadline;

    public Task(int taskId, String name, String description, int assigned_to, int assignee) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigned_to = assigned_to;
        this.assignee = assignee;
    }

    public Task(int taskId, String name, String description, int assigned_to, int assignee, boolean pending, boolean completed, String deadline) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigned_to = assigned_to;
        this.assignee = assignee;
        this.pending = pending;
        this.completed = completed;
        this.deadline = deadline;
    }

    public Task(int taskId, String name, String description, int assigned_to, int assignee, String deadline) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigned_to = assigned_to;
        this.assignee = assignee;
        this.deadline = deadline;
    }

    public Task(int taskId, String name, String description, int assigned_to, int assignee, boolean completed) {
        this.taskId = taskId;
        this.name = name;
        this.description = description;
        this.assigned_to = assigned_to;
        this.assignee = assignee;
        this.completed = completed;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getAssigned_to() {
        return assigned_to;
    }

    public int getAssignee() {
        return assignee;
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getDeadline() {
        return deadline;
    }
}
