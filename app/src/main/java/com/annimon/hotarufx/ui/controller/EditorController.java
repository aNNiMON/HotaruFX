package com.annimon.hotarufx.ui.controller;

import com.annimon.hotarufx.Main;
import com.annimon.hotarufx.exceptions.Exceptions;
import com.annimon.hotarufx.exceptions.RendererException;
import com.annimon.hotarufx.io.DocumentListener;
import com.annimon.hotarufx.io.DocumentManager;
import com.annimon.hotarufx.io.FileManager;
import com.annimon.hotarufx.io.IOStream;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.ui.RenderPreparer;
import com.annimon.hotarufx.ui.SyntaxHighlighter;
import com.annimon.hotarufx.ui.control.LibraryItem;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.val;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

@SuppressWarnings("unused")
public class EditorController implements Initializable, DocumentListener {

    @FXML
    private CheckMenuItem syntaxHighlightingItem;

    @FXML
    private Button undoButton, redoButton, cutButton, copyButton, pasteButton;

    @FXML
    private CodeArea editor;

    @FXML
    private Pane library;

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
        val stage = new Stage();
        stage.setTitle("About");
        stage.setResizable(false);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            val loader = new FXMLLoader(getClass().getResource("/fxml/About.fxml"));
            val scene = new Scene(loader.load());
            scene.getStylesheets().addAll(
                    getClass().getResource("/styles/theme-dark.css").toExternalForm(),
                    getClass().getResource("/styles/about.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showError("Unable to open about window");
        }
    }

    @FXML
    private void handleMenuPlay(ActionEvent event) {
        log.setText("");
        val input = editor.getText();
        if (input.isEmpty()) {
            return;
        }
        try {
            RenderPreparer.init()
                    .input(input)
                    .context(new Context())
                    .evaluateWithRuntimeBundle()
                    .prepareStage(primaryStage)
                    .peek((stage, composition) -> {
                        val timeline = composition.getTimeline();
                        timeline.getFxTimeline().currentTimeProperty().addListener((o, oldValue, d) -> {
                            val min = (int) d.toMinutes();
                            val durationSec = d.subtract(Duration.minutes(min));
                            val sec = (int) durationSec.toSeconds();
                            val durationMs = durationSec.subtract(Duration.seconds(sec));
                            val frame = (int) (durationMs.toMillis() * timeline.getFrameRate() / 1000d);
                            val allFrame = (int) (d.toMillis() * timeline.getFrameRate() / 1000d);
                            stage.setTitle(String.format("%02d:%02d.%02d   %d", min, sec, frame, allFrame));
                        });

                        stage.setOnShown(e -> timeline.getFxTimeline().play());
                        stage.show();
                    });
        } catch (RendererException re) {
            showError(re.getMessage());
        } catch (RuntimeException e) {
            showError(Exceptions.stackTraceToString(e));
        }
    }

    @FXML
    private void handleMenuRender(ActionEvent event) {
        log.setText("");
        val input = editor.getText();
        if (input.isEmpty()) {
            return;
        }
        try {
            RenderPreparer.init()
                    .input(input)
                    .context(new Context())
                    .evaluateForRender()
                    .prepareStage(primaryStage)
                    .peek((stage, composition) -> {
                        val chooser = new DirectoryChooser();
                        chooser.setTitle("Choose directory for rendering frames");
                        val directory = chooser.showDialog(primaryStage);
                        if (directory == null || !directory.exists() || !directory.isDirectory()) {
                            return;
                        }

                        val fxTimeline = composition.getTimeline().getFxTimeline();
                        stage.setOnShown(e -> {
                            fxTimeline.playFromStart();
                            fxTimeline.pause();
                            val task = new RenderTask(directory, composition, stage.getScene());
                            task.messageProperty().addListener(ev -> {
                                stage.setTitle(task.getMessage());
                            });
                            task.setOnFailed(ev -> {
                                showError(Exceptions.stackTraceToString(ev.getSource().getException()));
                                stage.close();
                            });
                            task.setOnSucceeded(ev -> stage.close());
                            new Thread(task).start();
                        });
                        stage.show();
                    });
        } catch (RendererException re) {
            showError(re.getMessage());
        } catch (RuntimeException e) {
            showError(Exceptions.stackTraceToString(e));
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        documentManager = new FileManager();
        initSyntaxHighlighter();
        initUndoRedo();
        initCopyCutPaste();
        openSample();
        editor.getUndoManager().forgetHistory();
        initializeLibrary();
        Platform.runLater(editor::requestFocus);
    }

    private void initSyntaxHighlighter() {
        val highlightProperty = syntaxHighlightingItem.selectedProperty();
        highlightProperty.addListener((observable, oldValue, highlightEnabled) -> {
            if (highlightEnabled) {
                // create event to reinit highlighter
                val pos = editor.getCaretPosition();
                editor.insertText(pos, " ");
                editor.replaceText(pos, pos + 1, "");
            } else {
                editor.clearStyle(0, editor.getLength());
            }
        });
        syntaxHighlighter = new SyntaxHighlighter(editor, Executors.newSingleThreadExecutor());
        syntaxHighlighter.init(highlightProperty);
    }

    private void initUndoRedo() {
        undoButton.disableProperty().bind(
                Bindings.not(editor.undoAvailableProperty()));
        redoButton.disableProperty().bind(
                Bindings.not(editor.redoAvailableProperty()));
        undoButton.setOnAction(editorAction(editor::undo));
        redoButton.setOnAction(editorAction(editor::redo));
    }

    private void initCopyCutPaste() {
        val selectionEmpty = new BooleanBinding() {
            { bind(editor.selectionProperty()); }
            @Override
            protected boolean computeValue() {
                return editor.getSelection().getLength() == 0;
            }
        };
        cutButton.disableProperty().bind(selectionEmpty);
        copyButton.disableProperty().bind(selectionEmpty);

        cutButton.setOnAction(editorAction(editor::cut));
        copyButton.setOnAction(editorAction(editor::copy));
        pasteButton.setOnAction(editorAction(editor::paste));
    }

    private EventHandler<ActionEvent> editorAction(Runnable r) {
        return event -> {
            r.run();
            editor.requestFocus();
        };
    }

    private void initializeLibrary() {
        library.getChildren().stream()
                .map(LibraryItem.class::cast)
                .forEach(item -> item.setOnAction(a -> {
                    editor.insertText(editor.getCaretPosition(), item.getCode());
                    editor.requestFocus();
                }));
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void stop() {
        syntaxHighlighter.release();
    }

    public void showError(String message) {
        logError(message);
        logPane.setExpanded(true);
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
                "render(A)\n");
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
