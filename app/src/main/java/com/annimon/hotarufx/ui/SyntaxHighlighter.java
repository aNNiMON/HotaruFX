package com.annimon.hotarufx.ui;

import com.annimon.hotarufx.bundles.BundleLoader;
import com.annimon.hotarufx.bundles.IdentifierType;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lexer.HotaruTokenId;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import static java.util.Map.entry;

public class SyntaxHighlighter {

    private final CodeArea editor;
    private final ExecutorService executor;
    private Map<String, Set<String>> identifierClasses;
    private Map<HotaruTokenId, String> operatorClasses;

    public SyntaxHighlighter(CodeArea editor, ExecutorService executor) {
        this.editor = editor;
        this.executor = executor;
    }

    public void init(BooleanProperty enabledProperty) {
        operatorClasses = new EnumMap<>(HotaruTokenId.class);
        operatorClasses.put(HotaruTokenId.AT, CssStyles.KEYFRAMES_EXTRACTOR);

        final var identifiers = BundleLoader.identifiers();
        identifierClasses = Map.ofEntries(
                entry(CssStyles.NODE_FUNCTION,
                        identifiers.getOrDefault(IdentifierType.NODE, Set.of())),
                entry(CssStyles.INTERPOLATION,
                        identifiers.getOrDefault(IdentifierType.INTERPOLATION, Set.of()))
        );

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
            protected StyleSpans<Collection<String>> call() {
                final var spans = new StyleSpansBuilder<Collection<String>>();
                for (final var t : new HotaruLexer(text).tokenize()) {
                    final var category = t.type().getPrimaryCategory();
                    final List<String> classes = switch (category) {
                        case CssStyles.STRING,
                             CssStyles.KEYWORD,
                             CssStyles.COMMENT,
                             CssStyles.NUMBER -> List.of(category);
                        case "identifier" -> {
                            for (var entry : identifierClasses.entrySet()) {
                                final String className = entry.getKey();
                                final Set<String> identifiers = entry.getValue();
                                if (identifiers.contains(t.text())) {
                                    yield List.of(className);
                                }
                            }
                            yield List.of();
                        }
                        case "operator" -> {
                            final var className = operatorClasses.get(t.type());
                            if (className != null) {
                                yield List.of(className);
                            } else {
                                yield List.of();
                            }
                        }
                        default -> List.of();
                    };
                    spans.add(classes, t.length());
                }
                return spans.create();
            }
        };
        executor.execute(task);
        return task;
    }
}
