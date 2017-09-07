package com.annimon.hotarufx.ui.control;

import java.util.Optional;
import javafx.application.HostServices;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;

public class ClickableHyperLink extends Hyperlink {

    private static HostServices hostServices;

    public static void setHostServices(HostServices hostServices) {
        // Please, forgive me this approach
        // it saved me a lot of time
        ClickableHyperLink.hostServices = hostServices;
    }

    public ClickableHyperLink() {
        init();
    }

    public ClickableHyperLink(String text) {
        super(text);
        init();
    }

    public ClickableHyperLink(String text, Node graphic) {
        super(text, graphic);
        init();
    }

    private void init() {
        setOnAction(e -> Optional
                .ofNullable(hostServices)
                .ifPresent(s -> s.showDocument(getText()))
        );
    }
}
