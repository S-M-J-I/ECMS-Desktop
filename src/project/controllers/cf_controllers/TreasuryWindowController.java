package project.controllers.cf_controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import project.controllers.BaseController;
import project.enums.CSSTabPath;
import project.middlewares.*;
import project.models.ClubForum;
import project.models.Transaction;
import project.views.ViewFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;

public class TreasuryWindowController extends BaseController implements Initializable {
    private ClubForum clubForum;

    public TreasuryWindowController(ViewFactory viewFactory, String fxmlName, ClubForum clubForum) {
        super(viewFactory, "ui/cf_views/"+fxmlName);
        this.clubForum = clubForum;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpTabsAndListenForChanges();
        setupTab.setDisable(this.clubForum.getAccounts() != -1);
        if(!setupTab.isDisable()) {
            accountsTab.setDisable(true);
            feeTrackingTab.setDisable(true);
            setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
            accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        } else {
            accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
            feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
            transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
        }
        fundValueField.setText(Double.toString(this.clubForum.getAccounts())+" BDT");
        registrationFeeField.setText(Double.toString(this.clubForum.getRegistrationFee()));
        accountsReceivableField.setText(Double.toString(CFMiddleware.getAccountsReceivable(this.clubForum)));

        setUpTransactionTab();

        setUpTransactionReportsTab();

        setUpPendingFeeListView();
    }

    private void setUpTransactionReportsTab() {
        transIDCol.setCellValueFactory(new PropertyValueFactory<>("IdString"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("Type"));
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("Category"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("AmountString"));
        reasonCol.setCellValueFactory(new PropertyValueFactory<>("Reason"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("Date"));
        filterTransactionChoiceBox.setItems(FXCollections.observableArrayList("Show All", "Income", "Expense", "General", "Registration Fee", "Accounts Payable"));
        fromMonthChoiceBox.setItems(FXCollections.observableArrayList(months()));
        toMonthChoiceBox.setItems(FXCollections.observableArrayList(months()));
        fromYearChoiceBox.setItems(FXCollections.observableArrayList(years()));
        toYearChoiceBox.setItems(FXCollections.observableArrayList(years()));
    }

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab accountsTab;

    @FXML
    private Label fundValueField;

    @FXML
    private Label accountsReceivableField;

    @FXML
    private Tab transactionsTab;

    @FXML
    private TextField addBalanceField;

    @FXML
    private ChoiceBox<String> addTransactionTypeChoiceBox;

    @FXML
    private CheckBox allowMultipleTransactionsCheckBox;

    @FXML
    private TextField enrolIdsField;

    @FXML
    private ChoiceBox<String> subtractTransactionTypeChoiceBox;

    @FXML
    private Label enrolIdLabel;

    @FXML
    private ChoiceBox<String> enrolIdChoiceBox;

    @FXML
    private Button addTransactionBtn;

    @FXML
    private Button subtractTransactionBtn;


    @FXML
    private TextField subtractBalanceField;

    @FXML
    private TextArea addBalanceReason;

    @FXML
    private TextArea subtractBalanceReason;

    @FXML
    private Label addSuccessLabel;

    @FXML
    private Label subtractSuccessLabel;

    @FXML
    private Tab feeTrackingTab;

    @FXML
    private TextField registrationFeeField;

    @FXML
    private ListView<CheckBox> pendingRegFees;

    @FXML
    private Tab setupTab;

    @FXML
    private TextField initBalanceField;


    /** Transaction reports tab */
    @FXML
    private Tab transactionReportsTab;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> transIDCol;

    @FXML
    private TableColumn<Transaction, String> typeCol;

    @FXML
    private TableColumn<Transaction, String> categoryCol;

    @FXML
    private TableColumn<Transaction, String> amountCol;

    @FXML
    private TableColumn<Transaction, String> reasonCol;

    @FXML
    private TableColumn<Transaction, String> dateCol;

    @FXML
    private ChoiceBox<String> filterTransactionChoiceBox;

    @FXML
    private ChoiceBox<String> fromMonthChoiceBox;

    @FXML
    private ChoiceBox<String> fromYearChoiceBox;

    @FXML
    private ChoiceBox<String> toMonthChoiceBox;

    @FXML
    private ChoiceBox<String> toYearChoiceBox;

    @FXML
    private Button searchTransactionBtn;

    @FXML
    private Button exportTableBtn;

    @FXML
    void onBackAction(ActionEvent event) {
        super.getViewFactory().showCFDashboard(clubForum);
        super.getViewFactory().closeWindow((Stage) initBalanceField.getScene().getWindow());
    }

    @FXML
    void onAddBalanceAction(ActionEvent event) {
        ArrayList<Integer> ids = new ArrayList<>();
        double getAddBalance = Double.parseDouble(addBalanceField.getText());
        if(addTransactionTypeChoiceBox.getValue().equals("Registration Fee")) {
            if(allowMultipleTransactionsCheckBox.isSelected()) {
                String[] s = enrolIdsField.getText().split(" ");
                for(String str : s) {
                    ids.add(Integer.parseInt(str));
                }
                try {
                    HashMap<Integer, Double> idFeeMap = MemberMiddleware.getMultipleMembersFee(ids, this.clubForum.getForumID());
                    MemberMiddleware.updateFeesGiven(getAddBalance, idFeeMap, ids, this.clubForum.getForumID());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    addSuccessLabel.setText("Something went wrong!");
                    addSuccessLabel.setTextFill(Color.RED);
                    return;
                }
            } else {
                int id = Integer.parseInt(enrolIdChoiceBox.getValue());
                try {
                    double oldFeeValue = MemberMiddleware.getMembersFee(id, clubForum.getForumID());
                    MemberMiddleware.updateFeeGiven(getAddBalance + oldFeeValue, id, clubForum.getForumID());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    addSuccessLabel.setText("Something went wrong!");
                    addSuccessLabel.setTextFill(Color.RED);
                    return;
                }
            }
            Platform.runLater(() -> {
                pendingRegFees.getItems().clear();
                setUpPendingFeeListView();
            });
        }
        CFMiddleware.updateBalance((getAddBalance + this.clubForum.getAccounts()), this.clubForum.getForumID());
        this.clubForum.setAccounts((getAddBalance + this.clubForum.getAccounts()));

        if(allowMultipleTransactionsCheckBox.isSelected()) {
            TransactionMiddlewares.addMultipleTransactions(
                    addTransactionTypeChoiceBox.getValue(),
                    "Income",
                    getAddBalance,
                    (addTransactionTypeChoiceBox.getValue().equals("Registration Fee") ? "Registration Fee for " : addBalanceReason.getText()),
                    theMonth(getCurrentMonth()),
                    Integer.parseInt(getCurrentYear()),
                    this.clubForum.getForumID(),
                    ids
            );
        } else {
            TransactionMiddlewares.addTransaction(
                    addTransactionTypeChoiceBox.getValue(),
                    "Income",
                    getAddBalance,
                    (addTransactionTypeChoiceBox.getValue().equals("Registration Fee") ? "Registration Fee for " + MemberMiddleware.getMemberDetailsByMemberId(Integer.parseInt(enrolIdChoiceBox.getValue())).getName() +", "+MemberMiddleware.getMemberDetailsByEnrolId(Integer.parseInt(enrolIdChoiceBox.getValue())).getUIUid()  : addBalanceReason.getText()),
                    theMonth(getCurrentMonth()),
                    Integer.parseInt(getCurrentYear()),
                    this.clubForum.getForumID()
            );
        }

        addSuccessLabel.setText("Transaction complete!");
        addSuccessLabel.setTextFill(Color.GREEN);
        Platform.runLater(() -> {
            fundValueField.setText(Double.toString(this.clubForum.getAccounts())+" BDT");
            addBalanceField.setText("");
            addBalanceReason.setText("");
        });
    }

    @FXML
    void onSubtractBalanceAction(ActionEvent event) {
        double getSubtractBalance = Double.parseDouble(subtractBalanceField.getText());

        // TODO: Accounts Payable (handle the transaction as well as the payed transaction

        if(getSubtractBalance > this.clubForum.getAccounts() || (this.clubForum.getAccounts() - getSubtractBalance) < 0) {
            subtractSuccessLabel.setText("Illogical Amount!");
            subtractSuccessLabel.setTextFill(Color.RED);
            return;
        }
        CFMiddleware.updateBalance((this.clubForum.getAccounts() - getSubtractBalance), this.clubForum.getForumID());
        this.clubForum.setAccounts((this.clubForum.getAccounts() - getSubtractBalance));

        TransactionMiddlewares.addTransaction(
                subtractTransactionTypeChoiceBox.getValue(),
                "Expense",
                -getSubtractBalance,
                subtractBalanceReason.getText(),
                theMonth(getCurrentMonth()),
                Integer.parseInt(getCurrentYear()),
                this.clubForum.getForumID()
        );

        subtractSuccessLabel.setText("Transaction complete!");
        subtractSuccessLabel.setTextFill(Color.GREEN);
        Platform.runLater(() -> {
            fundValueField.setText(Double.toString(this.clubForum.getAccounts())+" BDT");
            subtractBalanceField.setText("");
            subtractBalanceReason.setText("");
        });
    }


    @FXML
    void onExportTableAction(ActionEvent event) {
        ExcelExport xls = new ExcelExport();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("excel files (*.xls)", "*.xls"));

        File file = fileChooser.showSaveDialog((Stage) subtractBalanceReason.getScene().getWindow());
        if(file != null) {
            xls.export(transactionsTable, "Transaction Reports for " + this.clubForum.getName(), file);
        }

    }

    @FXML
    void onSearchForTransactions(ActionEvent event) {
        ArrayList<Transaction> transactions;

        if(filterTransactionChoiceBox.getValue().equals("Show All")) {
            transactions = TransactionMiddlewares.getAllTransactions(
                    this.clubForum.getForumID(),
                    theMonth(fromMonthChoiceBox.getValue()),
                    Integer.parseInt(fromYearChoiceBox.getValue()),
                    theMonth(toMonthChoiceBox.getValue()),
                    Integer.parseInt(toYearChoiceBox.getValue())
            );
        } else if (filterTransactionChoiceBox.getValue().equals("Income") || filterTransactionChoiceBox.getValue().equals("Expense")) {
            transactions = TransactionMiddlewares.getTransactionsByCategory(
                    this.clubForum.getForumID(),
                    filterTransactionChoiceBox.getValue(),
                    theMonth(fromMonthChoiceBox.getValue()),
                    Integer.parseInt(fromYearChoiceBox.getValue()),
                    theMonth(toMonthChoiceBox.getValue()),
                    Integer.parseInt(toYearChoiceBox.getValue())
            );
        } else {
            transactions = TransactionMiddlewares.getTransactionsByFiltering(
                    this.clubForum.getForumID(),
                    filterTransactionChoiceBox.getValue(),
                    theMonth(fromMonthChoiceBox.getValue()),
                    Integer.parseInt(fromYearChoiceBox.getValue()),
                    theMonth(toMonthChoiceBox.getValue()),
                    Integer.parseInt(toYearChoiceBox.getValue())
            );
        }

        Platform.runLater(() -> {
            transactionsTable.getItems().clear();
            transactionsTable.setItems(FXCollections.observableArrayList(transactions));
        });
    }


    @FXML
    void onSetRegistrationFeeAction(ActionEvent event) {
        double getRegFee = Double.parseDouble(registrationFeeField.getText());
        CFMiddleware.updateRegFee(getRegFee, this.clubForum.getForumID());
        this.clubForum.setRegistrationFee(getRegFee);
        Platform.runLater(() -> {
            registrationFeeField.setText(Double.toString(getRegFee));
        });
    }

    @FXML
    void onUpdateRegFeeList(ActionEvent event) {
        ArrayList<Integer> ids = new ArrayList<>();
        for(CheckBox checkBox : pendingRegFees.getItems()) {
            if(checkBox.isSelected()) {
                int id = Integer.parseInt(checkBox.getText().split(":")[0]);
                ids.add(id);
            }
        }
        CFMiddleware.updateCompletedFees(ids, clubForum.getForumID());
        pendingRegFees.getItems().clear();
        Platform.runLater(() -> {
            setUpPendingFeeListView();
            accountsReceivableField.setText(Double.toString(CFMiddleware.getAccountsReceivable(this.clubForum)));
        });
    }


    @FXML
    void onSetupAction(ActionEvent event) {
        double initBalance = Double.parseDouble(initBalanceField.getText());
        CFMiddleware.updateBalance(initBalance, this.clubForum.getForumID());
        this.clubForum.setAccounts(initBalance);
        setupTab.setDisable(true);
        accountsTab.setDisable(false);
        feeTrackingTab.setDisable(false);
        Platform.runLater(() -> {
            fundValueField.setText(Double.toString(this.clubForum.getAccounts()) + " BDT");
        });
    }


    private void setUpTransactionTab() {
        addTransactionTypeChoiceBox.setItems(FXCollections.observableArrayList("General", "Registration Fee"));
        addTransactionTypeChoiceBox.setValue("General");
        subtractTransactionTypeChoiceBox.setItems(FXCollections.observableArrayList("General", "Accounts Receivable"));
        subtractTransactionTypeChoiceBox.setValue("General");
        addTransactionBtn.setDisable(true);
        subtractTransactionBtn.setDisable(true);
        allowMultipleTransactionsCheckBox.setVisible(false);
        enrolIdsField.setVisible(false);
        addBalanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                addTransactionBtn.setDisable(addBalanceField.getText().equals(""));
            }
        });
        subtractBalanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                subtractTransactionBtn.setDisable(subtractBalanceField.getText().equals(""));
            }
        });
        addTransactionTypeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(addTransactionTypeChoiceBox.getValue().equals("Registration Fee")) {
                    enrolIdLabel.setVisible(true);
                    enrolIdChoiceBox.setVisible(true);
                    allowMultipleTransactionsCheckBox.setVisible(true);
                    addBalanceReason.setDisable(true);
                } else {
                    enrolIdLabel.setVisible(false);
                    enrolIdChoiceBox.setVisible(false);
                    addBalanceReason.setDisable(false);
                }
            }
        });
        allowMultipleTransactionsCheckBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(allowMultipleTransactionsCheckBox.isSelected()) {
                    enrolIdChoiceBox.setVisible(false);
                    enrolIdsField.setVisible(true);
                } else {
                    enrolIdChoiceBox.setVisible(true);
                    enrolIdsField.setVisible(false);
                }
            }
        });
        enrolIdChoiceBox.setItems(FXCollections.observableArrayList(MemberMiddleware.getAllMemberUMIds(this.clubForum.getForumID())));
        enrolIdLabel.setVisible(false);
        enrolIdChoiceBox.setVisible(false);
    }



    private void setUpPendingFeeListView() {
        HashMap<Integer, Double> regFeeMap = CFMiddleware.getPendingRegFees(this.clubForum);
        for(Map.Entry<Integer, Double> fees : regFeeMap.entrySet()) {
            CheckBox checkBox = new CheckBox();
            checkBox.setDisable(true);
            checkBox.setText(fees.getKey()+": "+MemberMiddleware.getMemberDetailsByMemberId(fees.getKey()).getName()+"("+fees.getValue()+")");
            if(fees.getValue() == 0) {
                checkBox.setTextFill(Color.GREEN);
                checkBox.setDisable(false);
            }
            pendingRegFees.getItems().add(checkBox);
        }
    }

    private ArrayList<String> months() {
        ArrayList<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        return months;
    }

    private ArrayList<String> years() {
        ArrayList<String> years = new ArrayList<>();
        years.add("2022");
        years.add("2023");
        years.add("2024");
        years.add("2025");
        years.add("2026");
        years.add("2027");
        years.add("2028");
        years.add("2029");
        years.add("2030");

        return years;
    }

    private int theMonth(String month){
        ArrayList<String> months = months();
        int count = 1;
        for(String m : months) {
            if(month.equals(m)) {
                return count;
            }
            count++;
        }
        return -1;
    }

    private String getCurrentMonth() {
        Calendar mCalendar = Calendar.getInstance();
        return mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
    }

    private String getCurrentYear() {
        return String.valueOf(new Date().getYear() + 1900);
    }

    private void setUpTabsAndListenForChanges() {
        tabPane.setStyle("-fx-background-color: white;");
        tabPane.getStyleClass().add("floating");
        tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
            Platform.runLater(() -> {
                if(newTab.equals(setupTab)) {
                    setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if(newTab.equals(accountsTab)) {
                    setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(feeTrackingTab)) {
                    setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(transactionsTab)) {
                    setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                    feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                } else if (newTab.equals(transactionReportsTab)) {
                    setupTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    accountsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    feeTrackingTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.INACTIVE));
                    transactionReportsTab.setStyle(CSSTabPath.getCSSPath(CSSTabPath.ACTIVE));
                }
            });
        });
    }
}
