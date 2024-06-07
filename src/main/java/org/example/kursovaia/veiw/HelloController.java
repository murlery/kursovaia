package org.example.kursovaia.veiw;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.kursovaia.data.DbWorker;
import org.example.kursovaia.domain.User;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button authSignInBtn;

    @FXML
    private TextField loginFild;

    @FXML
    private Button loginSignUpBtn;

    @FXML
    private PasswordField passwordFild;

    @FXML
    void initialize() {
        authSignInBtn.setOnAction( event ->{
            String loginText = loginFild.getText().trim();//без пробелов
            String passwordText = passwordFild.getText().trim();//без пробелов
            if (!loginText.equals("") && !passwordText.equals("")){
                try {
                    loginUser(loginText,passwordText);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }else
                showErrorMessage("Введите данные");

        });

        loginSignUpBtn.setOnAction(event -> {
            openNewWindow("/org/example/kursovaia/signUp.fxml");
        });

    }

    private void loginUser(String loginText, String passwordText) throws SQLException {
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(passwordText);
        ResultSet resultSet = DbWorker.getUser(user);
        int count = 0;
        while (resultSet.next()){
            count++;
        }
        if (count==1){
            openNewWindow("/org/example/kursovaia/mainWindow.fxml");
        }else{
            showErrorMessage("Вы не зарегистрированы");
        }



    }
public void openNewWindow(String window){
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
        Parent root = loader.load();
        Stage stage = (Stage) loginSignUpBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
