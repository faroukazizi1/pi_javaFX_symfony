package GroupSuccess.esprit;

import Util.DBconnection;
import models.TypeConge;
import models.StatutDemande;
import models.BulletinPaie;
import Services.BulletinPaieService;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

import static Services.GestionConge.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../../../resources/Test.fxml"));
        primaryStage.setTitle("Gestion des Congés et Bulletins de Paie");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();

        // Lancer la logique métier après le démarrage de l'interface graphique
        runBusinessLogic();
    }

    public static void main(String[] args) {
        launch(args);  // Démarrer JavaFX et appeler start()
    }

    private void runBusinessLogic() {
        Connection conn = DBconnection.getInstance().getConn();

        // Soumettre une demande de congé
        soumettreDemandeConge(3, "2025-02-20", "2025-02-25", String.valueOf(TypeConge.ANNUEL), "Vacances");

        // Mise à jour d'une demande de congé
        int demandeId = 3;
        String nouvelleDateDebut = "2025-02-22";
        String nouvelleDateFin = "2025-02-27";
        TypeConge nouveauTypeConge = TypeConge.MALADIE;
        String nouveauCommentaire = "Repos";

        updateDemandeConge(conn, demandeId, nouvelleDateDebut, nouvelleDateFin, nouveauTypeConge, nouveauCommentaire);
        System.out.println("\nDemandes en attente après mise à jour :");
        afficherDemandesEnAttente(conn);

        // Traitement et suppression d'une demande de congé
        System.out.println("\nSuppression de la demande avec ID 1...");
        traiterDemandeConge(conn, 1, StatutDemande.APPROUVE);
        supprimerDemandeConge(13);

        // Création d'un bulletin de paie
        BulletinPaie bulletin = new BulletinPaie();
        bulletin.setEmployeId(3);
        bulletin.setMois("Février");
        bulletin.setAnnee(2025);
        bulletin.setSalaireBrut(new BigDecimal("2500.00"));
        bulletin.setDeductions(new BigDecimal("300.00"));
        bulletin.setSalaireNet(new BigDecimal("2200.00"));

        BulletinPaieService.createBulletinPaie(bulletin);
        System.out.println("Bulletin de paie créé pour Février 2025");

        // Affichage des bulletins de paie d'un employé
        List<BulletinPaie> bulletins = BulletinPaieService.getBulletinsPaieByEmployeId(3);
        for (BulletinPaie b : bulletins) {
            System.out.println(b.getMois() + " " + b.getAnnee() + ": Salaire net = " + b.getSalaireNet());
        }

        // Récupération et mise à jour d'un bulletin de paie
        BulletinPaie retrievedBulletin = BulletinPaieService.getBulletinPaieById(1);
        System.out.println("Bulletin ID 1: Salaire net = " + retrievedBulletin.getSalaireNet());

        retrievedBulletin.setSalaireNet(new BigDecimal("2300.00"));
        BulletinPaieService.updateBulletinPaie(retrievedBulletin);
        System.out.println("Bulletin ID 1 mis à jour avec nouveau salaire net = " + retrievedBulletin.getSalaireNet());

        // Suppression d'un bulletin de paie
        BulletinPaieService.deleteBulletinPaie(13);
        System.out.println("Bulletin de paie ID 2 supprimé.");
    }
}
