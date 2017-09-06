package com.annimon.hotarufx.io;

import java.util.function.Consumer;
import javafx.stage.Stage;

public interface DocumentManager {

    void newDocument();

    boolean open(Stage stage, Consumer<String> contentConsumer);


    void addDocumentListener(DocumentListener listener);
}
