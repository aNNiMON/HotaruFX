package com.annimon.hotarufx;

import com.annimon.hotarufx.exceptions.Exceptions;
import com.annimon.hotarufx.ui.control.ClickableHyperLink;
import com.annimon.hotarufx.ui.controller.EditorController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class Main extends Application {

    private EditorController controller;

    @Override
    public void start(Stage primaryStage) {
        ClickableHyperLink.setHostServices(getHostServices());
        primaryStage.setTitle("HotaruFX");
        try {
            final var loader = new FXMLLoader(getClass().getResource("/fxml/Editor.fxml"));
            final var scene = new Scene(loader.load());
            scene.getStylesheets().addAll(
                    getClass().getResource("/styles/theme-dark.css").toExternalForm(),
                    getClass().getResource("/styles/codearea.css").toExternalForm(),
                    getClass().getResource("/styles/hotarufx-keywords.css").toExternalForm(),
                    getClass().getResource("/styles/color-picker-box.css").toExternalForm()
                    );
            controller = loader.getController();
            controller.setPrimaryStage(primaryStage);
            primaryStage.setOnCloseRequest(controller::onCloseRequest);
            primaryStage.setScene(scene);
        } catch (IOException ex) {
            final var text = new TextArea(Exceptions.stackTraceToString(ex));
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
