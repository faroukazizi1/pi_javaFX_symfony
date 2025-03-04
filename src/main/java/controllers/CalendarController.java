package controllers;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.ProjectTask;
import services.ProjectTaskService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CalendarController {
    public static ProjectTaskService projectTaskService = new ProjectTaskService();

    public static void openCalendar() throws SQLException {
        Stage primaryStage = new Stage();
        System.out.println("CalendarController.initialize");
        CalendarView calendarView = new CalendarView();
        Calendar tasks = new Calendar("Tasks"); // (2)

        tasks.setStyle(Calendar.Style.STYLE1); // (3)

        CalendarSource myCalendarSource = new CalendarSource("My Calendars"); // (4)
        myCalendarSource.getCalendars().addAll(tasks);

        calendarView.getCalendarSources().addAll(myCalendarSource); // (5)

        calendarView.setRequestedTime(LocalTime.now());

        Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(() -> {
                        calendarView.setToday(LocalDate.now());
                        calendarView.setTime(LocalTime.now());
                    });

                    try {
                        // update every 10 seconds
                        sleep(10000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                    }

                }
            }
        };

        updateTimeThread.setPriority(Thread.MIN_PRIORITY);
        updateTimeThread.setDaemon(true);
        updateTimeThread.start();

        Scene scene = new Scene(calendarView);

        primaryStage.setScene(scene); // Set the scene to the primary stage
        primaryStage.show(); // Show the primary stage

        List<ProjectTask> projectTasks = projectTaskService.readAll();

        for (ProjectTask task : projectTasks) {
            Entry<?> entry = new Entry<>(task.getTitre());
            entry.changeStartDate(task.getDate().toLocalDate());
            entry.changeEndDate(task.getDate().toLocalDate());
            tasks.addEntry(entry);
        }
    }
}
