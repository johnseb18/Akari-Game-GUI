package com.projects.akari.view;

import com.projects.akari.model.CellType;
import com.projects.akari.model.Model;
import com.projects.akari.model.ModelObserver;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class PuzzleView implements FXComponent, ModelObserver {
  private final Model model;
  private final BorderPane parentLayout;

  public PuzzleView(Model model, BorderPane parentLayout) {
    this.model = model;
    this.parentLayout = parentLayout;
  }

  @Override
  public Parent render() {

    GridPane gridPane = new GridPane();

    gridPane.getStyleClass().add("grid-pane");

    int height = model.getActivePuzzle().getHeight();
    int width = model.getActivePuzzle().getWidth();

    for (int r = 0; r < height; r++) {
      for (int c = 0; c < width; c++) {
        CellType cellType = model.getActivePuzzle().getCellType(r, c);

        switch (cellType) {
          case CLUE:
            int clueValue = model.getActivePuzzle().getClue(r, c);
            Button clueButton = new Button(String.valueOf(clueValue));
            clueButton.setDisable(true);

            try {
              if (model.isClueSatisfied(r, c)) {
                clueButton.getStyleClass().add("satisfied-clue-cell");
              } else {
                clueButton.getStyleClass().add("clue-cell");
              }
            } catch (Exception e) {
              clueButton.getStyleClass().add("clue-cell");
              System.err.println("Error checking clue satisfaction: " + e.getMessage());
            }

            gridPane.add(clueButton, c, r);
            break;

          case WALL:
            Button wallButton = new Button();
            wallButton.setDisable(true);
            wallButton.getStyleClass().add("wall-cell");
            gridPane.add(wallButton, c, r);
            break;

          case CORRIDOR:
            Button corridorButton = new Button();
            if (model.isLamp(r, c)) {
              if (model.isLampIllegal(r, c)) {
                corridorButton.getStyleClass().add("illegal-lamp-cell");
              } else {
                corridorButton.getStyleClass().add("lamp-cell");
              }
              Image lightBulbImage = new Image(getClass().getResourceAsStream("/light-bulb.png"));
              ImageView imageView = new ImageView(lightBulbImage);
              imageView.setFitWidth(18);
              imageView.setFitHeight(21);
              corridorButton.setGraphic(imageView);
            } else if (model.isLit(r, c)) {
              corridorButton.getStyleClass().add("lit-cell");
            } else {
              corridorButton.getStyleClass().add("unlit-cell");
            }

            int finalR = r;
            int finalC = c;
            corridorButton.setOnAction(
                event -> {
                  if (model.getActivePuzzle().getCellType(finalR, finalC) == CellType.CORRIDOR) {
                    if (model.isLamp(finalR, finalC)) {
                      model.removeLamp(finalR, finalC);
                    } else {
                      model.addLamp(finalR, finalC);
                    }
                  }
                });

            gridPane.add(corridorButton, c, r);
            break;

          default:
            throw new IllegalStateException("Unexpected cell type: " + cellType);
        }
      }
    }

    return gridPane;
  }


  @Override
  public void update(Model model) {
    parentLayout.setCenter(render());
  }
}
