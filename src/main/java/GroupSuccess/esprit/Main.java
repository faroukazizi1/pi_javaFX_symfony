package GroupSuccess.esprit;

import Util.DBconnection;
import models.Project;
import models.ProjectTask;
import services.ProjectServices;
import services.ProjectTaskService;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        // Initialiser la connexion à la base de données
        DBconnection.getInstance();

        // Initialiser le service pour les projets
        ProjectServices project = new ProjectServices();  // Crée une instance de ProjectServices
        ProjectTaskService projectTask = new ProjectTaskService();

// 🔹 Ajouter un projet
        Project newProject = new Project("SGBD", "Meilleur Matière", "DONE",
                Date.valueOf("2023-05-14"), Date.valueOf("2024-06-24"));
        project.add(newProject);  // Appel à la méthode add de ProjectServices

// 🔹 Update d'un projet
        Project updatedProject = new Project(4, "WorkshopCRUD", "Projet du 3éme année", "IN_PROGRESS",
                Date.valueOf("2024-01-10"), Date.valueOf("2024-05-11"));
        project.update(updatedProject);  // Appel à la méthode update de ProjectServices

// 🔹 Suppression d'un projet (tester avec un ID existant et inexistant)
        Project pToDelete = new Project();
        pToDelete.setId(2);
        project.delete(pToDelete);  // Appel à la méthode delete de ProjectServices

 // 🔹 Afficher tous les projets
        System.out.println("\n📌 Liste des projets après l'ajout :");
        List<Project> projects = project.getAll();  // Appel à la méthode getAll de ProjectServices
        for (Project p : projects) {
            System.out.println("🔹 " + p.getId() + " | " + p.getTitre() + " | " + p.getDescription() + " | " +
                    p.getStatut() + " | " + p.getDate_debut() + " → " + p.getDate_fin());
        }


        //  --------🔹🔹🔹🔹🔹🔹🔹🔹-----ProjectTask-----🔹🔹🔹🔹🔹🔹🔹----------
        System.out.println("\n ----------------------- ProjectTask ------------------------:");

// 🔹Créer un nouvel objet ProjectTask
        ProjectTask newTask = new ProjectTask("Analyse Numerique ",
                "Matiére 3A ",
                Date.valueOf("2025-05-25"),
                ProjectTask.Statut.TODO,
                3  ,
                5);
        // Ajouter une nouvelle tâche
        try {
            projectTask.create(newTask);
            System.out.println("✅ Tâche ajoutée avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la tâche : " + e.getMessage());
        }

// 🔹 Update d'une tâche existante
        ProjectTask updatedTask = new ProjectTask(3, "GITHUB", "Push your work crud", Date.valueOf("2025-03-11"),
                ProjectTask.Statut.IN_PROGRESS, 3 , 4);

        if (updatedTask.getId() > 0) {
            try {
                projectTask.update(updatedTask);
                System.out.println("✅ Tâche mise à jour avec succès.");
            } catch (SQLException e) {
                System.err.println("❌ Erreur lors de la mise à jour de la tâche : " + e.getMessage());
            }
        } else {
            System.err.println("❌ ID de la tâche invalide.");
        }

// 🔹 Changer le statut d'une tâche
        int taskIdToChangeStatus = 6;  // Assurez-vous que l'ID existe en base de données

        try {
            projectTask.changeTaskStatus(taskIdToChangeStatus, ProjectTask.Statut.DONE);
            System.out.println("✅ Statut de la tâche changé avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du changement de statut : " + e.getMessage());
        }

// 🔹 Assigner une tâche à un utilisateur
        int taskIdToAssign = 5;  // Assurez-vous que l'ID existe en base de données
        int userIdToAssign = 1;  // ID de l'utilisateur à assigner

        try {
            projectTask.assignTaskToUser(taskIdToAssign, userIdToAssign);
            System.out.println("✅ Tâche assignée à un utilisateur avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'assignation de la tâche : " + e.getMessage());
        }

// 🔹 Supprimer une tâche
        try {
            projectTask.delete(8);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }

// 🔹 Afficher toutes les tâches
        try {
            List<ProjectTask> tasks = projectTask.readAll();
            System.out.println("📌 Liste des tâches après suppression :");
            for (ProjectTask task : tasks) {
                System.out.println(task);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des tâches après suppression : " + e.getMessage());
        }

    }
}