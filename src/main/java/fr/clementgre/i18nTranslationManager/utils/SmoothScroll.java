package fr.clementgre.i18nTranslationManager.utils;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;

public class SmoothScroll {

    // from https://stackoverflow.com/questions/35065310/javafx-smooth-scrolling-for-listview

    private static ScrollBar getScrollbarComponent(ListView<?> control, Orientation orientation) {
        Node n = control.lookup(".scroll-bar");
        if (n instanceof ScrollBar) {
            final ScrollBar bar = (ScrollBar) n;
            if (bar.getOrientation().equals(orientation)) {
                return bar;
            }
        }

        return null;
    }



}
