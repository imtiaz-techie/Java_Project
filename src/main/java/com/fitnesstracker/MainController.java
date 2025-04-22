package com.fitnesstracker;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController implements Initializable {
    @FXML private TableView<Workout> workoutTable;
    @FXML private TableColumn<Workout, LocalDate> dateColumn;
    @FXML private TableColumn<Workout, String> typeColumn;
    @FXML private TableColumn<Workout, Integer> durationColumn;
    @FXML private TableColumn<Workout, String> intensityColumn;
    @FXML private TableColumn<Workout, Integer> caloriesColumn;

    @FXML private ComboBox<String> exerciseTypeCombo;
    @FXML private TextField durationField;
    @FXML private ComboBox<String> intensityCombo;
    @FXML private TextField caloriesField;
    @FXML private DatePicker datePicker;

    @FXML private TableView<Goal> goalsTable;
    @FXML private TableColumn<Goal, String> goalDescriptionColumn;
    @FXML private TableColumn<Goal, Integer> goalTargetColumn;
    @FXML private TableColumn<Goal, LocalDate> goalDeadlineColumn;
    @FXML private TableColumn<Goal, Boolean> goalCompletedColumn;

    @FXML private TextField goalDescriptionField;
    @FXML private TextField goalTargetField;
    @FXML private DatePicker goalDeadlinePicker;

    @FXML private LineChart<String, Number> activityChart;
    
    @FXML private Label totalCaloriesLabel;
    @FXML private Label totalDurationLabel;
    @FXML private Label goalsCompletedLabel;

    private DatabaseManager dbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();

        // Initialize workout table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<>("duration"));
        intensityColumn.setCellValueFactory(new PropertyValueFactory<>("intensity"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("caloriesBurned"));

        // Initialize goals table columns
        goalDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        goalTargetColumn.setCellValueFactory(new PropertyValueFactory<>("targetValue"));
        goalDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));
        goalCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("completed"));

        // Initialize combo boxes
        exerciseTypeCombo.getItems().addAll(Arrays.asList(
            "Running", "Walking", "Cycling", "Swimming", "Weightlifting", "Yoga", "HIIT", "Other"
        ));
        intensityCombo.getItems().addAll(Arrays.asList("Low", "Medium", "High"));

        // Set default values
        datePicker.setValue(LocalDate.now());
        goalDeadlinePicker.setValue(LocalDate.now());

        // Style the chart
        activityChart.setCreateSymbols(true);
        activityChart.setAnimated(true);

        // Load initial data
        refreshWorkouts();
        refreshGoals();
        updateActivityChart();
        updateStatistics();
    }

    @FXML
    private void handleAddWorkout() {
        try {
            String type = exerciseTypeCombo.getValue();
            int duration = Integer.parseInt(durationField.getText());
            String intensity = intensityCombo.getValue();
            int calories = Integer.parseInt(caloriesField.getText());
            LocalDate date = datePicker.getValue();

            if (type == null || intensity == null || date == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            Workout workout = new Workout(type, duration, intensity, calories, date);
            dbManager.addWorkout(workout);

            // Clear fields
            exerciseTypeCombo.setValue(null);
            durationField.clear();
            intensityCombo.setValue(null);
            caloriesField.clear();
            datePicker.setValue(LocalDate.now());

            // Refresh data
            refreshWorkouts();
            updateActivityChart();
            updateStatistics();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter valid numbers for duration and calories.");
        }
    }

    @FXML
    private void handleAddGoal() {
        try {
            String description = goalDescriptionField.getText();
            int target = Integer.parseInt(goalTargetField.getText());
            LocalDate deadline = goalDeadlinePicker.getValue();

            if (description.isEmpty() || deadline == null) {
                showAlert("Error", "Please fill in all fields.");
                return;
            }

            Goal goal = new Goal(description, target, deadline);
            dbManager.addGoal(goal);

            // Clear fields
            goalDescriptionField.clear();
            goalTargetField.clear();
            goalDeadlinePicker.setValue(LocalDate.now());

            // Refresh goals
            refreshGoals();
            updateStatistics();
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid number for the target value.");
        }
    }

    private void refreshWorkouts() {
        List<Workout> workouts = dbManager.getWorkouts();
        workoutTable.getItems().clear();
        workoutTable.getItems().addAll(workouts);
    }

    private void refreshGoals() {
        List<Goal> goals = dbManager.getGoals();
        goalsTable.getItems().clear();
        goalsTable.getItems().addAll(goals);
    }

    private void updateActivityChart() {
        List<Workout> workouts = dbManager.getWorkouts();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Calories Burned");

        for (Workout workout : workouts) {
            series.getData().add(new XYChart.Data<>(
                workout.getDate().toString(),
                workout.getCaloriesBurned()
            ));
        }

        activityChart.getData().clear();
        activityChart.getData().add(series);
    }

    private void updateStatistics() {
        List<Workout> workouts = dbManager.getWorkouts();
        List<Goal> goals = dbManager.getGoals();

        // Calculate total calories
        int totalCalories = workouts.stream()
                .mapToInt(Workout::getCaloriesBurned)
                .sum();
        totalCaloriesLabel.setText(totalCalories + " kcal");

        // Calculate total duration
        int totalDuration = workouts.stream()
                .mapToInt(Workout::getDuration)
                .sum();
        totalDurationLabel.setText(totalDuration + " min");

        // Count completed goals
        long completedGoals = goals.stream()
                .filter(Goal::isCompleted)
                .count();
        goalsCompletedLabel.setText(String.valueOf(completedGoals));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 