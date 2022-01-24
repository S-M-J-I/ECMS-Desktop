package project.middlewares;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import project.database.DBHandler;
import project.models.ClubForum;
import project.models.Member;
import project.models.Performers;
import project.models.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TaskMiddleware {
    private static PreparedStatement pst;
    private static Connection connection;
    private static final DBHandler dbHandler = new DBHandler();

    public static void createTask(String name, String description, String deadline, ClubForum cf, Member member, Label assignTaskLabel) {
        String create = "INSERT INTO tasks(`name`, `description`, `assigned_to`, `assignee`, `deadline`) " +
                "VALUES(?,?,?,?,?)";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(create);

            pst.setString(1, name);
            pst.setString(2, description);
            pst.setInt(3, member.getId());
            pst.setInt(4, cf.getForumID());
            pst.setString(5, deadline);

            pst.executeUpdate();

            assignTaskLabel.setText("Task given to " + member.getName());

        } catch (SQLException e) {
            e.printStackTrace();
            assignTaskLabel.setText("Something went wrong!");
            assignTaskLabel.setTextFill(Color.RED);
        }
    }

    public static void createTaskForAll(String name, String description, String deadline, ClubForum cf, ArrayList<Integer> members, Label assignTaskLabel) {
        StringBuilder createBuilder = new StringBuilder();
        createBuilder.append("INSERT INTO tasks(`name`, `description`, `assigned_to`, `assignee`, `deadline`) VALUES ");

        for(int i = 0 ; i < members.size(); ++i) {
            if(i == members.size() - 1) {
                createBuilder.append("('").append(name).append("','").append(description).append("',").append(members.get(i)).append(",").append(cf.getForumID()).append(", DATE '").append(deadline).append("')");
            } else {
                createBuilder.append("('").append(name).append("','").append(description).append("',").append(members.get(i)).append(",").append(cf.getForumID()).append(", DATE '").append(deadline).append("'), ");
            }
        }

        String create = createBuilder.toString();
        System.out.println(create);
        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(create);

            pst.executeUpdate();

            assignTaskLabel.setText("Tasks given to everyone");
            assignTaskLabel.setTextFill(Color.GREEN);

        } catch (SQLException e) {
            e.printStackTrace();
            assignTaskLabel.setText("Something went wrong!");
            assignTaskLabel.setTextFill(Color.RED);
        }
    }

    public static ArrayList<Task> getTaskDetails(int member) {

        ArrayList<Integer> ids =  new ArrayList<>();
        int count = 0;
        String retrieveEnrolIds = "SELECT `enrol_id` FROM members WHERE `member`=?";
        connection = dbHandler.getConnection();
        try {
            pst = connection.prepareStatement(retrieveEnrolIds);

            pst.setInt(1, member);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
                ids.add(rs.getInt("enrol_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<Task> tasks = new ArrayList<>();
        while (count > 0) {
            String get = "SELECT * from tasks WHERE `assigned_to`=? AND (completed='false' AND pending='false' AND submitted='false')";
            try {
                connection = dbHandler.getConnection();

                pst = connection.prepareStatement(get);

                pst.setString(1, Integer.toString(ids.get(count-1)));

                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    tasks.add(new Task(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getInt("assigned_to"),
                            rs.getInt("assignee"),
                            rs.getString("deadline")
                    ));
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            ids.remove(count-1);
            count--;
        }

        // TODO : Query tasks such that the incomplete task of the member is returned
        return tasks;
    }

    public static void completeTaskAndSendToHR(int taskId) {
        String update = "UPDATE tasks SET pending='true' WHERE id=?";

        try {
            connection = dbHandler.getConnection();

            pst = connection.prepareStatement(update);

            pst.setInt(1, taskId);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isMaxLimit(Member member) {
        String check = "SELECT * FROM tasks WHERE completed='false' AND `assigned_to`=?";
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(check);

            pst.setInt(1, member.getId());
            ResultSet rs = pst.executeQuery();

            int count = 0;

            while(rs.next()) {
                count++;
            }

            if(count >= 10) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static int getTasksDone(int assignee) {
        String get = "SELECT * FROM tasks WHERE assignee=? AND completed='true'";
        int count = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, assignee);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getTasksGiven(int assignee) {
        String get = "SELECT * FROM tasks WHERE assignee=?";
        int count = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, assignee);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getTasksDoneByMember(int assigned_to) {
        String get = "SELECT * FROM tasks WHERE `assigned_to`=? AND completed='true'";
        int count = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, assigned_to);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static int getTasksGivenToMember(int assigned_to) {
        String get = "SELECT * FROM tasks WHERE `assigned_to`=?";
        int count = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, assigned_to);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static ArrayList<Performers> getAllPerformers(int assignee) {
        String get = "SELECT * FROM tasks WHERE assignee=?";
        ArrayList<Performers> performers = new ArrayList<>();

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, assignee);
            ResultSet rs = pst.executeQuery();

            LinkedHashSet<Integer> ints = new LinkedHashSet<>();
            while (rs.next()) {
                ints.add(rs.getInt("assigned_to"));
            }

            for(int i : ints) {
                performers.add(new Performers(
                        i,
                        TaskMiddleware.getTasksDoneByMember(i),
                        TaskMiddleware.getTasksGivenToMember(i)
                ));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return performers;
    }

    public static ArrayList<Task> getTasksThatNeedRecheck(int id) {
        String get = "SELECT * FROM tasks WHERE `assignee`="+id+" AND (`pending`='true' AND `completed`='false')";
        ArrayList<Task> tasks = new ArrayList<>();

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("assigned_to"),
                        rs.getInt("assignee"),
                        rs.getString("deadline")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public static void completeTask(int id) {
        String update = "UPDATE tasks SET `pending`='false', `completed`='true', submitted='true' WHERE id="+id;

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(update);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void rejectTask(int id) {
        String update = "UPDATE tasks SET `pending`='false', `completed`='false', submitted='true' WHERE id="+id;

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(update);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
