package fr.clementgre.i18nDotPropertiesGUI.utils;

import javafx.application.Platform;

public class PlatformUtils {

    public static void runLaterOnUIThread(int millis, Runnable runnable){
        new Thread(() -> {
            try{ Thread.sleep(millis); }catch(InterruptedException e){ e.printStackTrace(); }
            Platform.runLater(runnable);
        }, "runLaterOnUIThread").start();
    }

}
