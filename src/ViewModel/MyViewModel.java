package ViewModel;

import Model.IModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * class that allows interaction between the View Package and the Model Package
 */
public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int characterPositionRowIndex;
    private int characterPositionColumnIndex;
    public StringProperty characterPositionRow = new SimpleStringProperty("1"); //For Binding
    public StringProperty characterPositionColumn = new SimpleStringProperty("1"); //For Binding

    /**
     * constructor
     * @param model
     */
    public MyViewModel(IModel model){
        this.model = model;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o==model){
            characterPositionRowIndex = model.getCharacterPositionRow();
            characterPositionRow.set(characterPositionRowIndex + "");
            characterPositionColumnIndex = model.getCharacterPositionColumn();
            characterPositionColumn.set(characterPositionColumnIndex + "");
            setChanged();
            notifyObservers();
        }
    }

    public void generateMaze(int width, int height){
        model.generateMaze(width, height);
    }

    public void moveCharacter(KeyCode movement){
        model.moveCharacter(movement);
    }
    public void solveMaze(){
        model.solveMaze();
    }
    public void save(String name){ model.save(name); }
    public void load(File loadedFile) { model.load(loadedFile); }
    public void stopServers() {
        model.stopServers();
    }
    //getters
    public int[][] getMaze() {
        return model.getMaze();
    }
    public int getGoalRow() {
        return model.getGoalRow();
    }
    public int getStartRow() {
        return model.getStartRow(); }

    public int getStartColumn() {
        return model.getStartColumn();
    }

    public int getGoalColumn() {
        return model.getGoalColumn();
    }

    public int getCharacterPositionRow() {
        return model.getCharacterPositionRow();
    }

    public int getCharacterPositionColumn() {
        return model.getCharacterPositionColumn();
    }

    public ArrayList<int[]> getSolution(){
        return model.getSolution();
    }

}
