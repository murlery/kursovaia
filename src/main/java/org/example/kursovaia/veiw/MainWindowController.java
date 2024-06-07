package org.example.kursovaia.veiw;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.kursovaia.data.DbWorker;
import org.example.kursovaia.domain.Competition;

public class MainWindowController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private ComboBox comboBoxDate;
    @FXML
    private ComboBox comboBoxLevel;
    @FXML
    private Button downloadButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Competition> competitionTableView;
    @FXML
    private TableColumn<Competition, String> colLevel;
    @FXML
    private TableColumn<Competition, String> colName;
    @FXML
    private TableColumn<Competition, String> colTiming;
    @FXML
    private TableColumn<Competition, String> colAge;
    @FXML
    private TableColumn<Competition, String> colLocation;
    private ObservableList<Competition> data;
    private ObservableList<String> listLevels;
    private ObservableList<String> listDates;
    private ArrayList<Competition> competitionsList;
    @FXML
    void initialize() throws SQLException {
        competitionsList= new ArrayList<>();
        listLevels = FXCollections.observableArrayList("Все уровни", "Областные", "Окружные", "Всероссийские","Международные");
        comboBoxLevel.setItems(listLevels);
        comboBoxLevel.getSelectionModel().selectFirst();
        listDates = FXCollections.observableArrayList("Все турниры", "Предстоящие");
        comboBoxDate.setItems(listDates);
        comboBoxDate.getSelectionModel().selectFirst();

        // Настройка столбцов
        colLevel.setCellValueFactory(new PropertyValueFactory<>("level"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colTiming.setCellValueFactory(new PropertyValueFactory<>("timing"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("ageCategory"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        try {
            data=FXCollections.observableArrayList(competitionsList);
            addCompetitionsList();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        competitionTableView.setItems(data);
        exitButton.setOnAction(event ->{
            openNewWindow("/org/example/kursovaia/hello-view.fxml");
        });
        comboBoxLevel.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTableView());
        comboBoxDate.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTableView());
searchButton.setOnAction(event ->{
    searchCompetition();
});
        downloadButton.setOnAction(event -> {
            try {
                saveTableViewToCSV();
            } catch (IOException e) {
                // Вывод сообщения об ошибке пользователю
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка");
                alert.setHeaderText("Ошибка сохранения таблицы");
                alert.setContentText("Произошла ошибка при сохранении таблицы в файл.");
                alert.showAndWait();
            }
        });
    }
    private void saveTableViewToCSV() throws IOException {
        // Получение данных из таблицы
        ObservableList<Competition> competitions = competitionTableView.getItems();
        //выбор куда сохранить файл
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить таблицу");
        fileChooser.setInitialFileName("competitions.csv");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV-файлы", "*.csv")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            java.io.FileWriter fileWriter = new java.io.FileWriter(file);
            // Запись заголовков столбцов
            fileWriter.write(colLevel.getText() + "," +
                    colName.getText() + "," +
                    colTiming.getText() + "," +
                    colAge.getText() + "," +
                    colLocation.getText() + "\n");
            // Запись данных из таблицы
            for (Competition competition : competitions) {
                fileWriter.write(competition.getLevel() + "," +
                        competition.getName() + "," +
                        competition.getTiming() + "," +
                        competition.getAgeCategory() + "," +
                        competition.getLocation() + "\n");
            }
            // Закрытие
            fileWriter.close();
        }
    }
    public void searchCompetition() {

        String searchString = searchField.getText().toLowerCase();
        // Проверка, пустое ли поле поиска
        if (searchString.isEmpty()) {
            // Вывод сообщения, если поле пустое
            showMessage("Пожалуйста, введите текст для поиска.");
            return; // Выход из метода, если поле пустое
        }
        ObservableList<Competition> searchResults = FXCollections.observableArrayList();
        for (Competition competition : competitionsList) {
            if (competition.getName().toLowerCase().contains(searchString)) {
                searchResults.add(competition);
            }
        }
        // Создание нового окна
        Stage newWindow = new Stage();
        newWindow.setTitle("Результаты поиска");
        // Создание новой таблицы для результатов поиска
        TableView<Competition> searchTableView = new TableView<>();
        TableColumn<Competition, String> nameColumn = new TableColumn<>("Название");
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        TableColumn<Competition, String> levelColumn = new TableColumn<>("Уровень");
        levelColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLevel()));
        TableColumn<Competition, String> dateColumn = new TableColumn<>("Дата");
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTiming()));
        TableColumn<Competition, String> ageColumn = new TableColumn<>("Возраст");
        ageColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAgeCategory()));
        TableColumn<Competition, String> placeColumn = new TableColumn<>("Место");
        placeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLocation()));
        searchTableView.getColumns().addAll(nameColumn, levelColumn, dateColumn, ageColumn, placeColumn);
        // Установка результатов поиска в таблицу
        searchTableView.setItems(searchResults);
        // Проверка, найдены ли результаты
        if (searchResults.isEmpty()) {
            // Вывод сообщения, если результаты не найдены
            showMessage("Соревнований, соответствующих вашему запросу, не найдено.");
            searchField.setText("");
            return; // Выход из метода, если результаты не найдены
        }
        // Создание сцены для нового окна
        Scene scene = new Scene(searchTableView, 850, 400);
        newWindow.setScene(scene);
        // Отображение нового окна
        newWindow.show();
        // Очистка поля поиска
        searchField.setText("");
    }
    private void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void updateTableView() {
        String selectedLevel = (String) comboBoxLevel.getValue();
        String selectedDate = (String) comboBoxDate.getValue();
        try {
            data.clear();
            // Получение данных из базы данных в соответствии с выбранными значениями
            if (selectedLevel.equals("Все уровни") && selectedDate.equals("Все турниры")) {
                competitionsList = DbWorker.getAll();
            } else if (selectedLevel.equals("Все уровни") && selectedDate.equals("Предстоящие")) {
                competitionsList = DbWorker.getCompetitionsByDate();
            } else if (selectedDate.equals("Все турниры")) {
                competitionsList = DbWorker.getCompetitionsByLevel(selectedLevel);
            } else {
                competitionsList = DbWorker.getCompetitionsByLevelAndDate(selectedLevel);
            }
            data.addAll(competitionsList);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void addCompetitionsList() throws SQLException {
        competitionsList = DbWorker.getAll();
        data.addAll(competitionsList);
    }
    public void openNewWindow(String window){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(window));
            Parent root = loader.load();
            Stage stage = (Stage) exitButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
