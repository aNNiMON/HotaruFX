package com.annimon.hotarufx.io;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.stage.Stage;

public interface DocumentManager {

    Optional<String> name();

    void newDocument();

    boolean open(Stage stage, Consumer<String> contentConsumer);

    boolean save(Stage stage, Supplier<String> contentSupplier);

    boolean saveAs(Stage stage, Supplier<String> contentSupplier);


    void addDocumentListener(DocumentListener listener);
}
