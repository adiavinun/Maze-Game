package Model;

import javafx.scene.input.KeyCode;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Aviadjo on 6/14/2017.
 */
public interface IModel {
    void generateMaze(int width, int height);
    void moveCharacter(KeyCode movement);
    int[][] getMaze();
    int getCharacterPositionRow();
    int getCharacterPositionColumn();
    int getGoalRow();
    int getGoalColumn();
    int getStartRow();
    int getStartColumn();
    void stopServers();
    ArrayList<int[]> getSolution();
    void solveMaze();
    void save(String name);
    void load(File file);
}
