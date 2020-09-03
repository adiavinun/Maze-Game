package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * this class manages the solution displayer and draws the solution
 */
public class SolutionDisplayer extends Canvas {

    private ArrayList<int[]> solutionList;
    private int mazeHeight, mazeWidth;
    private double zoomSize = 1;
    private StringProperty ImageFileNameStars = new SimpleStringProperty();

    //setters and getters
    public void setHeightWidth (int row, int col)
    {
        this.mazeHeight = row;
        this.mazeWidth = col;
    }
    public void setSolution (ArrayList<int[]> solutionList){
        this.solutionList = solutionList;
        redrawSolution();
    }
    public void setZoomSize(double size){
        zoomSize = size;
    }
    public double getZoomSize(){
        return zoomSize;
    }
    public void setImageFileNameStars(String imageFileNameStars) {
        this.ImageFileNameStars.set(imageFileNameStars);
    }
    public String getImageFileNameStars() {
        return ImageFileNameStars.get();
    }

    /**
     * draws the solution
     */
    public void redrawSolution() {
        if (solutionList != null) {
            double canvasHeight = getWidth()* zoomSize;
            double canvasWidth = getHeight()* zoomSize;
            double cellHeight = canvasHeight / mazeWidth;
            double cellWidth = canvasWidth / mazeHeight;
            try {
                Image stars = new Image(new FileInputStream(ImageFileNameStars.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Solution
               for (int i = 0; i < solutionList.size(); i++){
                   gc.drawImage(stars, solutionList.get(i)[1]*cellHeight, solutionList.get(i)[0]*cellWidth,cellHeight,cellWidth);
               }
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            }
        }
    }
    /**
     * the function clear the solution from board when called
     */
    public void clear(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());
    }
}
