package fr.clementgre.i18nTranslationManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class Main extends Application {


    public static void main(String[] args){
        System.out.println("Starting i18nTranslationManager...");

        ///// START APP /////
        launch(args);
    }
    @Override
    public void start(Stage window) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/mainWindow.fxml"));
        new JMetro(root, Style.LIGHT);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        Scene scene = new Scene(root, 1000, 600);

        window.setMinHeight(400);
        window.setMinWidth(700);

        window.setTitle("i18nTranslationManager");
        window.setScene(scene);
        window.show();

    }



    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    public static boolean isOSX(){
        return System.getProperty("os.name").toLowerCase().contains("mac os x");
    }
    public static boolean isLinux(){
        return !isWindows() && !isOSX();
    }

}
