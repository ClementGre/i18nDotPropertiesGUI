module i18nDotPropertiesGUI {

    // JAVA MODULES

    requires java.xml;
    requires java.base;
    requires java.logging;
    requires java.sql;
    requires java.desktop;
    requires java.management;
    requires jdk.crypto.ec;
    requires jdk.accessibility;
    requires java.prefs;

    // OTHER DEPENDENCIES

    // jfx addons
    requires org.jfxtras.styles.jmetro;
    requires org.controlsfx.controls;

    // JAVAFX
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;

    // --add-exports=javafx.graphics/com.sun.javafx.scene=org.controlsfx.controls
    // --add-exports=javafx.graphics/com.sun.javafx.scene.traversal=org.controlsfx.controls

    exports fr.clementgre.i18nDotPropertiesGUI;
    exports fr.clementgre.i18nDotPropertiesGUI.utils;
    exports fr.clementgre.i18nDotPropertiesGUI.translationsPane;
    exports fr.clementgre.i18nDotPropertiesGUI.saving;

}