package com.annimon.hotarufx.ui.controller;

import com.annimon.hotarufx.Main;
import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.CompositionBundle;
import com.annimon.hotarufx.bundles.NodesBundle;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.parser.HotaruParser;
import com.annimon.hotarufx.parser.ParseError;
import com.annimon.hotarufx.parser.visitors.InterpreterVisitor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import lombok.val;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

public class EditorController implements Initializable {

    @FXML
    private CodeArea editor;

    @FXML
    private TextArea log;
    @FXML
    private TitledPane logPane;

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

        val context = new Context();
        BundleLoader.load(context, Arrays.asList(
                CompositionBundle.class,
                NodesBundle.class
        ));

        val parser = new HotaruParser(HotaruLexer.tokenize(input));
        val program = parser.parse();
        if (parser.getParseErrors().hasErrors()) {
            val sb = new StringBuilder();
            for (ParseError parseError : parser.getParseErrors()) {
                sb.append(parseError);
            }
            log.setText(sb.toString());
            logPane.setExpanded(true);
            return;
        }
        program.accept(new InterpreterVisitor(), context);

        val stage = new Stage();
        val composition = context.composition();
        stage.setScene(composition.produceAnimationScene());
        composition.getTimeline().getFxTimeline().play();
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editor.setParagraphGraphicFactory(LineNumberFactory.get(editor));
        editor.replaceText(0, 0, readProgram("/main.hfx"));
    }

    private static String readProgram(String path) {
        val fallbackProgram = "composition(640, 480, 25)";
        try (InputStream is = Main.class.getResourceAsStream(path)) {
            if (is == null) {
                return fallbackProgram;
            }
            val baos = new ByteArrayOutputStream();
            val bufferSize = 4096;
            val buffer = new byte[bufferSize];
            int read;
            while ((read = is.read(buffer, 0, bufferSize)) != -1)  {
                baos.write(buffer, 0, read);
            }
            return baos.toString("UTF-8");
        } catch (IOException ioe) {
            return fallbackProgram;
        }
    }
}
