package com.annimon.hotarufx.ui.controller;

import com.annimon.hotarufx.Main;
import com.annimon.hotarufx.exceptions.Exceptions;
import com.annimon.hotarufx.exceptions.RendererException;
import com.annimon.hotarufx.io.DocumentListener;
import com.annimon.hotarufx.io.DocumentManager;
import com.annimon.hotarufx.io.FileManager;
import com.annimon.hotarufx.io.IOStream;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.ui.FontAwesome;
import com.annimon.hotarufx.ui.FontAwesomeIcon;
import com.annimon.hotarufx.ui.RenderPreparer;
import com.annimon.hotarufx.ui.SyntaxHighlighter;
import com.annimon.hotarufx.ui.control.LibraryItem;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

@SuppressWarnings("unused")
public class EditorController implements Initializable, DocumentListener {

    private static final int DEFAULT_FONT_SIZE = 14;

    @FXML
    private Menu examplesMenu;

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
    private int fontSize = DEFAULT_FONT_SIZE;

    @FXML
    private void handleMenuNew(ActionEvent event) {
        documentManager.newDocument();
        openExample();
        updateTitle();
    }

    @FXML
    private void handleMenuOpen(ActionEvent event) {
        final var isOpened = documentManager.open(primaryStage, editor::replaceText);
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
        if (confirmExit()) {
            primaryStage.close();
        }
    }

    public void onCloseRequest(WindowEvent windowEvent) {
        if (!confirmExit()) {
            windowEvent.consume();
        }
    }

    private boolean confirmExit() {
        final var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.initOwner(primaryStage);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.getDialogPane().setContent(new Group());
        final var icon = new FontAwesomeIcon(FontAwesome.QUESTION_CIRCLE);
        alert.getDialogPane().setGraphic(icon);
        return alert.showAndWait()
                .filter(b -> b == ButtonType.OK)
                .isPresent();
    }

    @FXML
    private void handleMenuIncreaseFontSize(ActionEvent event) {
        changeFontSize(+1);
    }

    @FXML
    private void handleMenuDecreaseFontSize(ActionEvent event) {
        changeFontSize(-1);
    }

    private void changeFontSize(int delta) {
        final int newSize = fontSize + delta;
        if (8 > newSize || newSize > 40) return;
        fontSize = newSize;
        editor.setStyle("-fx-font-size: " + newSize + "px");
    }

    @FXML
    private void handleMenuAbout(ActionEvent event) {
        final var stage = new Stage();
        stage.setTitle("About");
        stage.setResizable(false);
        stage.initOwner(primaryStage);
        stage.initModality(Modality.WINDOW_MODAL);
        try {
            final var loader = new FXMLLoader(getClass().getResource("/fxml/About.fxml"));
            final var scene = new Scene(loader.load());
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
        final var input = editor.getText();
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
                        final var timeline = composition.getTimeline();
                        timeline.getFxTimeline().currentTimeProperty().addListener((o, oldValue, d) -> {
                            final var min = (int) d.toMinutes();
                            final var durationSec = d.subtract(Duration.minutes(min));
                            final var sec = (int) durationSec.toSeconds();
                            final var durationMs = durationSec.subtract(Duration.seconds(sec));
                            final var frame = (int) (durationMs.toMillis() * timeline.getFrameRate() / 1000d);
                            final var allFrame = (int) (d.toMillis() * timeline.getFrameRate() / 1000d);
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
        final var input = editor.getText();
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
                        final var chooser = new DirectoryChooser();
                        chooser.setTitle("Choose directory for rendering frames");
                        final var directory = chooser.showDialog(primaryStage);
                        if (directory == null || !directory.exists() || !directory.isDirectory()) {
                            return;
                        }

                        final var fxTimeline = composition.getTimeline().getFxTimeline();
                        stage.setOnShown(e -> {
                            fxTimeline.playFromStart();
                            fxTimeline.pause();
                            final var task = new RenderTask(directory, composition, stage.getScene());
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
        populateExamples();
        initSyntaxHighlighter();
        changeFontSize(0);
        initUndoRedo();
        initCopyCutPaste();
        openExample();
        editor.getUndoManager().forgetHistory();
        initializeLibrary();
        Platform.runLater(editor::requestFocus);
    }

    private void populateExamples() {
        final var map = new LinkedHashMap<String, String>();
        map.put("HotaruFX Logo", "hotarufx-logo.hfx");
        map.put("Algorithm", "algorithm.hfx");
        map.put("Blend Modes", "blend-modes.hfx");
        map.put("Font", "font.hfx");
        map.put("Font Awesome Icons", "font-awesome.hfx");
        map.put("HSV Color", "hsv.hfx");
        map.put("Image", "image.hfx");
        map.put("Image Rotate & Zoom", "image_rotate_zoom.hfx");
        map.put("Line", "line.hfx");
        map.put("Rectangle", "rectangle.hfx");
        map.put("Round Rectangle", "round-rect.hfx");
        map.put("Stroke Ants", "stroke-ants.hfx");
        map.put("Text Clipping", "clip-text.hfx");
        map.put("Text Flow", "text-flow.hfx");
        map.put("Typewriter", "typewriter.hfx");
        examplesMenu.getItems().clear();
        for (final var entry : map.entrySet()) {
            final var item = new MenuItem(entry.getKey());
            item.setOnAction(e -> openExample("/examples/" + entry.getValue()));
            examplesMenu.getItems().add(item);
        }
    }

    private void initSyntaxHighlighter() {
        final var highlightProperty = syntaxHighlightingItem.selectedProperty();
        highlightProperty.addListener((observable, oldValue, highlightEnabled) -> {
            if (highlightEnabled) {
                // create event to reinit highlighter
                final var pos = editor.getCaretPosition();
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
        undoButton.disableProperty().bind(editor.undoAvailableProperty().map(x -> !x));
        redoButton.disableProperty().bind(editor.redoAvailableProperty().map(x -> !x));
        undoButton.setOnAction(editorAction(editor::undo));
        redoButton.setOnAction(editorAction(editor::redo));
    }

    private void initCopyCutPaste() {
        final var selectionEmpty = new BooleanBinding() {
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

    private void openExample() {
        openExample(null);
    }

    private void openExample(String path) {
        final var content = (path != null) ? readProgram(path) : fallbackProgram();
        editor.replaceText(content);
    }

    private String readProgram(String path) {
        try (InputStream is = Main.class.getResourceAsStream(path)) {
            if (is == null) {
                return fallbackProgram();
            }
            return IOStream.readContent(is);
        } catch (IOException ioe) {
            return fallbackProgram();
        }
    }

    private String fallbackProgram() {
        return """
                composition(1280, 720, 30)
                
                A = circle({
                  cx: 0,
                  cy: 0,
                  radius: 100,
                  fill: '#9bc747'
                })
                
                A@radius
                  .add(300 ms, 200)
                  .add(1 sec, 50)
                
                render(A)
                """.stripIndent();
    }
}
