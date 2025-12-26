package main.java.com.filesyncpro;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import java.io.File;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Root Container
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));

        // Navigation/Header Area
        Label headerLabel = new Label("Home");
        headerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Separator headSeparator = new Separator();

        // Input Grid (Source and Destination)
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);

        // Source Row
        Label lblSource = new Label("Source: ");
        TextField txtSource = new TextField();
        txtSource.setPrefWidth(350);
        Button btnBrowseSource = new Button("Browse...");
        btnBrowseSource.setOnAction(e -> selectDirectory(primaryStage, txtSource));

        grid.add(lblSource, 0, 0);
        grid.add(txtSource, 1, 0);
        grid.add(btnBrowseSource, 2, 0);

        // Destination Row
        Label lblDest = new Label("Destination: ");
        TextField txtDest = new TextField();
        Button btnBrowseDest = new Button("Browse...");
        btnBrowseDest.setOnAction(e -> selectDirectory(primaryStage, txtDest));

        grid.add(lblDest, 0, 1);
        grid.add(txtDest, 1, 1);
        grid.add(btnBrowseDest, 2, 1);

        // Button Action Buttons
        HBox buttonBar = new HBox(10);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);

        Button btnIncr = new Button("Incremental Sync");
        Button btnFull = new Button("Full Sync");
        Button btnExit = new Button("Exit");
        btnExit.setOnAction(e -> primaryStage.close());

        // Exit button spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        buttonBar.getChildren().addAll(btnIncr, btnFull, spacer, btnExit);

        root.getChildren().addAll(headerLabel, headSeparator, grid, new Separator(), buttonBar);

        System.out.println("JavaFX and CLI Running...");

        Scene scene = new Scene(root, 600, 300);
        primaryStage.setTitle("FileSync Pro");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void selectDirectory(Stage stage, TextField target) {
        DirectoryChooser dc = new DirectoryChooser();
        File selected = dc.showDialog(stage);
        if (selected != null)
            target.setText(selected.getAbsolutePath());
    }
}
