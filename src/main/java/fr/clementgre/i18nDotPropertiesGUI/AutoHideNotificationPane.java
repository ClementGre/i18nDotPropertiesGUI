package fr.clementgre.i18nDotPropertiesGUI;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import jfxtras.styles.jmetro.JMetroStyleClass;
import org.controlsfx.control.NotificationPane;

import java.util.concurrent.atomic.AtomicBoolean;

public class AutoHideNotificationPane extends NotificationPane {



    public void show(String text, String iconName, int autoHideTime){

        Image image = new Image(getClass().getResourceAsStream("/img/" + iconName + ".png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setPreserveRatio(true);

        VBox graphic = new VBox(imageView);
        VBox.setMargin(imageView, new Insets(4));

        hideAndThen(() -> {
            AtomicBoolean alreadyHidden = new AtomicBoolean(false);
            show(text, graphic);

            Platform.runLater(() -> {
                Pane region = (Pane) lookup(".notification-bar > .pane");
                if(region != null){
                    region.getStyleClass().add(JMetroStyleClass.BACKGROUND);
                    region.setEffect(new DropShadow());
                }

                // Auto Hide
                setOnHidden((e) -> {
                    alreadyHidden.set(true);
                });

                if(autoHideTime > 0){
                    AtomicBoolean hidden = new AtomicBoolean(false);
                    setOnHiding((e) -> hidden.set(true));

                    new Thread(() -> {
                        try{ Thread.sleep(autoHideTime * 1000L); }catch(InterruptedException e){ e.printStackTrace(); }
                        if(!hidden.get()) hide();

                    }, "notification auto hide").start();
                }
            });

        });
    }


    private void hideAndThen(final Runnable r) {
        if (isShowing()) {
            final EventHandler<Event> eventHandler = new EventHandler<>() {
                @Override
                public void handle(Event e) {
                    r.run();
                    removeEventHandler(NotificationPane.ON_HIDDEN, this);
                }
            };
            addEventHandler(NotificationPane.ON_HIDDEN, eventHandler);
            hide();
        } else {
            r.run();
        }
    }


}
