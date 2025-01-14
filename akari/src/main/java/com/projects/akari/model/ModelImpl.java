package com.projects.akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary library;
  private int index;
  private final List<ModelObserver> observers = new ArrayList<>();
  private int[][] lamps;

  public ModelImpl(PuzzleLibrary library) {
    this.library = library;
    index = 0;
    lamps =
        new int[getActivePuzzle().getHeight()]
            [getActivePuzzle().getWidth()]; // everything is set to default (0)
  }

  @Override
  public void addLamp(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
    }

    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Can only put a lamp on a corridor cell.");
    }
    if (lamps[r][c] == 1) {
      throw new IllegalArgumentException("There's already a lamp there.");
    }

    lamps[r][c] = 1;
    // update()
    notifyObservers();
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("A lamp can only be on a corridor cell.");
    }
    if (lamps[r][c] == 0) {
      throw new IllegalStateException("There's no lamp there.");
    }

    lamps[r][c] = 0;
    // update()
    notifyObservers();
  }

  @Override
  public boolean isLit(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
    }

    if (isLamp(r, c)) {
      return true;
    }

    // checks down
    for (int i = r + 1; i < getActivePuzzle().getHeight(); i++) {
        CellType cellType = getActivePuzzle().getCellType(i, c);
        if (cellType == CellType.WALL || cellType == CellType.CLUE) {
            break;
        }
        if (isLamp(i, c)) {
            return true;
        }
    }

    // checks up
    for (int i = r - 1; i >= 0; i--) {
        CellType cellType = getActivePuzzle().getCellType(i, c);
        if (cellType == CellType.WALL || cellType == CellType.CLUE) {
            break;
        }
        if (isLamp(i, c)) {
            return true;
        }
    }

    // checks right
    for (int j = c + 1; j < getActivePuzzle().getWidth(); j++) {
        CellType cellType = getActivePuzzle().getCellType(r, j);
        if (cellType == CellType.WALL || cellType == CellType.CLUE) {
            break;
        }
        if (isLamp(r, j)) {
            return true;
        }
    }

    // check left
    for (int j = c - 1; j >= 0; j--) {
        CellType cellType = getActivePuzzle().getCellType(r, j);
        if (cellType == CellType.WALL || cellType == CellType.CLUE) {
            break;
        }
        if (isLamp(r, j)) {
            return true;
        }
    }

    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Coordinates are out of bounds.4");
    }

    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Not a corridor cell.");
    }
    return (lamps[r][c] == 1);
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (!isLamp(r, c)) {
      return false; // If there isnt a lamp here, it cant be illegal
      // isLamp() does all the bounds/other checks that are necessary
    }

    int[] row = {1, -1, 0, 0}; // down, up
    int[] col = {0, 0, 1, -1}; // right, left

    for (int i = 0; i < 4; i++) {
      int nr = r + row[i];
      int nc = c + col[i];

      if (nr >= 0
          && nr < getActivePuzzle().getHeight()
          && nc >= 0
          && nc < getActivePuzzle().getWidth()) {
        if (getActivePuzzle().getCellType(nr, nc) == CellType.CLUE
            && getActivePuzzle().getClue(nr, nc) == 0) {
          return true;
        }
      }
    }

    for (int i = r + 1; i < getActivePuzzle().getHeight(); i++) { // down
      if (getActivePuzzle().getCellType(i, c) == CellType.WALL
          || getActivePuzzle().getCellType(i, c) == CellType.CLUE) {
        break;
      }
      if (isLamp(i, c)) {
        return true;
      }
    }
    for (int i = r - 1; i >= 0; i--) { // up
      if (getActivePuzzle().getCellType(i, c) == CellType.WALL
          || getActivePuzzle().getCellType(i, c) == CellType.CLUE) {
        break;
      }
      if (isLamp(i, c)) {
        return true;
      }
    }
    for (int j = c + 1; j < getActivePuzzle().getWidth(); j++) { // right
      if (getActivePuzzle().getCellType(r, j) == CellType.WALL
          || getActivePuzzle().getCellType(r, j) == CellType.CLUE) {
        break;
      }
      if (isLamp(r, j)) {
        return true;
      }
    }
    for (int j = c - 1; j >= 0; j--) { // left
      if (getActivePuzzle().getCellType(r, j) == CellType.WALL
          || getActivePuzzle().getCellType(r, j) == CellType.CLUE) {
        break;
      }
      if (isLamp(r, j)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return library.getPuzzle(index);
  }

  @Override
  public int getActivePuzzleIndex() {
    return index;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= library.size()) {
      throw new IndexOutOfBoundsException("Puzzle index out of bounds.");
    }
    this.index = index;

    lamps = new int[getActivePuzzle().getHeight()][getActivePuzzle().getWidth()];

    // update()
    notifyObservers();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    lamps =
        new int[getActivePuzzle().getHeight()]
            [getActivePuzzle().getWidth()]; // resets all the lamps
    // update()
    notifyObservers();
  }

  @Override
  public boolean isSolved() {
    Puzzle puzzle = getActivePuzzle();

    for (int r = 0; r < puzzle.getHeight(); r++) {
      for (int c = 0; c < puzzle.getWidth(); c++) {

        if (puzzle.getCellType(r, c) == CellType.CORRIDOR) {
          if (!(isLit(r, c))) {
            return false;
          }
          if (isLampIllegal(r, c)) {
            return false;
          }
        }

        if (puzzle.getCellType(r, c) == CellType.CLUE) {
          if (!(isClueSatisfied(r, c))) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r < 0 || r >= getActivePuzzle().getHeight() || c < 0 || c >= getActivePuzzle().getWidth()) {
      throw new IndexOutOfBoundsException("Coordinates are out of bounds.");
    }

    if (getActivePuzzle().getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("Cell is not a clue.");
    }

    int clueAmount = getActivePuzzle().getClue(r, c);
    int actualAmount = 0;

    if (r + 1 < getActivePuzzle().getHeight()
        && getActivePuzzle().getCellType(r + 1, c) == CellType.CORRIDOR
        && isLamp(r + 1, c)) {
      actualAmount++;
    }
    if (r - 1 >= 0
        && getActivePuzzle().getCellType(r - 1, c) == CellType.CORRIDOR
        && isLamp(r - 1, c)) {
      actualAmount++;
    }
    if (c + 1 < getActivePuzzle().getWidth()
        && getActivePuzzle().getCellType(r, c + 1) == CellType.CORRIDOR
        && isLamp(r, c + 1)) {
      actualAmount++;
    }
    if (c - 1 >= 0
        && getActivePuzzle().getCellType(r, c - 1) == CellType.CORRIDOR
        && isLamp(r, c - 1)) {
      actualAmount++;
    }

    return clueAmount == actualAmount;
  }

  @Override
  public void addObserver(ModelObserver observer) {
    observers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }
}
