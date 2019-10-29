package com.annimon.hotarufx.ui;

import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.FunctionType;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lexer.HotaruTokenId;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

public class SyntaxHighlighter {

    private final CodeArea editor;
    private final ExecutorService executor;
    private Set<String> nodeFunctions;
    private Map<HotaruTokenId, String> operatorClasses;

    public SyntaxHighlighter(CodeArea editor, ExecutorService executor) {
        this.editor = editor;
        this.executor = executor;
    }

    public void init(BooleanProperty enabledProperty) {
        operatorClasses = new HashMap<>();
        operatorClasses.put(HotaruTokenId.AT, "keyframes-extractor");
        nodeFunctions = BundleLoader.functions().entrySet().stream()
                .filter(e -> e.getValue() == FunctionType.NODE)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        editor.richChanges()
                .filter(ch -> enabledProperty.get())
                .filter(ch -> !ch.getInserted().equals(ch.getRemoved()))
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(editor.richChanges())
                .filterMap(t -> Optional.ofNullable(t.isSuccess() ? t.get() : null))
                .subscribe(spans -> editor.setStyleSpans(0, spans));
    }

    public void release() {
        executor.shutdown();
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        final var text = editor.getText();
        final var task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                final var spans = new StyleSpansBuilder<Collection<String>>();
                for (final var t : new HotaruLexer(text).tokenize()) {
                    final var category = t.getType().getPrimaryCategory();
                    switch (category) {
                        case "string":
                        case "keyword":
                        case "comment":
                        case "number":
                            spans.add(Collections.singleton(category), t.getLength());
                            break;

                        case "identifier":
                            if (nodeFunctions.contains(t.getText())) {
                                spans.add(Collections.singleton("node-function"), t.getLength());
                            } else {
                                spans.add(Collections.emptyList(), t.getLength());
                            }
                            break;

                        case "operator":
                            final var className = operatorClasses.get(t.getType());
                            if (className != null) {
                                spans.add(Collections.singleton(className), t.getLength());
                            } else {
                                spans.add(Collections.emptyList(), t.getLength());
                            }
                            break;

                        default:
                            spans.add(Collections.emptyList(), t.getLength());
                            break;
                    }
                }
                return spans.create();
            }
        };
        executor.execute(task);
        return task;
    }
}
