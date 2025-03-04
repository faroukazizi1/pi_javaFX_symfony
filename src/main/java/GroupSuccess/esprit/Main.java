package GroupSuccess.esprit;

import Service.absenceService;  // Import of your absenceService
import Service.penaliteService;
import Util.DBconnection;  // Import of DBConnection class
import models.absence;  // Import of absence class
import models.penalite;

import java.sql.Connection;
import java.util.Date;  // Import java.sql.Date


public class Main {
    public static void main(String[] args) {
<<<<<<< HEAD
        // Vérification de la connexion à la base de données
        Connection connection = DBconnection.getInstance().getConn();
        if (connection != null) {
            System.out.println("Test réussi : connexion établie !");
        } else {
            System.out.println("Échec de la connexion !");
            return;  // Sortir si la connexion échoue
        }

        // Gestion des absences
        absenceService absenceService = new absenceService();

        // Ajouter une absence
        absence absence1 = new absence(11, 19, (java.sql.Date) new Date(), 1, "non_juss",null);
        absenceService.add(absence1);
        System.out.println("Absence ajoutée avec succès !");

        // Mettre à jour une absence
        java.sql.Date updatedSqlDate = new java.sql.Date(System.currentTimeMillis()); // Mise à jour avec la date actuelle
        absence updatedAbsence = new absence(3, 13, updatedSqlDate, 3, "justifiée", "/path/to/image.jpg");
        absenceService.update(updatedAbsence);  // Si tu passes un chemin d'image ou null
        System.out.println("Absence mise à jour avec succès !");

        // Supprimer une absence
        absence absenceToDelete = new absence(5, 13, null, 0, "non justifiée", null);
        absenceService.delete(absenceToDelete);
        System.out.println("Absence supprimée avec succès !");

        // Afficher la liste des absences existantes
        System.out.println("\nListe des absences existantes :");
        for (absence abs : absenceService.getAll()) {
            System.out.println("ID: " + abs.getId_abs() + ", Date: " + abs.getDate() + ", Nombre d'absences: " + abs.getNbr_abs() + ", Type: " + abs.getType());
        }

        // Gestion des pénalités
        penaliteService penaliteService = new penaliteService();

        // Ajouter une pénalité
        try {
            int absenceId = 2;  // ID de l'absence à associer (tu peux récupérer cet ID dynamiquement en fonction de l'absence)
            penalite penalite1 = new penalite(0, "ll", 2, absenceId); // ID 0 car on laisse la base de données gérer l'ID
            penaliteService.add(penalite1);
            System.out.println("Pénalité ajoutée avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la pénalité : " + e.getMessage());
        }

        // Mettre à jour une pénalité
        try {
            penalite updatedPenalite = new penalite(6, "avertissement écrit", 3, 2); // ID 6 à mettre à jour
            penaliteService.update(updatedPenalite);
            System.out.println("Pénalité mise à jour avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de la pénalité : " + e.getMessage());
        }

        // Supprimer une pénalité
        try {
            penalite penaliteToDelete = new penalite(7, null, 0, 0);  // ID 7 de la pénalité à supprimer
            penaliteService.delete(penaliteToDelete);
            System.out.println("Pénalité supprimée avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la pénalité : " + e.getMessage());
        }

        // Afficher la liste des pénalités existantes
        System.out.println("\nListe des pénalités existantes :");
        try {
            for (penalite pen : penaliteService.getAll()) {
                System.out.println("ID: " + pen.getId_pen() + ", Type: " + pen.getType() + ", Seuil d'absence: " + pen.getSeuil_abs() + ", ID Absence: " + pen.getCin());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des pénalités : " + e.getMessage());
        }
=======
>>>>>>> 0542470027de48c818e3792b43273f5b8dd31e9b
    }
}
