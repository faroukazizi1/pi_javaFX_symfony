package models;

import java.sql.Date;

public class Reponse {
    private int idReponse;
    private int idPret; // Add this field to link to Pret
    private Date dateReponse;
    private double montantDemande;
    private double revenusBruts;
    private double tauxInteret;
    private double mensualiteCredit;
    private double potentielCredit;
    private int dureeRemboursement;
    private double montantAutorise;
    private double assurance;

    public Reponse(int idReponse, Date dateReponse, double montantDemande, double revenusBruts,
                   double tauxInteret, double mensualiteCredit, double potentielCredit,
                   int dureeRemboursement, double montantAutorise, double assurance) {
        this.idReponse = idReponse;
        this.dateReponse = dateReponse;
        this.montantDemande = montantDemande;
        this.revenusBruts = revenusBruts;
        this.tauxInteret = tauxInteret;
        this.mensualiteCredit = mensualiteCredit;
        this.potentielCredit = potentielCredit;
        this.dureeRemboursement = dureeRemboursement;
        this.montantAutorise = montantAutorise;
        this.assurance = assurance;
    }

    public int getIdReponse() { return idReponse; }
    public void setIdReponse(int idReponse) { this.idReponse = idReponse; }

    public Date getDateReponse() { return dateReponse; }
    public void setDateReponse(Date dateReponse) { this.dateReponse = dateReponse; }

    public double getMontantDemande() { return montantDemande; }
    public void setMontantDemande(double montantDemande) { this.montantDemande = montantDemande; }

    public double getRevenusBruts() { return revenusBruts; }
    public void setRevenusBruts(double revenusBruts) { this.revenusBruts = revenusBruts; }

    public double getTauxInteret() { return tauxInteret; }
    public void setTauxInteret(double tauxInteret) { this.tauxInteret = tauxInteret; }

    public double getMensualiteCredit() { return mensualiteCredit; }
    public void setMensualiteCredit(double mensualiteCredit) { this.mensualiteCredit = mensualiteCredit; }

    public double getPotentielCredit() { return potentielCredit; }
    public void setPotentielCredit(double potentielCredit) { this.potentielCredit = potentielCredit; }

    public int getDureeRemboursement() { return dureeRemboursement; }
    public void setDureeRemboursement(int dureeRemboursement) { this.dureeRemboursement = dureeRemboursement; }

    public double getMontantAutorise() { return montantAutorise; }
    public void setMontantAutorise(double montantAutorise) { this.montantAutorise = montantAutorise; }

    public double getAssurance() { return assurance; }
    public void setAssurance(double assurance) { this.assurance = assurance; }

    public Reponse() {}

    @Override
    public String toString() {
        return "Reponse {" +
                "idReponse=" + idReponse +
                ", dateReponse=" + dateReponse +
                ", montantDemande=" + montantDemande +
                ", revenusBruts=" + revenusBruts +
                ", tauxInteret=" + tauxInteret +
                ", mensualiteCredit=" + mensualiteCredit +
                ", potentielCredit=" + potentielCredit +
                ", dureeRemboursement=" + dureeRemboursement +
                ", montantAutorise=" + montantAutorise +
                ", assurance=" + assurance +
                '}';
    }
    public void calculerValeursAutomatiques() {
        double montantDemande = this.getMontantDemande();
        int duree = this.getDureeRemboursement();

        double tauxInteret = 5.0;
        double tauxMensuel = tauxInteret / 12 / 100;

        double revenusBruts = montantDemande * 0.4;
        double mensualiteCredit = (tauxMensuel > 0) ?
                (montantDemande * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree)) :
                montantDemande / duree;

        double potentielCredit = revenusBruts * 0.3;
        double montantAutorise = potentielCredit * 0.9;
        double assurance = montantDemande * 0.02;

        this.setRevenusBruts(revenusBruts);
        this.setTauxInteret(tauxInteret);
        this.setMensualiteCredit(mensualiteCredit);
        this.setMontantAutorise(montantAutorise);
        this.setAssurance(assurance);
    }
    public double getTauxMensuel() {
        return this.tauxInteret / 12 / 100;
    }
    public int getIdPret() {
        return idPret;
    }

    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

}
