package com.fwd.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @Author: fanwd
 * @Description:
 * @Date: Create in 14:08 2018/11/28
 */
public class Test extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox();

        TabPane tabPane = new TabPane();



        ListView<String> listView = new ListView<>();
        Tab tab = new Tab("tab", listView);
        tab.setTooltip(new Tooltip("ttttttttttttttttttttttt"));

        TableView<String> tableView = new TableView<>();
        Tab tableTab = new Tab("table", tableView);

        tabPane.getTabs().addAll(tab, tableTab);

        root.getChildren().addAll(tabPane);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
