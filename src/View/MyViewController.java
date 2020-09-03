package View;
import ViewModel.MyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MyViewController implements IView, Observer, Initializable{
    @FXML
    private MyViewModel myViewModel;
    public MazeDisplayer mazeDisplayer;
    public SolutionDisplayer solutionDisplayer;
    public CharacterDisplayer characterDisplayer;
    public javafx.scene.control.MenuItem Exit;
    public javafx.scene.control.Button start;
    public javafx.scene.control.TextField txtfield_numOfRows;
    public javafx.scene.control.TextField txtfield_numOfCols;
    public javafx.scene.control.Label lbl_numOfRows;
    public javafx.scene.control.Label lbl_numOfCols;
    public javafx.scene.control.Label welcome;
    public javafx.scene.control.Button newGame;
    public javafx.scene.control.Button btn_hint;
    public javafx.scene.control.Button solve;
    public javafx.scene.control.Label lbl_question;
    public javafx.scene.control.Label lbl_row;
    public javafx.scene.control.Label lbl_col;
    public javafx.scene.control.Label lbl_currcol;
    public javafx.scene.control.Label lbl_currrow;
    public javafx.scene.control.Label lbl_name;
    public javafx.scene.control.TextField txtfield_name;
    public javafx.scene.control.Label lbl_letsplay;
    public javafx.scene.layout.BorderPane borderPane;
    public ImageView lowerLeftImage;
    public ImageView mainMenuPic;
    public ImageView clockPic;
    public ArrayList<int[]> solutionList;
    public boolean toSolve=false;
    public boolean toGenerate=false;
    public boolean hint=false;
    public boolean hideHint=false;
    public boolean solving=false;
    public boolean sound=false;
    public MediaPlayer menuMusic = new MediaPlayer(new Media(new File("Resources/Music/menuMusic.mp3").toURI().toString()));
    public MediaPlayer  backRoundMusic = new MediaPlayer((new Media(new File("Resources/Music/music.m4a").toURI().toString())));
    public MediaPlayer win = new MediaPlayer((new Media(new File("Resources/Music/VictorySound.m4a").toURI().toString())));
    public Stage myStage;
    public Pane pane;
    //region String Property for Binding
    public StringProperty characterPositionRow = new SimpleStringProperty();
    public StringProperty characterPositionColumn = new SimpleStringProperty();

    /**
     * function that enables the screen to resize
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources){
        pane.prefHeightProperty().bind(borderPane.heightProperty());
        pane.prefWidthProperty().bind(borderPane.widthProperty());
        mazeDisplayer.heightProperty().bind(pane.heightProperty());
        mazeDisplayer.widthProperty().bind(pane.widthProperty());
        mazeDisplayer.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(myViewModel.getMaze() != null)
                displayMaze(myViewModel.getMaze(),myViewModel.getGoalRow(), myViewModel.getGoalColumn());
        });
        mazeDisplayer.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(myViewModel.getMaze() != null)
                displayMaze(myViewModel.getMaze(),myViewModel.getGoalRow(), myViewModel.getGoalColumn());
        });

        solutionDisplayer.heightProperty().bind(pane.heightProperty());
        solutionDisplayer.widthProperty().bind(pane.widthProperty());
        solutionDisplayer.heightProperty().addListener((observable, oldValue, newValue) -> {
            if(myViewModel.getSolution() != null && solving)
                displaySolution(myViewModel.getSolution());
        });
        mazeDisplayer.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(myViewModel.getSolution() != null && solving)
                displaySolution(myViewModel.getSolution());
        });
        characterDisplayer.heightProperty().bind(pane.heightProperty());
        characterDisplayer.widthProperty().bind(pane.widthProperty());
        characterDisplayer.heightProperty().addListener((observable, oldValue, newValue) -> { if(myViewModel.getMaze() != null)
            if(myViewModel.getMaze() != null)
                displayCharacter(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionColumn());
        });
        characterDisplayer.widthProperty().addListener((observable, oldValue, newValue) -> {
            if(myViewModel.getMaze() != null)
                displayCharacter(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionColumn());
        });


    }

    /**
     * setter for the viewModel class
     * @param viewModel
     */
    public void setViewModel(MyViewModel viewModel) {
        this.myViewModel = viewModel;
        newGame.setVisible(false);
        btn_hint.setVisible(false);
        solve.setVisible(false);
        lbl_question.setVisible(false);
        lbl_row.setVisible(false);
        lbl_col.setVisible(false);
        txtfield_numOfRows.setVisible(false);
        txtfield_numOfCols.setVisible(false);
        lbl_numOfRows.setVisible(false);
        lbl_numOfCols.setVisible(false);
        lbl_currcol.setVisible(false);
        lbl_currrow.setVisible(false);
        lbl_letsplay.setVisible(false);
        clockPic.setVisible(false);
        bindProperties(this.myViewModel);
        menuMusic.play();
        menuMusic.setCycleCount(menuMusic.INDEFINITE);
        try {
            lowerLeftImage.setImage(new Image(new FileInputStream("resources/Images/icytower.png")));
            mainMenuPic.setImage(new Image(new FileInputStream("resources/Images/mainMenuPic.png")));
            clockPic.setImage(new Image(new FileInputStream("resources/Images/clock.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * bind the row and column to the text block
     * @param viewModel
     */
    private void bindProperties(MyViewModel viewModel) {
        lbl_numOfRows.textProperty().bind(viewModel.characterPositionRow);
        lbl_numOfCols.textProperty().bind(viewModel.characterPositionColumn);
    }

    /**
     * function when start button is clicked
     */
    public void start() {
        welcome.setVisible(false);
        lbl_name.setVisible(false);
        txtfield_name.setVisible(false);
        start.setVisible(false);
        newGame.setVisible(true);
        btn_hint.setVisible(false);
        solve.setVisible(false);
        lbl_question.setVisible(true);
        lbl_row.setVisible(true);
        lbl_col.setVisible(true);
        txtfield_numOfRows.setVisible(true);
        txtfield_numOfCols.setVisible(true);
        lbl_numOfRows.setVisible(false);
        lbl_numOfCols.setVisible(false);
        lbl_currcol.setVisible(false);
        lbl_currrow.setVisible(false);
        lbl_letsplay.setVisible(true);
        clockPic.setVisible(true);
    }

    /**
     * load about fxml
     */
    @FXML
    public void about() {
        loadFXML("About.fxml", "About");

    }

    /**
     * load help fxml
     */
    @FXML
    public void help() {
        loadFXML("Help.fxml", "Help", "#helpText", "Everyone knows what the goal of the maze is - to reach the exit.\n" +
                "And in this game you will do this in a light and enjoyable way helping\n" +
                "Harold The Homeboy find his way to the top of the tower.\n" +
                "During the game, if you are stuck you can use the hint button and see\n" +
                "the solution for a limited time. If you can no longer continue you may\n" +
                "see the solution and find your way out of the tower. You can always start\n" +
                "a new game with the New Game button.\n" +
                "\n" +
                "At any stage during the game you can click on the menu icon on the upper\n" +
                "left side of the screen to open the options menu. There ,for example, you can\n" +
                "save the maze you are solving and try again at a later time.\n");
    }

    /**
     * function that loads an fxml file
     * @param fxml
     * @param title
     */
    private void loadFXML(String fxml, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("resources/View.css").toExternalForm());
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function that loads an fxml file
     * @param fxml
     * @param title
     * @param label
     * @param text
     */
    private void loadFXML(String fxml, String title, String label, String text) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxml));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            Scene scene = new Scene(root);
            //scene.getStylesheets().add(getClass().getResource("View.css").toExternalForm());
            stage.setScene(scene);
            Label l = (Label) root.lookup(label);
            l.setText(text);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function when exit button is clicked
     */
    @FXML
    public void exitButton() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            // ... user chose OK
            // Close program
            myViewModel.stopServers();
        }
    }

    /**
     * load exit fxml
     */
    @FXML
    public void exit() {
        myViewModel.stopServers();
    }

    /**
     * function when new maze button is clicked, a new maze is generated
     */
    @FXML
    public void generateMaze() {
        menuMusic.stop();
        win.stop();
        backRoundMusic.stop();
        if (!sound){
            backRoundMusic.play();
            backRoundMusic.setCycleCount(backRoundMusic.INDEFINITE);
        }
        if (!checkIfInt(txtfield_numOfRows.getText()) || !checkIfInt(txtfield_numOfCols.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Incorrect input");
            alert.setHeaderText("Error! The input is not valid");
            alert.setContentText("Please reenter a number");
            alert.showAndWait();
        } else {
            start();
            toGenerate=true;
            solving=false;
            welcome.setVisible(false);
            newGame.setDisable(true);
            start.setDisable(true);
            btn_hint.setVisible(true);
            solve.setVisible(true);
            lbl_numOfRows.setVisible(true);
            lbl_numOfCols.setVisible(true);
            lbl_currcol.setVisible(true);
            lbl_currrow.setVisible(true);
            btn_hint.setDisable(true);
            solve.setDisable(true);
            int row = Integer.valueOf(txtfield_numOfRows.getText());
            int col = Integer.valueOf(txtfield_numOfCols.getText());
            if (row < 5)
                row = 5;
            if (col < 5)
                col = 5;
            mainMenuPic.setVisible(false);
            solutionDisplayer.clear();
            characterDisplayer.setMazeSize(row,col);
            solutionDisplayer.setHeightWidth(row, col);
            myViewModel.generateMaze(row, col);
        }
    }

    /**
     * function that checks if the input is an integer
     * @param text
     * @return boolean
     */
    private boolean checkIfInt(String text) {
        return text.matches("[0-9]*");
    }

    /**
     * override method observer when an event has occurred
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o == myViewModel) {
            if(hideHint){
                hideHint=false;
                solutionDisplayer.clear();
            }
            if(toSolve){
                toSolve=false;
                solving=true;
                solutionList = myViewModel.getSolution();
                displaySolution(solutionList);
                solve.setDisable(true);
                btn_hint.setDisable(true);
            }
            else if (hint){
                hint=false;
                hideHint=true;
                solutionList = myViewModel.getSolution();
                displaySolution(solutionList);
                solve.setDisable(true);

            }
            else{
                if(checkIfWin() == true) {
                    ActionEvent act = new ActionEvent();
                    characterWin(act);
                    btn_hint.setVisible(false);
                    solve.setVisible(false);
                    solutionDisplayer.clear();
                }
                displayMaze(myViewModel.getMaze(), myViewModel.getGoalRow(), myViewModel.getGoalColumn());
                displayCharacter(myViewModel.getCharacterPositionRow(), myViewModel.getCharacterPositionColumn());
                if (!solving){
                    newGame.setDisable(false);
                    btn_hint.setDisable(false);
                    solve.setDisable(false);
                    start.setDisable(false);
                }
            }
        }
    }

    /**
     * function that checks if the character has reached the goal
     * @return boolean
     */
    private boolean checkIfWin() {
        if (myViewModel.getCharacterPositionColumn() == myViewModel.getGoalColumn() && myViewModel.getCharacterPositionRow() == myViewModel.getGoalRow()) {
            return true;
        }
        return false;
    }

    /**
     * function that displays the maze on the screen
     * @param maze
     * @param goalRow
     * @param goalColumn
     */
    @Override
    public void displayMaze(int[][] maze, int goalRow, int goalColumn) {
        mazeDisplayer.setMaze(maze, goalRow, goalColumn, myViewModel.getStartRow(), myViewModel.getStartColumn());
    }

    /**
     * when solve button is clicked
     */
    public void solveMaze(){
        if(toGenerate){
            toSolve=true;
            newGame.setDisable(false);
            btn_hint.setDisable(false);
            solve.setDisable(false);
            myViewModel.solveMaze();
        }

    }

    /**
     * when hint button is clicked
     */
    public void Hint(){
        if(toGenerate){
            hint=true;
            newGame.setDisable(false);
            btn_hint.setDisable(true);
            solve.setDisable(true);
            myViewModel.solveMaze();
            MediaPlayer hint = new MediaPlayer((new Media(new File("Resources/Music/hintEffects.mp3").toURI().toString())));
            hint.setRate(1);
            hint.setCycleCount(1);
            hint.play();
        }
    }

    /**
     * function that displays the solution on the screen
     * @param solutionList
     */
    @Override
    public void displaySolution(ArrayList<int[]> solutionList){
        solutionDisplayer.setSolution(solutionList);
    }

    /**
     * function that displays the character on the screen
     * @param row
     * @param column
     */
    @Override
    public void displayCharacter(int row, int column) {
        characterDisplayer.SetCharacterPosition(row,column);
    }

    /**
     * function that requests focus
     * @param mouseEvent
     */
    public void mouseClicked(MouseEvent mouseEvent) {
        this.mazeDisplayer.requestFocus();
    }

    public void setResizeEvent(Scene scene) {
        myStage.setScene(scene);
        myStage.setResizable(true);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
            }
        });
    }

    /**
     * setter for the stage
     * @param stage
     */
    public void setStage(Stage stage){
        myStage=stage;
    }

    /**
     * handle key events
     * @param keyEvent
     */
    public void KeyPressed(KeyEvent keyEvent) {
        if (myViewModel.getMaze() != null) {
            myViewModel.moveCharacter(keyEvent.getCode());
            keyEvent.consume();
        }
    }

    /**
     *  getter for character row
     * @return
     */
    public String getCharacterPositionRow() {
        return characterPositionRow.get();
    }

    /**
     * function when character wins the game
     * @param actionEvent
     */
    public void characterWin(ActionEvent actionEvent) {
        try {
            win.play();
            Stage stage = new Stage();
            stage.setTitle("Congratulations! You won the game!");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("won.fxml").openStream());
            Scene scene = new Scene(root, 600, 330);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            win.play();
            backRoundMusic.stop();
            stage.show();
        } catch (Exception e) { }
    }

    /**
     * function when save button is clicked
     * @param actionEvent
     */
    public void Save(ActionEvent actionEvent) {
        try {
            Stage st = new Stage();
            st.setTitle("Saved Mazes");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Save.fxml").openStream());
            Scene scene = new Scene(root, 500, 400);
            st.setScene(scene);
            st.initModality(Modality.APPLICATION_MODAL);
            st.showAndWait();
            String saveAsName=SaveController.getName();
            myViewModel.save(saveAsName);
        } catch (Exception exp) {}
    }

    /**
     * function when load button is clicked
     * @param actionEvent
     */
    public void Load(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose your saved maze");
        File pathToLoadDir = new File("./savedMazes/");
        if(!pathToLoadDir.exists())
            return;
        fileChooser.setInitialDirectory((pathToLoadDir));
        File file = fileChooser.showOpenDialog(new PopupWindow(){});
        if (file != null) {
            myViewModel.load(file);
            start();
            lbl_numOfRows.setVisible(true);
            lbl_numOfCols.setVisible(true);
            lbl_currcol.setVisible(true);
            lbl_currrow.setVisible(true);
            btn_hint.setVisible(true);
            solve.setVisible(true);
        }
    }

    /**
     * load properties fxml
     * @param actionEvent
     */
    public void properties(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();
            stage.setTitle("Properties");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Prop.fxml").openStream());
            Scene scene = new Scene(root, 500, 350);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * enable to zoom in and out of game
     * @param s
     */
    public void Scroll(ScrollEvent s){
        if(myViewModel.getMaze() != null && s.isControlDown()){
            double factor = 1.1D;
            double delta = s.getDeltaY();
            if (delta > 0.0D) {
                mazeDisplayer.setZoomSize(mazeDisplayer.getZoomSize() * factor);
                solutionDisplayer.setZoomSize(solutionDisplayer.getZoomSize() * factor);
                characterDisplayer.setZoomSize(characterDisplayer.getZoomSize() * factor);

            } else{
                mazeDisplayer.setZoomSize(mazeDisplayer.getZoomSize() / factor);
                solutionDisplayer.setZoomSize(solutionDisplayer.getZoomSize() / factor);
                characterDisplayer.setZoomSize(characterDisplayer.getZoomSize() / factor);
            }
            if(myViewModel.getMaze() != null){
                mazeDisplayer.redraw();
                characterDisplayer.redraw();
            }
            if(solving){
                solutionDisplayer.redrawSolution();
            }
            s.consume();
        }
    }

    /**
     * when sound on/off is clicked
     */
    public void Sound() {
        if(sound){
            backRoundMusic.play();
            backRoundMusic.setCycleCount(backRoundMusic.INDEFINITE);
            sound = false;
        }
        else{
            backRoundMusic.stop();
            menuMusic.stop();
            sound = true;
        }
    }
}
