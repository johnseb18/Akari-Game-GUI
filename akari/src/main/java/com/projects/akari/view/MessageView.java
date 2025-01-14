package com.projects.akari.view;

import com.projects.akari.model.Model;
import com.projects.akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MessageView implements FXComponent, ModelObserver {
  private final Model model;
  private final BorderPane parentLayout;

  public MessageView(Model model, BorderPane parentLayout) {
    this.model = model;
    this.parentLayout = parentLayout;
  }

  @Override
  public Parent render() {
    VBox messagePane = new VBox();
    messagePane.getStyleClass().add("message-pane");
    messagePane.setSpacing(5);

    Label messageLabel = new Label();
    messageLabel.getStyleClass().add("message-label");

    if (model.isSolved()) {
      messageLabel.setText("Puzzle Solved! Congratulations!");
      messageLabel.getStyleClass().add("success-message");
    } else {
      messageLabel.setText("Keep going! Solve the puzzle.");
      messageLabel.getStyleClass().add("progress-message");
    }

    int currentIndex = model.getActivePuzzleIndex() + 1;
    int totalPuzzles = model.getPuzzleLibrarySize();
    Label puzzleInfo = new Label("Puzzle " + currentIndex + " of " + totalPuzzles);
    puzzleInfo.getStyleClass().add("puzzle-info");

    messagePane.getChildren().addAll(messageLabel, puzzleInfo);

    return messagePane;
  }


  @Override
  public void update(Model model) {
    parentLayout.setTop(render());
  }
}