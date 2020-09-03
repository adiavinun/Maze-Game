package Model;
import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyModel extends Observable implements IModel {

    private ExecutorService threadPool;
    private Maze maze;
    private Server generate, solve;
    private Solution solution;
    private int characterPositionRow = 0;
    private int characterPositionColumn = 0;

    /**
     * constructor
     */
    public MyModel() {
        threadPool = Executors.newCachedThreadPool();
    }

    /**
     * start the two servers (generate and solve)
     */
    public void startServers() {
        generate = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        solve = new Server(5402, 1000, new ServerStrategySolveSearchProblem());
        generate.start();
        solve.start();
    }

    /**
     * stops the two servers
     */
    public void stopServers() {
        generate.stop();
        solve.stop();
        threadPool.shutdown();
        Platform.exit();
    }

    /**
     * the main method to generate the maze
     * @param row
     * @param col
     */
    @Override
    public void generateMaze(int row, int col) {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = ((byte[])fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[(row*col)+12];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        characterPositionRow = maze.getStartPosition().getRowIndex();
                        characterPositionColumn = maze.getStartPosition().getColumnIndex();
                        setChanged();
                        notifyObservers();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }
    }

    /**
     * checks if the given move is legal
     * @param row
     * @param col
     * @return
     */
    private boolean legalMove(int row, int col){
        if (row >= 0 && col >= 0 && row < maze.getRow() && col < maze.getCol()){
            return true;
        }
        return false;
    }

    /**
     * check if the given position is a wall
     * @param row
     * @param col
     * @return
     */
    private boolean checkIfWall (int row, int col){
        if (maze.getCellValue(row, col) == 1)
            return true;
        return false;
    }

    /**
     * enables the movement of the character
     * @param movement
     */
    @Override
    public void moveCharacter(KeyCode movement) {
        if (characterPositionRow == getGoalRow() && characterPositionColumn == getGoalColumn())
            return;
        switch (movement) {
            case NUMPAD8://up
            case DIGIT8://up
            case UP://up
                if (legalMove(characterPositionRow - 1,characterPositionColumn) && !checkIfWall(characterPositionRow - 1,characterPositionColumn)){
                    characterPositionRow--;
                }
                break;
            case DIGIT2://down
            case NUMPAD2://down
            case DOWN://down
                if (legalMove(characterPositionRow + 1,characterPositionColumn) && !checkIfWall(characterPositionRow + 1,characterPositionColumn)){
                    characterPositionRow++;
                }
                break;
            case DIGIT6://right
            case NUMPAD6://right
            case RIGHT://right
                if (legalMove(characterPositionRow,characterPositionColumn + 1) && !checkIfWall(characterPositionRow,characterPositionColumn + 1)){
                    characterPositionColumn++;
                }
                break;
            case DIGIT4://left
            case NUMPAD4://left
            case LEFT://left
                if (legalMove(characterPositionRow,characterPositionColumn - 1) && !checkIfWall(characterPositionRow,characterPositionColumn - 1)){
                    characterPositionColumn--;
                }
                break;
            case DIGIT9://up-right
            case NUMPAD9://up-right
                if (legalMove(characterPositionRow - 1,characterPositionColumn + 1) && !checkIfWall(characterPositionRow - 1,characterPositionColumn + 1)) {
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
            case DIGIT7://up-left
            case NUMPAD7://up-left
                if (legalMove(characterPositionRow - 1,characterPositionColumn - 1) && !checkIfWall(characterPositionRow - 1,characterPositionColumn - 1)) {
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case DIGIT3://down-right
            case NUMPAD3://down-right
                if (legalMove(characterPositionRow + 1,characterPositionColumn + 1) && !checkIfWall(characterPositionRow + 1,characterPositionColumn + 1)) {
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;
            case DIGIT1://down-left
            case NUMPAD1://down-left
                if (legalMove(characterPositionRow + 1,characterPositionColumn - 1) && !checkIfWall(characterPositionRow + 1,characterPositionColumn - 1)) {
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;
        }

        setChanged();
        notifyObservers();
    }

    /**
     * main method to solve the maze
     */
    public void solveMaze() {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5402, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        solution = (Solution)fromServer.readObject();
                        setChanged();
                        notifyObservers();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException var1) {
            var1.printStackTrace();
        }

    }

    /**
     * the main method used to save the maze
     * @param name
     */
    public void save(String name) {
        ArrayList<Object> saveList=new ArrayList<>();
        saveList.add(maze);
        saveList.add(characterPositionRow);
        saveList.add(characterPositionColumn);
        try {
            File directory = new File("./savedMazes/");
            if (!directory.exists())
                directory.mkdir();
            File[] dir_contents = directory.listFiles();
            FileOutputStream mazeFile = new FileOutputStream("./savedMazes/" +""+name);
            ObjectOutputStream out = new ObjectOutputStream(mazeFile);
            out.writeObject(saveList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * the main method to load the maze
     * @param file
     */
    @Override
    public void load(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            ArrayList<Object> loadList = (ArrayList<Object>) objectInputStream.readObject();
            if (loadList != null) {
                maze = (Maze) loadList.get(0);
                characterPositionRow = (int) loadList.get(1);
                characterPositionColumn =  (int) loadList.get (2);
                setChanged();
                notifyObservers();
            }
            fileInputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    //getters
    @Override
    public int[][] getMaze() {
        if (maze == null){
            return null;
        }
        return maze.getMaze();
    }
    public int getStartRow(){
        return maze.getStartPosition().getRowIndex();
    }

    public int getStartColumn(){
        return maze.getStartPosition().getColumnIndex();
    }
    public int getGoalRow(){
        return maze.getGoalPosition().getRowIndex();
    }

    public int getGoalColumn(){
        return maze.getGoalPosition().getColumnIndex();
    }

    @Override
    public int getCharacterPositionRow() {
        return characterPositionRow;
    }

    @Override
    public int getCharacterPositionColumn() {
        return characterPositionColumn;
    }
    public ArrayList<int[]> getSolution (){
        if (solution == null){
            return null;
        }
        ArrayList<AState> solutionList = solution.getSolutionPath();
        ArrayList<int[]> solutionIntList = new ArrayList<>();
        for (int i = 0; i < solutionList.size(); i++){
            MazeState mazeState = (MazeState)solutionList.get(i);
            Position position = mazeState.getPosition();
            int[] rowCol = new int[2];
            rowCol[0] = position.getRowIndex();
            rowCol[1] = position.getColumnIndex();
            solutionIntList.add(i, rowCol);

        }
        return solutionIntList;

    }

}
