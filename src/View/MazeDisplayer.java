package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;


public class MazeDisplayer extends Canvas{

    private int[][] maze;
    private int characterPositionRow = 1;
    private int characterPositionColumn = 1;
    private int goalRow,goalCol, startRow, startCol;
    private double zoomSize = 1;
    private ArrayList<int[]> solutionList;

    public void setMaze(int[][] maze, int goalRow, int goalColumn,int startRow, int startColumn) {
        this.goalRow = goalRow;
        this.goalCol = goalColumn;
        this.startRow = startRow;
        this.startCol = startColumn;
        this.maze = maze;
        redraw();
    }

    public void setCharacterPosition(int row, int column) {
        characterPositionRow = row;
        characterPositionColumn = column;
    }

    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
    public double getZoomSize(){
        return zoomSize;
    }
    public void setZoomSize(double size){
        zoomSize = size;
    }

    public void redraw() {
        if (maze != null) {
            double canvasHeight = getWidth() * zoomSize;
            double canvasWidth = getHeight() * zoomSize;
            double cellHeight = canvasHeight / maze[0].length;
            double cellWidth = canvasWidth / maze.length;

            try {
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image goalImage = new Image (new FileInputStream(ImageFileNameGoal.get()));
                Image iceImage = new Image (new FileInputStream(ImageFileNameIce.get()));


                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < maze.length; i++) {
                    for (int j = 0; j < maze[i].length; j++) {
                        if (maze[i][j] == 1) {
                            //gc.fillRect(i * cellHeight, j * cellWidth, cellHeight, cellWidth);
                            gc.drawImage(wallImage, j * cellHeight, i * cellWidth,  cellHeight,cellWidth);
                        }
                        else{
                            gc.drawImage(iceImage, j * cellHeight, i * cellWidth,  cellHeight,cellWidth);

                        }
                    }
                }

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                //gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(goalImage, goalCol*cellHeight,goalRow*cellWidth, cellHeight, cellWidth);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }

    //region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameGoal = new SimpleStringProperty();
    private StringProperty ImageFileNameIce = new SimpleStringProperty();

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }
    public String getImageFileNameGoal() {
        return ImageFileNameGoal.get();
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }
    public String getImageFileNameIce() {
        return ImageFileNameIce.get();
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
    public void setImageFileNameGoal(String imageFileNameGoal) {
        this.ImageFileNameGoal.set(imageFileNameGoal);
    }
    public void setImageFileNameIce(String imageFileNameIce) {
        this.ImageFileNameIce.set(imageFileNameIce);
    }

    //endregion

}