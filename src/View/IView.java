package View;

import java.util.ArrayList;

public interface IView {
    void displayMaze(int[][] maze, int goalRow, int goalColumn);
    void displaySolution(ArrayList<int[]> solutionList);
    void displayCharacter(int row, int column);
}
