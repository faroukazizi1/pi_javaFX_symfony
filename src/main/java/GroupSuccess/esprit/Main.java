package GroupSuccess.esprit;

import Service.PretService;
import Service.ReponseService;
import models.Pret;
import models.Reponse;

import java.sql.Date;

public class Main {
    public static void main(String[] args) {}
//        PretService pretService = new PretService();
//        ReponseService reponseService = new ReponseService();
//
//        // Ajout d'un prêt dans la base de données
//        Pret newPret = new Pret(1, new Date(System.currentTimeMillis()), 888.0f, 12.0f, "cadre", 30, 30.0f, 2.0f, 3);
//        pretService.add(newPret);
//
//        // Mise à jour d'un prêt
//        Pret pret = new Pret(1, new Date(System.currentTimeMillis()), 22.0f, 12.0f, "cadre", 30, 30.0f, 2.0f, 3);
//        int oldId = 4;
//        pret.setMontantPret(1.0f);
//        pret.setDatePret(new Date(System.currentTimeMillis() + 18000));
//        pret.setTmm(999.0f);
//        pret.setTaux(18.0f);
//        pret.setRevenusBruts(180.0f);
//        pret.setAgeEmploye(45);
//        pret.setDureeRemboursement(6);
//        pret.setCategorie("cadre");
//        pretService.update(pret, oldId);
//
//        // Suppression d'un prêt
//        pretService.delete(5);
//
//        // Affichage des prêts
//        System.out.println("\nListe des prêts existants :");
//        for (Pret p : pretService.getAll()) {
//            System.out.println("ID: " + p.getIdPret() + ", Montant: " + p.getMontantPret() + ", Date: " + p.getDatePret());
//        }
//
//        // Gestion des réponses
//        double montantDemande = 8000.0; // Exemple de montant demandé
//        double revenusBruts = 3000.0; // Exemple de revenus bruts
//        int dureeRemboursement = 6; // Durée en mois
//        double tauxInteret = 0.05; // Taux d'intérêt de 5%
//
//        // Calcul des valeurs
//        double mensualiteCredit = (montantDemande * tauxInteret / 12) / (1 - Math.pow(1 + tauxInteret / 12, -dureeRemboursement));
//        double potentielCredit = revenusBruts * 0.35;
//        double montantAutorise = Math.min(montantDemande, potentielCredit * dureeRemboursement);
//        double assurance = montantAutorise * 0.01;
//
//        // Ajout d'une réponse avec les valeurs calculées
//        Reponse newReponse = new Reponse(1, new Date(System.currentTimeMillis()), montantDemande, revenusBruts, tauxInteret, mensualiteCredit, potentielCredit, dureeRemboursement, montantAutorise, assurance);
//        reponseService.add(newReponse);
//
//        // Mise à jour d'une réponse
//        int oldReponseId = 4;
//        newReponse.setMontant_demande(9500.0);
//        newReponse.setRevenus_bruts(3200.0);
//        newReponse.setTaux_interet(0.045);
//        newReponse.setMensualite_credit((newReponse.getMontant_demande() * newReponse.getTaux_interet() / 12) / (1 - Math.pow(1 + newReponse.getTaux_interet() / 12, -newReponse.getDuree_remboursement())));
//        newReponse.setPotentiel_credit(newReponse.getRevenus_bruts() * 0.35);
//        newReponse.setMontant_autorise(Math.min(newReponse.getMontant_demande(), newReponse.getPotentiel_credit() * newReponse.getDuree_remboursement()));
//        newReponse.setAssurance(newReponse.getMontant_autorise() * 0.01);
//
//        reponseService.update(newReponse, oldReponseId);
//
//        // Suppression d'une réponse
//        reponseService.delete(5);
//
//        // Affichage des réponses
//        System.out.println("\nListe des réponses existantes :");
//        for (Reponse r : reponseService.getAll()) {
//            System.out.println("ID: " + r.getId_reponse() + ", Montant: " + r.getMontant_demande() + ", Mensualité: " + r.getMensualite_credit() + ", Autorisé: " + r.getMontant_autorise());
//        }
    }

