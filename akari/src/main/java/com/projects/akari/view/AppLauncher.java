package com.projects.akari.view;

import com.projects.akari.SamplePuzzles;
import com.projects.akari.controller.ClassicMvcController;
import com.projects.akari.controller.ControllerImpl;
import com.projects.akari.model.Model;
import com.projects.akari.model.ModelImpl;
import com.projects.akari.model.PuzzleImpl;
import com.projects.akari.model.PuzzleLibraryImpl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class AppLauncher extends Application {
  @Override
  public void start(Stage stage) {
    PuzzleLibraryImpl puzzleLibrary = new PuzzleLibraryImpl();
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));

    Model model = new ModelImpl(puzzleLibrary);
    ClassicMvcController controller = new ControllerImpl(model);

    BorderPane root = new BorderPane();

    PuzzleView puzzleView = new PuzzleView(model, root);
    ControlView controlView = new ControlView(controller);
    MessageView messageView = new MessageView(model, root);

    // add Observers
    model.addObserver(puzzleView);
    model.addObserver(messageView);

    root.setTop(messageView.render());
    root.setCenter(puzzleView.render());
    root.setBottom(controlView.render());

    Scene scene = new Scene(root, 800, 650);
    scene.getStylesheets().add("main.css");
    stage.setTitle("Akari Light Up Game");
    stage.setScene(scene);
    stage.show();
  }
}
