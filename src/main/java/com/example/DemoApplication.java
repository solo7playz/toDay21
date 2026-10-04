package com.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class DemoApplication {

    @FXML
    private TextField nameField;

    @FXML
    private TextField infoField;

    @FXML
    private ToggleButton notDoneBtn;

    @FXML
    private ToggleButton doneBtn;

    @FXML
    private ListView<Task> taskListView;

    private final TaskService taskService = new TaskService();
    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    private ToggleGroup statusGroup = new ToggleGroup();
    private boolean isDone = false;

    @FXML
    public void initialize() {
        taskListView.setItems(tasks);
        taskListView.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Task task, boolean empty) {
                super.updateItem(task, empty);
                if (empty || task == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(task.toString());
                    if (task.isDone()) {
                        setStyle("-fx-text-fill: #a6e3a1;");
                    } else {
                        setStyle("-fx-text-fill: #f38ba8;");
                    }
                }
            }
        });

        notDoneBtn.setToggleGroup(statusGroup);
        doneBtn.setToggleGroup(statusGroup);
        notDoneBtn.setSelected(true);

        taskService.ensureTableExists();
        refreshTasks();
    }

    @FXML
    private void onStatusChanged() {
        isDone = (doneBtn.isSelected());
    }

    @FXML
    private void onAddTask() {
        String name = nameField.getText().trim();
        String info = infoField.getText() != null ? infoField.getText().trim() : "";

        if (name.isEmpty()) {
            nameField.setStyle("-fx-border-color: #f38ba8;");
            return;
        }

        Task task = new Task(name, info, isDone);
        taskService.addTask(task);

        nameField.clear();
        infoField.clear();
        nameField.setStyle("");

        isDone = false;
        notDoneBtn.setSelected(true);

        refreshTasks();
    }

    private void refreshTasks() {
        tasks.clear();
        tasks.addAll(taskService.getAllTasks());
    }
}
