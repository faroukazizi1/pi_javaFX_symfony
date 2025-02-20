package GroupSuccess.esprit;

import Service.absenceService;  // Import de ton service absenceService
import Service.penaliteService;
import Util.DBconnection;  // Import de la classe DBConnection
import models.absence;  // Import de la classe absence
import models.penalite;

import java.sql.Connection;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
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
        absence absence1 = new absence(11, new Date(), 19, "non_juss");
        absenceService.add(absence1);
        System.out.println("Absence ajoutée avec succès !");

        absence updatedAbsence = new absence(3, new Date(System.currentTimeMillis()), 13, "non justifiee");
        absenceService.update(updatedAbsence);
        System.out.println("Absence mise à jour avec succès !");

        absence absenceToDelete = new absence(9, null, 13, "non justifiee");
        absenceToDelete.setId_abs(5);
        absenceService.delete(absenceToDelete);
        System.out.println("Absence supprimée avec succès !");

        System.out.println("\nListe des absences existantes :");
        for (absence abs : absenceService.getAll()) {
            System.out.println("ID: " + abs.getId_abs() + ", Date: " + abs.getDate() + ", Nombre d'absences: " + abs.getNbr_abs() + ", Type: " + abs.getType());
        }

        // Gestion des pénalités
        penaliteService penaliteService = new penaliteService();
        absenceService absenceServicee = new absenceService();  // Si nécessaire pour associer l'absence

        // Exemple d'ajout de pénalité
        try {
            // Exemple d'ajout : Créer une pénalité pour une absence donnée
            int absenceId = 2;  // ID de l'absence à associer (tu peux récupérer cet ID dynamiquement en fonction de l'absence)
            penalite penalite1 = new penalite(0, "ll", 2, absenceId); // ID 0 car on laisse la base de données gérer l'ID
            penaliteService.add(penalite1);
            System.out.println("Pénalité ajoutée avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout de la pénalité : " + e.getMessage());
        }

        // Exemple de mise à jour de pénalité
        try {
            penalite updatedPenalite = new penalite(6, "avertissement écrit", 3, 2); // ID 6 à mettre à jour
            penaliteService.update(updatedPenalite);
            System.out.println("Pénalité mise à jour avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour de la pénalité : " + e.getMessage());
        }

        // Exemple de suppression de pénalité
        try {
            penalite penaliteToDelete = new penalite(7, null, 0, 0);  // ID 7 de la pénalité à supprimer
            penaliteService.delete(penaliteToDelete);
            System.out.println("Pénalité supprimée avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur lors de la suppression de la pénalité : " + e.getMessage());
        }

        // Affichage de la liste des pénalités existantes
        System.out.println("\nListe des pénalités existantes :");
        try {
            for (penalite pen : penaliteService.getAll()) {
                System.out.println("ID: " + pen.getId_pen() + ", Type: " + pen.getType() + ", Seuil d'absence: " + pen.getSeuil_abs() + ", ID Absence: " + pen.getId_absence());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'affichage des pénalités : " + e.getMessage());
        }
    }
}
