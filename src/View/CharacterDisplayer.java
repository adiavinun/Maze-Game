package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.canvas.Canvas;

/**
 * this class manages the character displayer and draws the character
 */
public class CharacterDisplayer extends Canvas {
    private int characterPositionRow=1;
    private int characterPositionColumn=1;
    private int mazeWidth,mazeHeight;
    private double zoomSize = 1;
    //region Properties
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    //setters and getters
    public double getZoomSize(){
        return zoomSize;
    }
    public void setZoomSize(double size){
        zoomSize = size;
    }

    public void setMazeSize(int width, int height){
        this.mazeWidth = width;
        this.mazeHeight = height;
    }

    public void SetCharacterPosition(int row,int column){
        characterPositionRow = row;
        characterPositionColumn = column;
        redraw();
    }
    public String getImageFileNameCharacter() { return ImageFileNameCharacter.get(); }
    public void setImageFileNameCharacter(String imageFileNameCharacter) {
        this.ImageFileNameCharacter.set(imageFileNameCharacter);
    }
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }

    /**
     * draws the character
     */
    public void redraw() {
            double canvasHeight = getWidth()* zoomSize;
            double canvasWidth = getHeight()* zoomSize;
            double cellHeight = canvasHeight / mazeHeight;
            double cellWidth = canvasWidth / mazeWidth;

            try {
                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));

                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Character
                //gc.setFill(Color.RED);
                //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                gc.drawImage(characterImage, characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    /**
     * the function clear the characters from board when called
     */
    public void clear() {
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
