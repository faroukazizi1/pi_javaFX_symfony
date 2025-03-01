package GroupSuccess.esprit;


import Util.DBconnection;
import models.StatutDemande;
import models.TypeConge;
import models.BulletinPaie;
import Services.BulletinPaieService;
import java.math.BigDecimal;
import java.sql.Connection;

import static Services.GestionConge.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = DBconnection.getInstance().getConn();
        if (connection != null) {
            System.out.println("Test réussi : connexion établie !");
        } else {
            System.out.println("Échec de la connexion !");
            return;
        }

//        soumettreDemandeConge(3, "2025-02-20", "2025-02-25", String.valueOf(TypeConge.ANNUEL));
//        traiterDemandeConge(connection, 3, StatutDemande.APPROUVE);

//        supprimerDemandeConge(3);

        BulletinPaie bulletin = new BulletinPaie();
        bulletin.setEmployeId(3);
        bulletin.setMois("12");
        bulletin.setAnnee(2025);
        bulletin.setSalaireBrut(new BigDecimal("1500.00"));
        bulletin.setDeductions(new BigDecimal("600.00"));
        bulletin.setSalaireNet(new BigDecimal("900.00"));


        int generatedId = BulletinPaieService.createBulletinPaie(bulletin);
        if (generatedId != -1) {
            System.out.println("Bulletin de paie créé avec ID = " + generatedId);

            BulletinPaie retrievedBulletin = BulletinPaieService.getBulletinPaieById(generatedId);

            if (retrievedBulletin == null) {
                System.out.println("Erreur: Aucun bulletin de paie trouvé avec l'ID " + generatedId);
            } else {
                System.out.println("Bulletin ID " + generatedId + ": Salaire net = " + retrievedBulletin.getSalaireNet());

//                retrievedBulletin.setSalaireNet(new BigDecimal("2300.00"));
//                BulletinPaieService.updateBulletinPaie(retrievedBulletin);
//                System.out.println("Bulletin ID " + generatedId + " mis à jour avec nouveau salaire net = " + retrievedBulletin.getSalaireNet());
//            }
//        } else {
//            System.out.println("Erreur lors de la création du bulletin de paie.");
//        }
            }

        }
    }
}
