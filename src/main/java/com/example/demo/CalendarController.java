package com.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.RowConstraints;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TextArea;


public class CalendarController {
    private Map<LocalDate, String> notes = new HashMap<>();
    private LocalDate selectedDate;

    @FXML
    private Label monthYearLabel;

    @FXML
    private TextArea notePad;

    @FXML
    private GridPane daysOfWeekGrid;

    @FXML
    private GridPane calendarGrid;

    private YearMonth currentYearMonth;

    @FXML
    public void initialize() {
        currentYearMonth = YearMonth.now();  // Set the current month and year
        // Call the method to populate the days of the week
        updateCalendar();  // Update the calendar with the dates of the month
    }

    @FXML
    private void prevMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        updateCalendar();
    }

    @FXML
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        updateCalendar();
    }

    private void updateCalendar() {
        // Update Month and Year Label
        monthYearLabel.setText(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()) + " " + currentYearMonth.getYear());

        // Clear previous calendar
        calendarGrid.getChildren().clear();

        // Set a fixed grid size (7 columns, 6 rows)
        calendarGrid.getColumnConstraints().clear();
        calendarGrid.getRowConstraints().clear();

        for (int i = 0; i < 7; i++) {
            calendarGrid.getColumnConstraints().add(new ColumnConstraints(50)); // Fixed width of 50px per column
        }
        for (int i = 0; i < 6; i++) {
            calendarGrid.getRowConstraints().add(new RowConstraints(50)); // Fixed height of 50px per row
        }

        // Get first day of the month
        LocalDate firstOfMonth = currentYearMonth.atDay(1);
        int dayOfWeekValue = firstOfMonth.getDayOfWeek().getValue(); // 1 (Monday) to 7 (Sunday)

        // Adjust for Sunday as first day if needed
        int startColumn = (dayOfWeekValue == 7) ? 0 : dayOfWeekValue; // Make Sunday=0, Monday=1, ..., Saturday=6

        int daysInMonth = currentYearMonth.lengthOfMonth();

        // Populate calendar with day buttons (7x6 grid)
        int row = 0;
        int col = startColumn;
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            Button dayButton = new Button(String.valueOf(day));

            // Set fixed size for each button
            dayButton.setMinWidth(50);
            dayButton.setMinHeight(50);
            dayButton.setMaxWidth(50);
            dayButton.setMaxHeight(50);

            dayButton.setOnAction(e -> onDateClicked(date));

            // Highlight today's date
            if (date.equals(LocalDate.now())) {
                dayButton.setStyle("-fx-background-color: #ADD8E6;"); // Light blue
            }

            calendarGrid.add(dayButton, col, row);

            col++;
            if (col > 6) { // 7 days a week
                col = 0;
                row++;
            }
        }

        // Add empty buttons for remaining grid spaces if necessary (for months with less than 42 days)
        while (row < 6) { // Max 6 rows
            for (int i = col; i < 7; i++) {
                Button emptyButton = new Button("");
                emptyButton.setStyle("-fx-background-color: transparent;");
                emptyButton.setMinWidth(50);
                emptyButton.setMinHeight(50);
                emptyButton.setMaxWidth(50);
                emptyButton.setMaxHeight(50);
                calendarGrid.add(emptyButton, i, row);
            }
            row++;
        }
    }


    private void onDateClicked(LocalDate date) {
        selectedDate = date;
        String note = notes.getOrDefault(date, "");
        notePad.setText(note);
        System.out.println("Date clicked: " + date);
    }

    @FXML
    private void saveNote() {
        if (selectedDate != null) {
            String note = notePad.getText();
            notes.put(selectedDate, note);
            System.out.println("Note saved for " + selectedDate + ": " + note);
        } else {
            System.out.println("No date selected to save the note.");
        }
    }
}

