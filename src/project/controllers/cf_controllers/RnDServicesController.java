package project.controllers.cf_controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.CSSTabPath;
import project.middlewares.MemberMiddleware;
import project.middlewares.TaskMiddleware;
import project.models.ClubForum;
import project.models.Member;
import project.models.Performers;
import project.views.ViewFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ResourceBundle;

public class RnDServicesController extends BaseController implements Initializable {
    private ClubForum clubForum;
    private int tasksDone;
    private int tasksGiven;
    ArrayList<Performers> members;

    public RnDServicesController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
        this.tasksDone = this.tasksGiven = 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        members = TaskMiddleware.getAllPerformers(this.clubForum.getForumID());
        setUpAnalysisTab();
        setUpPerformersTab();

        setUpTabsAndListenForChanges();
    }


    @FXML
    private TabPane tabPane;

    @FXML
    private Tab prodAnalysisTab;

    @FXML
    private Label noDataLabel;

    @FXML
    private Label productivityLabel;

    @FXML
    private PieChart pieChart;


    @FXML
    private Tab performersAnalysisTab;
    @FXML
    private Label noDataLabel2;

    @FXML
    private Label bPTextLabel;

    @FXML
    private Label wPTextLabel;

    @FXML
    private Label bestPerformerLabel;

    @FXML
    private Label worstPerformerLabel;

    @FXML
    private Slider bestPerformerIndex;

    @FXML
    private Label bestPerformerLabel2;

    @FXML
    private Label bestPerformerLabel3;

    @FXML
    private Slider worstPerformerIndex;

    @FXML
    private Label worstPerformerLabel2;

    @FXML
    private Label worstPerformerLabel3;

    @FXML
    void onBackAction(ActionEvent event) {
        super.getViewFactory().showCFDashboard(clubForum);
        super.getViewFactory().closeWindow((Stage) productivityLabel.getScene().getWindow());
    }

    /** Helpers and setters */

    private void setUpAnalysisTab() {
        if(members.size() == 0) {
            noDataLabel.setText("Not enough data");
            noDataLabel.setTextFill(Color.RED);
            pieChart.setVisible(false);
            productivityLabel.setText("0.00%");
            return;
        }
        this.tasksDone = TaskMiddleware.getTasksDone(this.clubForum.getForumID());
        this.tasksGiven = TaskMiddleware.getTasksGiven(this.clubForum.getForumID());
        PieChart.Data slice1 = new PieChart.Data("Done", this.tasksDone);
        PieChart.Data slice2 = new PieChart.Data("Not Done"  , (this.tasksGiven - this.tasksDone));
        pieChart.getData().add(slice1);
        pieChart.getData().add(slice2);
        pieChart.setVisible(true);
        double productivity = ((double) this.tasksDone / (double) this.tasksGiven) * 100;
        productivityLabel.setText(String.format("%.2f", productivity)+"%");
    }

    private void setUpPerformersTab() {
        if(members.size() == 0) {
            noDataLabel2.setText("Not enough data");
            noDataLabel2.setTextFill(Color.RED);
            bestPerformerIndex.setVisible(false);
            worstPerformerIndex.setVisible(false);
            bPTextLabel.setVisible(false);
            wPTextLabel.setVisible(false);
            return;
        }
        members.sort(new Comparator<Performers>() {
            @Override
            public int compare(Performers o1, Performers o2) {
                return Double.compare(o2.getProd(), o1.getProd());
            }
        });
        bestPerformerLabel.setText(MemberMiddleware.getMemberDetailsByEnrolId(members.get(0).getId()).getName());
        worstPerformerLabel.setText(MemberMiddleware.getMemberDetailsByEnrolId(members.get(members.size()-1).getId()).getName());

        ArrayList<Label> bestPerformerLabels = new ArrayList<>();
        bestPerformerLabels.add(bestPerformerLabel);
        bestPerformerLabels.add(bestPerformerLabel2);
        bestPerformerLabels.add(bestPerformerLabel3);

        ArrayList<Label> worstPerformerLabels = new ArrayList<>();
        worstPerformerLabels.add(worstPerformerLabel);
        worstPerformerLabels.add(worstPerformerLabel2);
        worstPerformerLabels.add(worstPerformerLabel3);

        bestPerformerIndex.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(Label l : bestPerformerLabels) {
                    l.setText("");
                }
                double count = bestPerformerIndex.getValue();
                for(int i = 0; i < count; ++i) {
                    bestPerformerLabels.get(i).setText(MemberMiddleware.getMemberDetailsByEnrolId(members.get(i).getId()).getName());
                }
            }
        });

        worstPerformerIndex.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                for(Label l : worstPerformerLabels) {
                    l.setText("");
                }
                double count = worstPerformerIndex.getValue();
                for(int i = 0; i < count; ++i) {
                    worstPerformerLabels.get(i).setText(MemberMiddleware.getMemberDetailsByEnrolId(members.get(members.size()-i-1).getId()).getName());
                }
            }
        });
    }


    private void setUpTabsAndListenForChanges() {
        tabPane.setStyle("-fx-background-color: white;");
        tabPane.getStyleClass().add("floating");
        prodAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
        performersAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Platform.runLater(() -> {
                if(newTab.equals(prodAnalysisTab)) {
                    prodAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    performersAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if(newTab.equals(performersAnalysisTab)) {
                    prodAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    performersAnalysisTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                }
            });
        });
    }


}
