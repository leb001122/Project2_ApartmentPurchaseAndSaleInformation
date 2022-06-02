package com.creative.gui;


import javafx.scene.Scene;
import javafx.stage.Stage;

public class ViewManager {
    private static ViewManager instance;
    Stage stage;
    Scene scene;

    public ViewManager( Stage stage ) {
        if(ViewManager.instance != null) return;
        ViewManager.instance = this;

        this.stage = stage;
    }

    public static ViewManager getInstance() {
        return instance;
    }

    public Stage getStage() {
        return this.stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setTitle( String title ) {
        stage.setTitle(title);
    }
}