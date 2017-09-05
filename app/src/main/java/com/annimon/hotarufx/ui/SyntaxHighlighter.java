package com.annimon.hotarufx.ui;

import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.FunctionType;
import com.annimon.hotarufx.lexer.HotaruLexer;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

@RequiredArgsConstructor
public class SyntaxHighlighter {

    private final CodeArea editor;
    private final ExecutorService executor;
    private Set<String> nodeFunctions;

    public void init() {
        nodeFunctions = BundleLoader.functions().entrySet().stream()
                .filter(e -> e.getValue() == FunctionType.NODE)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        editor.richChanges()
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
        val text = editor.getText();
        val task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                val spans = new StyleSpansBuilder<Collection<String>>();
                for (val t : new HotaruLexer(text).tokenize()) {
                    val category = t.getType().getPrimaryCategory();
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
