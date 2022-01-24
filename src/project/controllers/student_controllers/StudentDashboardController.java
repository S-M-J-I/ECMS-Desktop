package project.controllers.student_controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import project.controllers.BaseController;
import project.enums.CSSTabPath;
import project.middlewares.*;
import project.models.*;
import project.socket.ClientHandler;
import project.views.ViewFactory;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class StudentDashboardController extends BaseController implements Initializable {
    private Student student;
    private Member member;

    public StudentDashboardController(ViewFactory viewFactory, String fxmlName, Student student) {
        super(viewFactory, "ui/student_views/"+fxmlName);
        this.student = student;
        this.member = MemberMiddleware.getMemberDetailsByUIUId(student.getMember_id());
    }


    @Override
    public void run() {
        while (true) {
            try {
                String receivedAnnouncementFromClient;
                StringBuilder htmlText = new StringBuilder();
                String cf = super.getReader().readLine();
                System.out.println("Got it");
                for(Map.Entry<String, HTMLEditor> cfh : map.entrySet()) {
                    if((cfh.getKey()).equals(cf)) {
                        System.out.println("In");
                        while ((receivedAnnouncementFromClient = super.getReader().readLine()) != null) {
                            if(receivedAnnouncementFromClient.equals("break")) {

                                break;
                            }
                            htmlText.append(receivedAnnouncementFromClient);
                        }
                        System.out.println(htmlText.toString());
                        Platform.runLater(() -> {
                            cfh.getValue().setHtmlText(htmlText.toString() + cfh.getValue().getHtmlText());
                        });
                    }
                }
                Platform.runLater(() -> {
                    NotificationSender.sendNotification(cf);
                });
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }


    @FXML
    private Label studentName;

    @FXML
    private Label uiuID;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab myCFTab;

    @FXML
    private TableView<Member> cfTable;

    @FXML
    private TableColumn<Member, String> cfNameColumn;

    @FXML
    private TableColumn<Member, String> cfDesignationColumn;

    @FXML
    private TableColumn<Member, String> cfDepartmentCol;

    @FXML
    private TableColumn<Member, String> feeColumn;

    @FXML
    private Tab myAnnouncementTab;

    @FXML
    private HTMLEditor announcementArea;

    @FXML
    private Tab joinCFTab;

    @FXML
    private ChoiceBox<String> cfSelectorList;

    @FXML
    private Label dynamicCFNameLabel;

    @FXML
    private TextArea joinReasonTextArea;


    @FXML
    private PasswordField cfJoinPassword;

    @FXML
    private Button joinBtn;

    @FXML
    private Label joinStatusLabel;




    @FXML
    private Tab updateInfoTab;

    @FXML
    private TextField newUsernameField;

    @FXML
    private TextField newEmailField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private TextField newPhoneNumberField;



    /** My Tasks Tab */
    @FXML
    private Tab myTasksTab;

    @FXML
    private Label tasksLabel;

    @FXML
    private ListView<HBox> tasksList;

    private ArrayList<Task> tasks;


    /** Announcement tests */
    @FXML
    private Tab myAnnouncementTabtest;

    @FXML
    private Accordion announcementAccordion;

    private ArrayList<HTMLEditor> htmlEditors = new ArrayList<>();
    private HashMap<String, HTMLEditor> map = new HashMap<>();

    @FXML
    void onLogoutAction(ActionEvent event) {
        super.getViewFactory().showStudentLoginView();
        super.getViewFactory().closeWindow((Stage) tasksList.getScene().getWindow());
    }

    @FXML
    void onJoinRequestAction(ActionEvent event) {
        try {
            MemberMiddleware.sendJoinRequestIntoCF(
                    this.student,
                    CFMiddleware.getCFByName(cfSelectorList.getValue()),
                    joinReasonTextArea.getText()
            );
        } catch (Exception e) {
            joinStatusLabel.setText("Something went wrong!");
            joinStatusLabel.setTextFill(Color.RED);
            return;
        }

        joinStatusLabel.setText("Successfully sent join request to " + cfSelectorList.getValue());
        joinStatusLabel.setTextFill(Color.GREEN);

        Thread joinTabThread = new Thread(new Runnable() {
            @Override
            public void run() {
                refreshFieldsInJoinCFTab();
            }
        });
        Thread updateCFTableViewThread = new Thread(new Runnable() {
            @Override
            public void run() {
                setUpOrRefreshCfTableView();
            }
        });

        joinTabThread.start();
        updateCFTableViewThread.start();
    }


    @FXML
    void onUpdateDetailsAction(ActionEvent event) {
        StudentMiddleware.update(newUsernameField, newEmailField,newPhoneNumberField,newPasswordField,student.getMember_id());
        System.out.println("Updated");

        student = StudentMiddleware.getData(student.getMember_id());
        clearAllUpdateTabFields();
        refreshFieldsInUpdateTab();
    }

    @FXML
    void onUpdateTasksAction(ActionEvent event) {
        setupMyTasksTab();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /** Set's student name and ID and declares a student middleware*/
        studentName.setText(student.getName());
        uiuID.setText(student.getUIU_id()+"  | UMID: " + student.getMember_id());
        uiuID.setStyle("-fx-font-weight: bold;");

        /** sets student club forum information*/
        setUpOrRefreshCfTableView();

        /** Tab Changing features */
        tabSetUpAndChangingMethod();

        /** On the join CF Tab, inits values in the Choicebox list.
         * also, listens for a change in choicebox value
         * */
        initOrRefreshValuesInJoinCfTab();
        refreshFieldsInUpdateTab();

        /** set up my tasks tab */
        setupMyTasksTab();

        /**setup announcement test tab*/
        setupTestAnnouncement();
    }

    private void setupTestAnnouncement() {
        ArrayList<ClubForum> cfs = MemberMiddleware.getClubForum(this.student.getMember_id());
        for(ClubForum c : cfs) {
            HTMLEditor htmlEditor = new HTMLEditor();
            htmlEditors.add(htmlEditor);
            htmlEditor.setPrefHeight(400);
            htmlEditor.setPrefWidth(817);
            htmlEditor.setLayoutY(-68.00);
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.prefWidth(817);
            anchorPane.prefHeight(271);
            anchorPane.getChildren().add(htmlEditor);
            TitledPane pane = new TitledPane(c.getName(), anchorPane);
            announcementAccordion.getPanes().add(pane);
            map.put(c.getName(), htmlEditor);
        }
        HashMap<String, ArrayList<Announcement>> getAllAnnouncements = new HashMap<>();
        for(ClubForum c : cfs) {
            getAllAnnouncements.put(c.getName(), AnnouncementMiddlewares.pullAnnouncementByName(c.getName()));
        }
        for(Map.Entry<String, ArrayList<Announcement>> cfa : getAllAnnouncements.entrySet()) {
            map.get(cfa.getKey()).setHtmlText(getAllAnnouncementsInHtmlForm(cfa.getValue()));
        }
    }

    private String getAllAnnouncementsInHtmlForm(ArrayList<Announcement> announcements) {
        StringBuilder htmlBuilder = new StringBuilder();
        for(Announcement a : announcements) {
            htmlBuilder.append("Posted at ").append(a.getDateTime()).append(":\n").append(a.getBody());
        }

        return htmlBuilder.toString();
    }

    // TODO: finish implementing the dynamic task list UI
    private void setupMyTasksTab() {
        AtomicInteger count = new AtomicInteger();

        tasks = TaskMiddleware.getTaskDetails(this.student.getMember_id());
        Platform.runLater(() -> {
            for(Task t : tasks) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                if(t.getDeadline().equals(dateFormat.format(date))) {
                    TaskMiddleware.rejectTask(t.getTaskId());
                    continue;
                }
                ClubForum cf = CFMiddleware.getCFById(t.getAssignee());
                CheckBox checkBox = new CheckBox();
                checkBox.setText(cf.getName()+" assigned " + t.getName());
                checkBox.setStyle("-fx-font-family: Montserrat");
                checkBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        TaskMiddleware.completeTaskAndSendToHR(t.getTaskId());
                        Platform.runLater(() -> {
                            tasks.clear();
                            tasksList.getItems().clear();
                            setupMyTasksTab();
                        });
                    }
                });
                Button button = new Button("Details");
                button.setStyle("-fx-font-family: Montserrat; -fx-background-color: orange; -fx-font-weight: bold; -fx-text-fill: white; -fx-cursor: hand;");
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        StudentDashboardController.super.getViewFactory().showTaskDetailsSubWindow(t);
                    }
                });
                HBox hBox = new HBox();
                hBox.setSpacing(10);
                hBox.getChildren().addAll(checkBox, button);
                hBox.setAlignment(Pos.CENTER_LEFT);
                tasksList.getItems().add(hBox);
                count.getAndIncrement();
            }

            if(count.get() > 0) {
                tasksLabel.setText("You have " + count.get() + " tasks remaining.");
                tasksList.setVisible(true);
            } else if (count.get() == 0) {
                tasksLabel.setText("No assigned tasks remaining!");
                tasksList.setVisible(false);
            }
        });
    }

    /** HELPERS **/

    private void setUpOrRefreshCfTableView() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                cfNameColumn.setCellValueFactory(new PropertyValueFactory<>("CfName"));
                cfDesignationColumn.setCellValueFactory(new PropertyValueFactory<>("Designation"));
                cfDepartmentCol.setCellValueFactory(new PropertyValueFactory<>("Department"));
                feeColumn.setCellValueFactory(new PropertyValueFactory<>("RegFeeRemaining"));
                customiseFactory(feeColumn);
                cfTable.setItems(FXCollections.observableArrayList(MemberMiddleware.getMyClubForums(student.getMember_id())));
            }
        });
    }

    private void customiseFactory(TableColumn<Member, String> msData) {
        msData.setCellFactory(column -> {
            return new TableCell<Member, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    setText(empty ? "" : getItem().toString());
                    setGraphic(null);


                    if (!isEmpty()) {
                        if(item.equals("Completed"))
                            this.setStyle("-fx-background-color: #51e173; -fx-alignment: center; -fx-font-family: Montserrat;");
                        else
                            this.setStyle("-fx-background-color: white; -fx-alignment: center; -fx-font-family: Montserrat;");
                    }
                }
            };
        });
    }

    private void tabSetUpAndChangingMethod() {
        tabPane.setStyle("-fx-background-color: white;");
        tabPane.getStyleClass().add("floating");
        myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
        myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Platform.runLater(() -> {
                if(newTab.equals(myCFTab)) {
                    myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(myAnnouncementTab)) {
                    myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(joinCFTab)) {
                    joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(updateInfoTab)) {
                    updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(myTasksTab)) {
                    updateInfoTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myAnnouncementTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    joinCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myCFTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    myTasksTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                }
            });

        });
    }

    private void initOrRefreshValuesInJoinCfTab() {
        cfSelectorList.setItems(FXCollections.observableArrayList(CFMiddleware.getAllCFNamesWhoAreRecruitingAndMemberNotInIt(student)));
        joinReasonTextArea.setText("");
        cfSelectorList.setOnAction(actionEvent -> {
            Platform.runLater(() -> {
                if(cfSelectorList.getValue() != null) {
                    joinBtn.setText("Join "+cfSelectorList.getValue());
                } else {
                    joinBtn.setText("Join");
                }
            });
        });
    }

    private void clearAllUpdateTabFields() {
        newUsernameField.setText("");
        newEmailField.setText("");
        newPhoneNumberField.setText("");
        newPasswordField.setText("");
    }

    private void refreshFieldsInJoinCFTab() {
        Platform.runLater(() -> {
            cfSelectorList.getSelectionModel().clearSelection();
            joinBtn.setText("Join");
        });
    }

    private void refreshFieldsInUpdateTab() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                newUsernameField.setPromptText("Current: "+student.getUsername());
                newEmailField.setPromptText("Current: " +student.getEmail());
                newPhoneNumberField.setPromptText("Current: " +student.getPhone());
                newPasswordField.setPromptText("Current: " +student.getPassword());
            }
        });
    }
}
