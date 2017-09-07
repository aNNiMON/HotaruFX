package com.annimon.hotarufx;

import com.annimon.hotarufx.ui.control.ClickableHyperLink;
import com.annimon.hotarufx.ui.controller.EditorController;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    private EditorController controller;

    @Override
    public void start(Stage primaryStage) {
        ClickableHyperLink.setHostServices(getHostServices());
        primaryStage.setTitle("HotaruFX");
        try {
            val loader = new FXMLLoader(getClass().getResource("/fxml/Editor.fxml"));
            val scene = new Scene(loader.load());
            scene.getStylesheets().addAll(
                    getClass().getResource("/styles/theme-dark.css").toExternalForm(),
                    getClass().getResource("/styles/codearea.css").toExternalForm(),
                    getClass().getResource("/styles/hotarufx-keywords.css").toExternalForm(),
                    getClass().getResource("/styles/color-picker-box.css").toExternalForm()
                    );
            controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setScene(scene);
        } catch (IOException ex) {
            val sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            val text = new TextArea(sw.toString());
            text.setEditable(false);
            primaryStage.setScene(new Scene(text));
        }
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        if (controller != null) {
            controller.stop();
        }
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
