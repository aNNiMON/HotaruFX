package com.annimon.hotarufx.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.val;

public class FileManager implements DocumentManager {

    private static final String FILE_OPEN_TITLE = "Select file to open";
    private final FileChooser fileChooser;
    private File currentFile;
    private DocumentListener listener;

    public FileManager() {
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().setAll(
                new FileChooser.ExtensionFilter("Supported Files", "*.hfx")
        );
    }

    @Override
    public Optional<String> name() {
        return Optional.ofNullable(currentFile)
                .map(File::getName);
    }

    @Override
    public void newDocument() {
        currentFile = null;
    }

    @Override
    public boolean open(Stage stage, Consumer<String> contentConsumer) {
        fileChooser.setTitle(FILE_OPEN_TITLE);
        if (currentFile != null) {
            fileChooser.setInitialDirectory(currentFile.getParentFile());
        }
        currentFile = fileChooser.showOpenDialog(stage);
        if (currentFile == null) {
            return false;
        }
        if (!currentFile.exists() || !currentFile.isFile()) {
            return false;
        }

        val content = readFile(currentFile);
        if (content.isEmpty()) {
            return false;
        }
        contentConsumer.accept(content);
        return true;
    }

    @Override
    public void addDocumentListener(DocumentListener listener) {
        this.listener = listener;
    }

    private String readFile(File file) {
        try (InputStream is = new FileInputStream(file)) {
            return IOStream.readContent(is);
        } catch (IOException ioe) {
            if (listener != null) {
                listener.logError("Unable to open file " + file.getName());
            }
            return "";
        }
    }
}
