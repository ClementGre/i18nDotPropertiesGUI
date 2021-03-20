package fr.clementgre.i18nDotPropertiesGUI;

import fr.clementgre.i18nDotPropertiesGUI.utils.PlatformUtils;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jfxtras.styles.jmetro.JMetroStyleClass;
import org.controlsfx.control.NotificationPane;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class AutoHideNotificationPane extends NotificationPane {

    private static class Notification{
        public String text;
        public String iconName;
        public TextField input;
        public int autoHideTime;
        public Notification(String text, String iconName, int autoHideTime) {
            this.text = text;
            this.iconName = iconName;
            this.autoHideTime = autoHideTime;
        }
        public Notification(String text, String iconName, TextField input) {
            this.text = text;
            this.iconName = iconName;
            this.input = input;
            this.autoHideTime = -1;
        }
    }

    // PENDING MANAGING

    ArrayList<Notification> pendingList = new ArrayList<>();
    public AutoHideNotificationPane(){
        final EventHandler<Event> onHideEvent = e -> checkPending();
        addEventHandler(NotificationPane.ON_HIDDEN, onHideEvent);
    }

    public void addToPending(String text, String iconName, int autoHideTime){
        pendingList.add(new Notification(text, iconName, autoHideTime));
        checkPending();
    }
    public void showNow(String text, String iconName, int autoHideTime){
        pendingList.add(0, new Notification(text, iconName, autoHideTime));
        if(isShowing()) hide(); else checkPending();
    }
    public void showNow(String text, String iconName, TextField input){
        pendingList.add(0, new Notification(text, iconName, input));
        if(isShowing()) hide(); else checkPending();
    }

    private void checkPending(){
        if(pendingList.size() > 0 && !isShowing()){
            Notification notif = pendingList.get(0);
            if(notif.input == null) show(notif.text, notif.iconName, notif.autoHideTime);
            else showWithInput(notif.text, notif.input, notif.iconName, notif.autoHideTime);
            pendingList.remove(0);
        }
    }


    // SHOW AND UI DESIGN

    public void show(String text, String iconName, int autoHideTime){
        show(text, getGraphic(iconName), autoHideTime);
    }
    private HBox getGraphic(String iconName){
        Image image = new Image(getClass().getResourceAsStream("/img/" + iconName + ".png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(35);
        imageView.setPreserveRatio(true);

        HBox graphic = new HBox(imageView);
        HBox.setMargin(imageView, new Insets(4));
        return graphic;
    }
    public void showWithInput(String text, TextField input, String iconName, int autoHideTime){
        show("", getInputGraphic(iconName, text, input), autoHideTime);
    }
    private HBox getInputGraphic(String iconName, String text, TextField input){
        HBox graphic = getGraphic(iconName);
        VBox textGraphic = new VBox();
        HBox.setMargin(textGraphic, new Insets(4, 20, 4, 20));
        HBox.setHgrow(textGraphic, Priority.ALWAYS);
        input.setMaxWidth(400);

        Label textLabel = new Label(text);
        textGraphic.getChildren().addAll(textLabel, input);

        graphic.getChildren().add(textGraphic);
        return graphic;
    }



    // SHOW PROCESS

    private void show(String text, Pane graphic, int autoHideTime){
        hideAndThen(() -> {
            show(text, graphic);
            Platform.runLater(() -> {
                setupStyle();
                setupAutoHide(autoHideTime);
            });
        });
    }
    private void setupStyle(){
        Pane region = (Pane) lookup(".notification-bar > .pane");
        if(region != null){
            region.getStyleClass().add(JMetroStyleClass.BACKGROUND);
            region.setEffect(new DropShadow());
        }
        TextField input = (TextField) lookup(".text-field");
        if(input != null){
            input.requestFocus();
            PlatformUtils.runLaterOnUIThread(100, input::requestFocus);
        }
    }
    private void setupAutoHide(int autoHideTime){
        if(autoHideTime > 0){
            AtomicBoolean hidden = new AtomicBoolean(false);
            setOnHiding((e) -> hidden.set(true));

            new Thread(() -> {
                try{ Thread.sleep(autoHideTime * 1000L); }catch(InterruptedException e){ e.printStackTrace(); }
                if(!hidden.get()) hide();

            }, "notification auto hide").start();
        }
    }



    private void hideAndThen(final Runnable r) {
        if (isShowing()) {
            final EventHandler<Event> eventHandler = new EventHandler<>() {
                @Override public void handle(Event e) {
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
