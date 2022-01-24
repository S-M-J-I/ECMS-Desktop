package project.controllers.student_controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import project.controllers.BaseController;
import project.middlewares.CFMiddleware;
import project.models.Task;
import project.views.ViewFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskDetailsSubWindowController extends BaseController implements Initializable {
    private Task task;
    public TaskDetailsSubWindowController(ViewFactory viewFactory, String fxmlName, Task task) {
        super(viewFactory, "ui/student_views/"+fxmlName);
        this.task = task;
    }

    @FXML
    private Label taskName;

    @FXML
    private Label assigneeName;

    @FXML
    private Label taskDetails;

    @FXML
    private Label deadlineLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        taskName.setText(task.getName());
        assigneeName.setText(CFMiddleware.getCFById(task.getAssignee()).getName());
        taskDetails.setText(task.getDescription());
        deadlineLabel.setText(task.getDeadline());
    }
}
