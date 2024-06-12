package org.example.kursovaia.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.kursovaia.data.DbWorker;
import org.example.kursovaia.domain.User;

import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SignUpController {

    @FXML
    private Button signUpBtn;
    @FXML
    private Button backButton;

    @FXML
    private DatePicker dateOfBirthFild;

    @FXML
    private Label dateOfBirthLable;

    @FXML
    private TextField loginFild;

    @FXML
    private Label lognLable;

    @FXML
    private RadioButton manRadBtn;

    @FXML
    private TextField nameFild;

    @FXML
    private Label nameLable;

    @FXML
    private Label nameLable1;

    @FXML
    private TextField numberPhoneFild;

    @FXML
    private Label numberPnoneLable;

    @FXML
    private PasswordField passwordFild;

    @FXML
    private Label passwordLable;

    @FXML
    private PasswordField passwordRepeatFild;

    @FXML
    private Label passwordRepeatLable;

    @FXML
    private TextField patronymicFild;

    @FXML
    private Label patronymicLable;

    @FXML
    private TextField surnameFild;

    @FXML
    private Label surnameLable;

    @FXML
    private RadioButton womanRadBtn;
private User user;

    @FXML
    void initialize() {

        loginFild.textProperty().addListener((observable, oldValue, newValue) -> {
            // Проверяем, содержит ли новый текст русские буквы
            if (newValue.matches(".*[а-яА-Я].*")) {
                // Если да, то устанавливаем текст поля в предыдущее значение
                loginFild.setText(oldValue);
            }
        });

        // Флаг для отслеживания наличия открывающей скобки
        final boolean[] hasOpeningBracket = {false};

        // Добавляем слушателя события нажатия клавиши в поле ввода номера телефона
        numberPhoneFild.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            // Проверяем, является ли нажатая клавиша буквой
            if (Character.isLetter(event.getCharacter().charAt(0))) {
                // Если нажата буква, игнорируем ввод
                event.consume();
                return;
            }

            // Проверяем длину введенного текста
            if (numberPhoneFild.getText().length() < 14) {
                // Если длина введенного текста равна 0, добавляем "+7(" и устанавливаем флаг
                if (numberPhoneFild.getText().length() == 0 && !hasOpeningBracket[0]) {
                    numberPhoneFild.setText("+7(");
                    numberPhoneFild.positionCaret(3);
                    hasOpeningBracket[0] = true;
                } else if (numberPhoneFild.getText().length() == 6 && hasOpeningBracket[0]) { // Проверяем длину после ввода 6 символов
                    // Добавляем ")" после 6 символов
                    numberPhoneFild.setText(numberPhoneFild.getText() + ")");
                    numberPhoneFild.positionCaret(7);
                    // Сбрасываем флаг, т.к. скобка добавлена
                    hasOpeningBracket[0] = false;
                }
            } else {
                // Если длина введенного текста больше или равна 14, игнорируем ввод
                event.consume();
            }
        });

        nameFild.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String formattedValue = newValue.substring(0, 1).toUpperCase() + newValue.substring(1).toLowerCase();
                nameFild.setText(formattedValue.replaceAll("[^а-яА-ЯёЁa-zA-Z]", ""));
            }
        });

        surnameFild.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String formattedValue = newValue.substring(0, 1).toUpperCase() + newValue.substring(1).toLowerCase();
                surnameFild.setText(formattedValue.replaceAll("[^а-яА-ЯёЁa-zA-Z]", ""));
            }
        });

        patronymicFild.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String formattedValue = newValue.substring(0, 1).toUpperCase() + newValue.substring(1).toLowerCase();
                patronymicFild.setText(formattedValue.replaceAll("[^а-яА-ЯёЁa-zA-Z]", ""));
            }
        });

        // Ограничение выбора даты до текущей
        dateOfBirthFild.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isAfter(LocalDate.now()));
            }
        });

        ToggleGroup genderGroup = new ToggleGroup();
        womanRadBtn.setToggleGroup(genderGroup);
        manRadBtn.setToggleGroup(genderGroup);

        signUpBtn.setOnAction(event -> {
            // Проверка заполнения всех обязательных полей
            if (loginFild.getText().isEmpty() || numberPhoneFild.getText().isEmpty() || nameFild.getText().isEmpty() || surnameFild.getText().isEmpty() || dateOfBirthFild.getValue() == null || genderGroup.getSelectedToggle() == null || passwordFild.getText().isEmpty()) {
                showErrorMessage("Пожалуйста, заполните все обязательные поля");
                return; // Прервать обработку
            }
            // Проверка уникальности логина
            if (!user.isUniqueLogin(loginFild.getText())) {
                showErrorMessage("Логин уже занят");
                return; // Прервать обработку
            }

            // Проверка номера телефона
            if (!isValidPhoneNumber(numberPhoneFild.getText())) {
                showErrorMessage("Неверный формат номера телефона");
                return; // Прервать обработку
            }

            String password = passwordFild.getText();
            String passwordRepeat = passwordRepeatFild.getText();

            // Проверяем, совпадают ли пароль и подтверждение пароля
            if (!password.equals(passwordRepeat)) {
                showErrorMessage("Пароли не совпадают");
                return; // Прервать обработку
            }

            String gender = womanRadBtn.isSelected() ? "женский" : "мужской"; // Get selected gender
            LocalDate dateOfBirth = dateOfBirthFild.getValue();
            user = new User(loginFild.getText(), numberPhoneFild.getText(),
                    nameFild.getText(), surnameFild.getText(),
                    patronymicFild.getText(), dateOfBirth,
                    gender, passwordFild.getText());
            user.signUpUser();
            openNewWindow("/org/example/kursovaia/mainWindow.fxml");
        });
        backButton.setOnAction(event -> {
            openNewWindow("/org/example/kursovaia/hello-view.fxml");
        });
    }

    // Метод для проверки номера телефона
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Пример регулярного выражения для российского номера телефона
        String regex = "^\\+7\\(\\d{3}\\)\\d{7}$"; // Изменено для включения скобок
        return Pattern.matches(regex, phoneNumber);
    }

    // Метод для вывода диалогового окна с ошибкой
    private void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void openNewWindow(String window){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
            Parent root = loader.load();
            Stage stage = (Stage) signUpBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}