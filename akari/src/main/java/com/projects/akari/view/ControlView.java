package com.projects.akari.view;

import com.projects.akari.controller.ClassicMvcController;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ControlView implements FXComponent {

    private final ClassicMvcController controller;

    public ControlView(ClassicMvcController controller) {
        this.controller = controller;
    }

    @Override
    public Parent render() {

        HBox navigationBox = new HBox();
        navigationBox.getStyleClass().add("navigation-box");

        Button prevButton = new Button("Previous");
        prevButton.getStyleClass().add("control-button");
        prevButton.setOnAction(event -> controller.clickPrevPuzzle());

        Button resetButton = new Button("Reset");
        resetButton.getStyleClass().add("reset-button");
        resetButton.setOnAction(event -> controller.clickResetPuzzle());

        Button randomButton = new Button("Random");
        randomButton.getStyleClass().add("random-button");
        randomButton.setOnAction(event -> controller.clickRandPuzzle());

        Button nextButton = new Button("Next");
        nextButton.getStyleClass().add("control-button");
        nextButton.setOnAction(event -> controller.clickNextPuzzle());

        navigationBox.getChildren().addAll(prevButton, resetButton, randomButton, nextButton);

        return navigationBox;
    }
}