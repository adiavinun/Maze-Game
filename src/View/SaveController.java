package View;

import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.scene.control.Alert;

import java.io.File;

public class SaveController {

    public static String myName;
    public javafx.scene.control.TextField MySaves;


    public static String getName() {
        return myName;
    }

    /**
     * save the current maze object to savedMazes directory
     * @throws InterruptedException
     */
    public void save() throws InterruptedException {
        myName = MySaves.getText();
        String nameInFill = null;
        File directory = new File("./savedMazes/");
        if (directory != null){
            File[] listFiles = directory.listFiles();
            if (listFiles != null){
                for (int i = 0; i < listFiles.length; i++) {
                    if (listFiles[i].getName().equals(myName)) {
                        nameInFill = "This name is already saved in the system, and will be replaced";
                    }
                }
            }
            showAlert("Your maze is saved! See you next time...", nameInFill);
        }
    }

    /**
     * the method shows and alert when the saved option is used
     * @param alertMessage
     * @param smallAlert
     * @throws InterruptedException
     */
    private void showAlert(String alertMessage, String smallAlert) throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Save confirmation");
        alert.setHeaderText(smallAlert);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
}
