package com.annimon.hotarufx;

import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.CompositionBundle;
import com.annimon.hotarufx.bundles.NodesBundle;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.parser.HotaruParser;
import com.annimon.hotarufx.parser.visitors.InterpreterVisitor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.val;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        val input = readProgram("/main.hfx");
        val context = new Context();
        BundleLoader.load(context, Arrays.asList(
                CompositionBundle.class,
                NodesBundle.class
        ));

        HotaruParser.parse(HotaruLexer.tokenize(input))
                .accept(new InterpreterVisitor(), context);

        val composition = context.composition();
        primaryStage.setTitle("HotaruFX");
        primaryStage.setScene(composition.produceAnimationScene());
        composition.getTimeline().getFxTimeline().play();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
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
