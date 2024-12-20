package com.annimon.hotarufx.ui;

import com.annimon.hotarufx.bundles.Bundle;
import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.exceptions.RendererException;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.parser.HotaruParser;
import com.annimon.hotarufx.parser.visitors.InterpreterVisitor;
import com.annimon.hotarufx.visual.Composition;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class RenderPreparer {

    public static RenderPreparer init() {
        return new RenderPreparer();
    }

    private RenderPreparer() {
    }

    public WithInput input(String input) {
        return new WithInput(input);
    }


    public class WithInput {

        private final String input;

        private WithInput(String input) {
            this.input = input;
        }

        public WithContext context(Context context) {
            return new WithContext(this, context);
        }
    }


    public class WithContext {

        private final WithInput source;
        private final Context context;

        private WithContext(WithInput source, Context context) {
            this.source = source;
            this.context = context;
        }

        public Evaluated evaluateWithRuntimeBundle() {
            return evaluateWithBundles(BundleLoader.runtimeBundles());
        }

        public Evaluated evaluateWithBundles(List<Class<? extends Bundle>> bundles) {
            BundleLoader.load(context, bundles);
            evaluate();
            return new Evaluated(this);
        }

        public EvaluatedForRender evaluateForRender() {
            BundleLoader.load(context, BundleLoader.runtimeBundles());
            evaluate();
            return new EvaluatedForRender(this);
        }

        private void evaluate() {
            final var parser = new HotaruParser(HotaruLexer.tokenize(source.input));
            final var program = parser.parse();
            if (parser.getParseErrors().hasErrors()) {
                throw new RendererException(parser.getParseErrors().toString());
            }
            program.accept(new InterpreterVisitor(), context);
        }
    }


    public class Evaluated {

        protected final WithContext source;

        private Evaluated(WithContext source) {
            this.source = source;
        }

        public WithStage prepareStage(Stage primaryStage) {
            checkCompositionExists();
            final var stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            final var composition = source.context.composition();
            stage.setScene(sceneProvider().apply(composition));
            return new WithStage(composition, stage);
        }

        protected Function<Composition, Scene> sceneProvider() {
            return Composition::producePreviewScene;
        }

        private void checkCompositionExists() {
            if (source.context.composition() == null) {
                throw new RendererException("There is no composition.\n" +
                        "Make sure you call composition method.");
            }
        }
    }

    public class EvaluatedForRender extends Evaluated {

        private EvaluatedForRender(WithContext source) {
            super(source);
        }

        @Override
        protected Function<Composition, Scene> sceneProvider() {
            return Composition::produceRendererScene;
        }
    }

    public static class WithStage {

        private final Composition composition;
        private final Stage stage;

        private WithStage(Composition composition, Stage stage) {
            this.composition = composition;
            this.stage = stage;
        }

        public WithStage peek(BiConsumer<Stage, Composition> consumer) {
            consumer.accept(stage, composition);
            return this;
        }
    }
}
