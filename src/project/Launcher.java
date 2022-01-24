package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.socket.Server;
import project.views.ViewFactory;

public class Launcher extends Application {

    @Override
    public void start(Stage stage) {
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showStudentLoginView();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
