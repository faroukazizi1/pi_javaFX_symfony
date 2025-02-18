package services;

import Util.DBconnection;
import models.ProjectTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectTaskService {

    static Connection conn = DBconnection.getInstance().getConn();

    public void create(ProjectTask task) throws SQLException {
        String query = "INSERT INTO project_task (titre, description, date, statut, project_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, task.getTitre());
        preparedStatement.setString(2, task.getDescription());
        preparedStatement.setDate(3, task.getDate());
        preparedStatement.setString(4, task.getStatut().toString());
        preparedStatement.setInt(5, task.getProject_id());
        preparedStatement.setInt(6, task.getUser_test_id());
        preparedStatement.executeUpdate();
    }
    public static void create2(ProjectTask task) throws SQLException {
        String query = "INSERT INTO project_task (titre, description, date, statut, project_id, user_id) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, task.getTitre());
        preparedStatement.setString(2, task.getDescription());
        preparedStatement.setDate(3, task.getDate());
        preparedStatement.setString(4, task.getStatut().toString());
        preparedStatement.setInt(5, task.getProject_id());
        preparedStatement.setInt(6, task.getUser_test_id());
        preparedStatement.executeUpdate();
    }

    public void update(ProjectTask task) throws SQLException {
        String query = "UPDATE `project_task` SET titre = ?, description = ?, statut = ?, date = ? , project_id = ?, user_id =? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, task.getTitre());
        preparedStatement.setString(2, task.getDescription());
        preparedStatement.setString(3, task.getStatut().toString());
        preparedStatement.setDate(4,task.getDate());
        preparedStatement.setInt(5, task.getProject_id());  // Nouvelle valeur pour project_id
        preparedStatement.setInt(6, task.getUser_test_id());     // Nouvelle valeur pour user_id
        preparedStatement.setInt(7, task.getId());
        preparedStatement.executeUpdate();
    }

    public List<ProjectTask> readAll() throws SQLException {
        String query = "SELECT * FROM project_task";
        List<ProjectTask> projectTasks = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            projectTasks.add(new ProjectTask(
                    resultSet.getInt("id"),
                    resultSet.getString("titre"),
                    resultSet.getString("description"),
                    resultSet.getDate("date"),
                    ProjectTask.Statut.valueOf(resultSet.getString("statut")),
                    resultSet.getInt("project_id"),
                    resultSet.getInt("user_id")
            ));
        }

        return projectTasks;
    }

    public void delete(int taskId) throws SQLException {
        String query = "DELETE FROM project_task WHERE id = ?";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, taskId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("⚠️ Aucune tâche trouvée avec l'ID : " + taskId);
            } else {
                System.out.println("✅ Tâche supprimée avec succès.");
            }
        }
    }

    public void changeTaskStatus(int taskId, ProjectTask.Statut statut) throws SQLException {
        String query = "UPDATE project_task SET statut = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, statut.toString());
        preparedStatement.setInt(2, taskId);
        preparedStatement.executeUpdate();
    }

    public void assignTaskToUser(int taskId, int userId) throws SQLException {
        String query = "UPDATE project_task SET user_id = ? WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.setInt(2, taskId);
        preparedStatement.executeUpdate();
    }

    public void removeTaskFromUser(int taskId) throws SQLException {
        String query = "UPDATE project_task SET user_id = NULL WHERE id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, taskId);
        preparedStatement.executeUpdate();
    }

    public List<ProjectTask> getTasksByProject(int projectId) throws SQLException {
        List<ProjectTask> tasks = new ArrayList<>();

        String query = "SELECT * FROM project_task WHERE project_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, projectId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            tasks.add(new ProjectTask(
                    resultSet.getInt("id"),
                    resultSet.getString("titre"),
                    resultSet.getString("description"),
                    resultSet.getDate("date"),
                    ProjectTask.Statut.valueOf(resultSet.getString("statut")),
                    resultSet.getInt("project_id"),
                    resultSet.getInt("user_id")
            ));
        }
        return tasks;
    }

    public List<ProjectTask> getTasksByUser(int userId) throws SQLException {
        List<ProjectTask> tasks = new ArrayList<>();

        String query = "SELECT * FROM project_task WHERE user_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            tasks.add(new ProjectTask(
                    resultSet.getInt("id"),
                    resultSet.getString("titre"),
                    resultSet.getString("description"),
                    resultSet.getDate("date"),
                    ProjectTask.Statut.valueOf(resultSet.getString("statut")),
                    resultSet.getInt("project_id"),
                    resultSet.getInt("user_id")
            ));
        }

        return tasks;
    }

}
