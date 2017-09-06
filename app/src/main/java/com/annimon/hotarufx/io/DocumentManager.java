package com.annimon.hotarufx.io;

import java.util.Optional;
import java.util.function.Consumer;
import javafx.stage.Stage;

public interface DocumentManager {

    Optional<String> name();

    void newDocument();

    boolean open(Stage stage, Consumer<String> contentConsumer);


    void addDocumentListener(DocumentListener listener);
}
