package com.annimon.hotarufx.bundles;

import com.annimon.hotarufx.io.IOStream;
import com.annimon.hotarufx.lexer.HotaruLexer;
import com.annimon.hotarufx.lib.Context;
import com.annimon.hotarufx.lib.Value;
import com.annimon.hotarufx.parser.HotaruParser;
import com.annimon.hotarufx.parser.visitors.InterpreterVisitor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Stream;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ProgramTest {

    private static final String RES_DIR = "src/test/resources";

    static Stream<Arguments> programPathProvider() throws IOException {
        return Files.list(Paths.get(RES_DIR))
                .filter(p -> p.toString().endsWith(".hfx"))
                .map(Arguments::of);
    }

    static Value run(Path path, Context context) {
        final String input;
        try (InputStream is = Files.newInputStream(path, StandardOpenOption.READ)) {
            input = IOStream.readContent(is);
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
        return HotaruParser.parse(HotaruLexer.tokenize(input))
                .accept(new InterpreterVisitor(), context);
    }

    @DisplayName("Test programs")
    @MethodSource("programPathProvider")
    @ParameterizedTest
    void testProgram(Path path) {
        val context = new Context();
        val bundles = new ArrayList<Class<? extends Bundle>>();
        bundles.addAll(BundleLoader.runtimeBundles());
        bundles.add(AssertionsBundle.class);
        bundles.add(PrintBundle.class);
        BundleLoader.load(context, bundles);
        run(path, context);
    }
}