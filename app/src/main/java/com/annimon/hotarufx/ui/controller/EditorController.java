package com.annimon.hotarufx.ui.controller;

import com.annimon.hotarufx.Main;
import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.CompositionBundle;
import com.annimon.hotarufx.bundles.NodesBundle;
import com.annimon.hotarufx.io.DocumentListener;
import com.annimon.hotarufx.io.IOStream;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.parser.HotaruParser;
import com.annimon.hotarufx.parser.visitors.InterpreterVisitor;
import com.annimon.hotarufx.io.DocumentManager;
import com.annimon.hotarufx.io.FileManager;
import com.annimon.hotarufx.ui.SyntaxHighlighter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import lombok.val;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorController implements Initializable, DocumentListener {

    @FXML
    private Button undoButton, redoButton;

    @FXML
    private CodeArea editor;

    @FXML
    private TextArea log;
    @FXML
    private TitledPane logPane;

    private Stage primaryStage;
    private SyntaxHighlighter syntaxHighlighter;
    private DocumentManager documentManager;

    @FXML
    private void handleMenuNew(ActionEvent event) {
        documentManager.newDocument();
        openSample();
        updateTitle();
    }

    @FXML
    private void handleMenuOpen(ActionEvent event) {
        val isOpened = documentManager.open(primaryStage, editor::replaceText);
        if (isOpened) {
            updateTitle();
        }
    }

    @FXML
    private void handleMenuSave(ActionEvent event) {
        documentManager.save(primaryStage, editor::getText);
        updateTitle();
    }

    @FXML
    private void handleMenuSaveAs(ActionEvent event) {
        documentManager.saveAs(primaryStage, editor::getText);
        updateTitle();
    }

    private void updateTitle() {
        primaryStage.setTitle(
                documentManager.name().orElse("HotaruFX"));
    }

    @FXML
    private void handleMenuExit(ActionEvent event) {
        // TODO: confirmation
        Platform.exit();
    }

    @FXML
    private void handleMenuAbout(ActionEvent event) {
    }

    @FXML
    private void handleMenuPlay(ActionEvent event) {
        log.setText("");
        val input = editor.getText();
        if (input.isEmpty()) {
            return;
        }

        val context = new Context();
        BundleLoader.load(context, Arrays.asList(
                CompositionBundle.class,
                NodesBundle.class
        ));

        val parser = new HotaruParser(HotaruLexer.tokenize(input));
        val program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
            log.setText(parser.getParseErrors().toString());
            logPane.setExpanded(true);
            return;
        }
        program.accept(new InterpreterVisitor(), context);

        val stage = new Stage();
        val composition = context.composition();
        if (composition == null) {
            logError("There is no composition.\nMake sure you call composition method.");
            logPane.setExpanded(true);
            return;
        }
        stage.setScene(composition.produceAnimationScene());
        composition.getTimeline().getFxTimeline().play();
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        syntaxHighlighter = new SyntaxHighlighter(editor, Executors.newSingleThreadExecutor());
        syntaxHighlighter.init();
        documentManager = new FileManager();
        initUndoRedo();
        openSample();
        editor.getUndoManager().forgetHistory();
        Platform.runLater(editor::requestFocus);
    }

    private void initUndoRedo() {
        undoButton.disableProperty().bind(
                Bindings.not(editor.undoAvailableProperty()));
        redoButton.disableProperty().bind(
                Bindings.not(editor.redoAvailableProperty()));
        undoButton.setOnAction(a -> editor.undo());
        redoButton.setOnAction(a -> editor.redo());
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void stop() {
        syntaxHighlighter.release();
    }

    @Override
    public void logError(String message) {
        log.insertText(0, message + System.lineSeparator());
    }

    private void openSample() {
        editor.replaceText(
                "composition(1280, 720, 30)\n" +
                "\n" +
                "A = circle({\n" +
                "  cx: 0,\n" +
                "  cy: 0,\n" +
                "  radius: 100,\n" +
                "  fill: '#9bc747'\n" +
                "})\n" +
                "\n" +
                "A@radius\n" +
                "  .add(300 ms, 200)\n" +
                "  .add(1 sec, 50)\n" +
                "\n" +
                "render(A)");
    }

    private String readProgram(String path) {
        val fallbackProgram = "composition(640, 480, 25)";
        try (InputStream is = Main.class.getResourceAsStream(path)) {
            if (is == null) {
                return fallbackProgram;
            }
            return IOStream.readContent(is);
        } catch (IOException ioe) {
            return fallbackProgram;
        }
    }
}
