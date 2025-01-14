package com.projects.akari.controller;

import com.projects.akari.model.Model;

import java.util.Random;

public class ControllerImpl implements ClassicMvcController {

  private final Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  @Override
  public void clickNextPuzzle() {
    if (model.getPuzzleLibrarySize() == 0) {
      throw new IllegalStateException("No puzzles in the library.");
    }

    if (model.getActivePuzzleIndex() + 1 >= model.getPuzzleLibrarySize()) {
      model.setActivePuzzleIndex(0);
    } else {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() + 1);
    }
  }

  @Override
  public void clickPrevPuzzle() {
    if (model.getPuzzleLibrarySize() == 0) {
      throw new IllegalStateException("No puzzles in the library.");
    }

    if (model.getActivePuzzleIndex() - 1 < 0) {
      model.setActivePuzzleIndex(model.getPuzzleLibrarySize() - 1);
    } else {
      model.setActivePuzzleIndex(model.getActivePuzzleIndex() - 1);
    }
  }

  @Override
  public void clickRandPuzzle() {
    Random random = new Random();
    int randomNum =
        random.nextInt(
            model.getPuzzleLibrarySize()); // calculates number ranging from 0 to librarySize-1

    model.setActivePuzzleIndex(randomNum);
  }

  @Override
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  @Override
  public void clickCell(int r, int c) {
    if (model.isLamp(r, c)) {
      model.removeLamp(r, c);
    } else {
      model.addLamp(r, c);
    }
  }
}
