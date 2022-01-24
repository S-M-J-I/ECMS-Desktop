package project.controllers.cf_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.CSSTabPath;
import project.middlewares.*;
import project.models.ClubForum;
import project.models.Member;
import project.models.Student;
import project.models.Task;
import project.views.ViewFactory;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class HRServicesWindowController extends BaseController implements Initializable {
    private ClubForum clubForum;

    public HRServicesWindowController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init_table();
        setUpUpdateTab();
        toggleRecruitmentBtn();
        refreshRemoveTab();
        setUpTabsAndListenForChanges();
        setAssignTaskTab();
        setUpTaskRecheckTab();
        setUpMemberRequestsTab();
    }

    public void setUpMemberRequestsTab() {
        memberRequestQueue.setVisible(false);
        memberRequestQueue.getItems().clear();
        ArrayList<Member> membersQueue = MemberMiddleware.membersWhoseMembershipArePending(this.clubForum.getForumID());
        AtomicInteger count = new AtomicInteger(0);

        for(Member member : membersQueue) {
            Label label = new Label();
            label.setText(member.getName()+", "+member.getUIUid()+" requested to join:");
            Button seeReason = new Button("See Reason");
            seeReason.setStyle("-fx-font-family: Montserrat; -fx-background-color: orange; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand;");
            seeReason.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    HRServicesWindowController.super.getViewFactory().showReasonWindow(member, clubForum, HRServicesWindowController.this);
                }
            });
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label, seeReason);
            memberRequestQueue.getItems().add(hBox);
            count.getAndIncrement();
        }

        if(count.get() > 0) {
            addMembersTab.setText(addMembersTab.getText() + "\u2757");
            membersCountLabel.setText("You have " + count.get() + " requests pending");
            memberRequestQueue.setVisible(true);
        } else {
            addMembersTab.setText("Add Members");
            membersCountLabel.setText("No members to be added");
            memberRequestQueue.setVisible(false);
        }
    }


    @FXML
    private TableView<Member> infoTable = new TableView<>();

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab viewMembersTab;

    @FXML
    private Tab updateMembersTab;

    @FXML
    private Tab removeMemberTab;

    @FXML
    private Tab assignTaskTab;


    @FXML
    private TableColumn<Member, String> enrolIdCol;

    @FXML
    private TableColumn<Member, String> IDCol;

    @FXML
    private TableColumn<Member, String> nameCol;

    @FXML
    private TableColumn<Member, String> designationCol;

    @FXML
    private TableColumn<Member, String> deptCol;


    @FXML
    private TableColumn<Member, String> phoneCol;

    @FXML
    private TableColumn<Member, String> emailCol;


    @FXML
    private Button refreshBtn;

    @FXML
    private Button recruitmentToggleBtn;




    /** UPDATE MEMBER TAB */
    @FXML
    private ChoiceBox<String> enrolIdChoiceList;

    @FXML
    private ChoiceBox<String> designationChoiceList;

    @FXML
    private ChoiceBox<String> departmentChoiceList;

    @FXML
    private Label dynamicResponseLabel;


    /** Remove Member Tab */
    @FXML
    private Label memberRemoveLabel;

    @FXML
    private ChoiceBox<String> enrolIdSelectorList;

    @FXML
    private PasswordField removeMemberPasswordField;

    @FXML
    private Label dynamicResponseLabelRemoveTab;


    /*** assign task tab */

    @FXML
    private CheckBox taskIndividualCheckBox;

    @FXML
    private CheckBox taskAllCheckBox;

    @FXML
    private Label enrolIdsLabel;

    @FXML
    private ChoiceBox<String> enrolIdSelectorListOnAssignTaskTab;

    @FXML
    private Label memberRemoveLabel1;

    @FXML
    private TextField taskName;

    @FXML
    private TextArea taskDescription;

    @FXML
    private Label assignTaskLabel;

    @FXML
    private DatePicker deadlinePicker;


    @FXML
    private Tab recheckTaskTab;

    @FXML
    private ListView<HBox> recheckTasksListView;

    private ArrayList<Task> tasks;

    @FXML
    private Label tasksCheckLabel;


    /** Member requests tab **/
    @FXML
    private Tab addMembersTab;

    @FXML
    private ListView<HBox> memberRequestQueue;

    @FXML
    private Label membersCountLabel;

    @FXML
    private ImageView membersExistsIcon;


    /** Action Listeners */

    @FXML
    void onRecruitmentToggleButtonAction(ActionEvent event) {
        clubForum = CFMiddleware.startOrStopRecruitment(this.clubForum);
        toggleRecruitmentBtn();
    }

    @FXML
    void exportAction(ActionEvent event) {
        ExcelExport xls = new ExcelExport();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("excel files (*.xls)", "*.xls"));

        File file = fileChooser.showSaveDialog((Stage) enrolIdsLabel.getScene().getWindow());
        if(file != null) {
            xls.export(infoTable, clubForum.getName(), file);
        }

    }

    @FXML
    void onRefreshBtn(ActionEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                init_table();
            }
        });
    }

    @FXML
    void onUpdateMembersAction(ActionEvent event) {
        System.out.println("Trying to update");
        MemberMiddleware.updateMember(
                Integer.parseInt(enrolIdChoiceList.getValue()),
                designationChoiceList.getValue(),
                departmentChoiceList.getValue(),
                clubForum.getForumID()
        );
        dynamicResponseLabel.setText("Updated");
        init_table();
        setUpUpdateTab();
    }

    @FXML
    void onRemoveMembersAction(ActionEvent event) {
        if(removeMemberPasswordField.getText().equals(this.clubForum.getPassword())) {
            Member member = MemberMiddleware.getMemberDetailsByMemberId(Integer.parseInt(enrolIdSelectorList.getValue()));
            MemberMiddleware.removeMember(member.getId());
            dynamicResponseLabelRemoveTab.setText("Removed");
            dynamicResponseLabelRemoveTab.setTextFill((Paint) Color.DARKGREEN);
            init_table();
        } else {
            dynamicResponseLabelRemoveTab.setText("Wrong Password");
            dynamicResponseLabelRemoveTab.setTextFill((Paint) Color.RED);
        }
    }

    @FXML
    void onAssignTaskAction(ActionEvent event) {
        LocalDate deadlineDate = deadlinePicker.getValue();
        String deadline = deadlineDate.toString();

        if(taskAllCheckBox.isSelected()) {
            ArrayList<Integer> members = MemberMiddleware.getAllMemberEnrolIdsAsInts(this.clubForum.getForumID());
            TaskMiddleware.createTaskForAll(
                    taskName.getText(),
                    taskDescription.getText(),
                    deadline,
                    this.clubForum,
                    members,
                    assignTaskLabel
            );
            taskName.clear();
            taskDescription.clear();
            setAssignTaskTab();
            return;
        }

        Member assignedTo = MemberMiddleware.getMemberDetailsByUMIDandCFref(Integer.parseInt(enrolIdSelectorListOnAssignTaskTab.getValue()), clubForum.getForumID());
        if(!TaskMiddleware.isMaxLimit(assignedTo)) {
            TaskMiddleware.createTask(
                    taskName.getText(),
                    taskDescription.getText(),
                    deadline,
                    this.clubForum,
                    assignedTo,
                    assignTaskLabel
            );
            taskName.clear();
            taskDescription.clear();
            setAssignTaskTab();
        } else {
            assignTaskLabel.setText("Member has maximum tasks limit reached!");
            assignTaskLabel.setTextFill(Color.RED);
        }

    }


    @FXML
    void onBackAction(ActionEvent event) {
        super.getViewFactory().showCFDashboard(clubForum);
        super.getViewFactory().closeWindow((Stage) refreshBtn.getScene().getWindow());
    }


    /** Helpers */

    private void setUpTaskRecheckTab() {
        recheckTasksListView.setVisible(false);
        AtomicInteger count = new AtomicInteger(0);
        tasks = TaskMiddleware.getTasksThatNeedRecheck(this.clubForum.getForumID());

        for(Task t : tasks) {
            Label label = new Label();
            label.setText(MemberMiddleware.getMemberDetailsByEnrolId(t.getAssigned_to()).getName()+" submitted " + t.getName());
            Button accept = new Button("Accept");
            accept.setStyle("-fx-font-family: Montserrat; -fx-background-color: orange; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand;");
            accept.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    TaskMiddleware.completeTask(t.getTaskId());
                    Platform.runLater(() -> {
                        tasks.clear();
                        recheckTasksListView.getItems().clear();
                        setUpTaskRecheckTab();
                    });
                }
            });
            Button reject = new Button("Reject");
            reject.setStyle("-fx-font-family: Montserrat; -fx-background-color: orange; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand;");
            reject.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    TaskMiddleware.rejectTask(t.getTaskId());
                    Platform.runLater(() -> {
                        tasks.clear();
                        recheckTasksListView.getItems().clear();
                        setUpTaskRecheckTab();
                    });
                }
            });
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setSpacing(10);
            hBox.getChildren().addAll(label, accept, reject);
            recheckTasksListView.getItems().add(hBox);
            count.getAndIncrement();
        }

        if(count.get() > 0) {
            tasksCheckLabel.setText(count.get() + " tasks need a recheck!");
            recheckTasksListView.setVisible(true);
        } else {
            tasksCheckLabel.setText("No tasks need rechecking right now!");
            recheckTasksListView.setVisible(false);
        }
    }

    public void init_table() {
        enrolIdCol.setCellValueFactory(new PropertyValueFactory<>("Student_id"));
        IDCol.setCellValueFactory(new PropertyValueFactory<>("UIUid"));//
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));//
        designationCol.setCellValueFactory(new PropertyValueFactory<>("Designation"));//
        deptCol.setCellValueFactory(new PropertyValueFactory<>("Department"));//
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("Phone"));//
        emailCol.setCellValueFactory(new PropertyValueFactory<>("Email"));//

        enrolIdCol.setEditable(false);
        IDCol.setEditable(false);
        nameCol.setEditable(false);
        designationCol.setEditable(true);
        deptCol.setEditable(true);
        phoneCol.setEditable(true);
        emailCol.setEditable(true);


        /** TODO: Get all data of those members whose cf match with current cf */
        infoTable.setItems(FXCollections.observableArrayList(MemberMiddleware.getMemberDetails(clubForum.getForumID())));
    }

    private void toggleRecruitmentBtn() {
        Platform.runLater(() -> {
            if(this.clubForum.isRecruitmentOn()) {
                recruitmentToggleBtn.setText("STOP RECRUITMENT");
            } else {
                recruitmentToggleBtn.setText("START RECRUITMENT");
            }
        });
    }

    private ArrayList<String> getDesignations() {
        ArrayList<String> designations = new ArrayList<>();
        designations.add("General Member");
        designations.add("Junior Executive");
        designations.add("Executive");
        designations.add("Department Deputy");
        designations.add("Department Head");
        designations.add("Treasurer");
        designations.add("Organising Secretary");
        designations.add("General Secretary");
        designations.add("Vice-President");
        designations.add("President");

        return designations;
    }

    private ArrayList<String> getDepartments() {
        ArrayList<String> departments = new ArrayList<>();
        departments.add("General Committee");
        departments.add("Marketing");
        departments.add("Public Relations");
        departments.add("Event Management");
        departments.add("Human Resources");
        departments.add("Research and Development");
        departments.add("Core Committee");

        return departments;
    }

    private void setUpUpdateTab() {
        enrolIdChoiceList.setItems(FXCollections.observableArrayList(MemberMiddleware.getAllMemberUMIds(this.clubForum.getForumID())));
        designationChoiceList.setItems(FXCollections.observableArrayList(getDesignations()));
        designationChoiceList.setValue("Junior Executive");
        departmentChoiceList.setItems(FXCollections.observableArrayList(getDepartments()));
        designationChoiceList.setOnAction(e -> {
            Platform.runLater(() -> {
                if(checkIfCoreMembers()) {
                    departmentChoiceList.setValue("Core Committee");
                }
            });
        });
    }

    private void refreshRemoveTab() {
        enrolIdSelectorList.setItems(FXCollections.observableArrayList(MemberMiddleware.getAllMemberUMIds(this.clubForum.getForumID())));
        enrolIdSelectorList.setOnAction(e -> {
            Platform.runLater(() -> {
                Member member = MemberMiddleware.getMemberDetailsByMemberId(Integer.parseInt(enrolIdSelectorList.getValue()));
                memberRemoveLabel.setText("Are you sure you want to remove " + member.getName() + " | ID:" + member.getUIUid());
            });
        });
    }

    private void setAssignTaskTab() {
        taskIndividualCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                enrolIdsLabel.setVisible(taskIndividualCheckBox.isSelected());
                enrolIdSelectorListOnAssignTaskTab.setVisible(taskIndividualCheckBox.isSelected());
                taskAllCheckBox.setSelected(!taskIndividualCheckBox.isSelected());
            }
        });
        taskAllCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                taskIndividualCheckBox.setSelected(!taskAllCheckBox.isSelected());
                enrolIdSelectorListOnAssignTaskTab.setVisible(false);
                enrolIdsLabel.setVisible(false);
            }
        });
        enrolIdSelectorListOnAssignTaskTab.setItems(FXCollections.observableArrayList(MemberMiddleware.getAllMemberUMIds(this.clubForum.getForumID())));
    }

    private boolean checkIfCoreMembers() {
        return designationChoiceList.getValue().equals("President") || designationChoiceList.getValue().equals("Vice-President") ||
                designationChoiceList.getValue().equals("General Secretary") || designationChoiceList.getValue().equals("Organising Secretary") ||
                designationChoiceList.getValue().equals("Treasurer");
    }

    private void setUpTabsAndListenForChanges() {
        tabPane.setStyle("-fx-background-color: white;");
        tabPane.getStyleClass().add("floating");
        viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
        updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Platform.runLater(() -> {
                if(newTab.equals(viewMembersTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if(newTab.equals(updateMembersTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(removeMemberTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(assignTaskTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(recheckTaskTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(addMembersTab)) {
                    viewMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    removeMemberTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    assignTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    recheckTaskTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    addMembersTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                }
            });
        });
    }

}
