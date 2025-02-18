package models;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Objects;

public class Pret {
    private int idPret;
    private float montantPret;
    private Date datePret;
    private float tmm;
    private String categorie;
    private int ageEmploye;
    private float revenusBruts;
    private float taux;
    private int dureeRemboursement;

    // ✅ Constructeur par défaut (utile pour Hibernate/JPA)
    public Pret() {
    }

    public Pret(int idPret, float montantPret, Date datePret, float tmm, float taux, float revenusBruts, int ageEmploye, int dureeRemboursement, String categorie) {
        this.idPret = idPret;
        this.montantPret = montantPret;
        this.datePret = datePret;
        this.tmm = tmm;
        this.taux = taux;
        this.revenusBruts = revenusBruts;
        this.ageEmploye = ageEmploye;
        this.dureeRemboursement = dureeRemboursement;
        this.categorie = categorie;
    }


    public Pret(int idPret, Date datePret, float tmm, float montantPret, String categorie, int ageEmploye, float revenusBruts, float taux, int dureeRemboursement) {
        this.idPret = idPret;
        this.datePret = datePret;
        this.tmm = tmm;
        this.montantPret = montantPret;
        this.categorie = categorie;
        this.ageEmploye = ageEmploye;
        this.revenusBruts = revenusBruts;
        this.taux = taux;
        this.dureeRemboursement = dureeRemboursement;
    }

    // ✅ Getters
    public int getIdPret() {
        return idPret;
    }

    public float getTmm() {
        return tmm;
    }

    public float getMontantPret() {
        return montantPret;
    }

    public String getCategorie() {
        return categorie;
    }

    public int getAgeEmploye() {
        return ageEmploye;
    }

    public float getRevenusBruts() {
        return revenusBruts;
    }

    public float getTaux() {
        return taux;
    }

    public int getDureeRemboursement() {
        return dureeRemboursement;
    }

    public Date getDatePret() {
        return datePret;
    }

    public void setIdPret(int idPret) {
        this.idPret = idPret;
    }

    public void setMontantPret(float montantPret) {
        this.montantPret = montantPret;
    }

    public void setDatePret(Date datePret) {
        this.datePret = datePret;
    }

    public void setTmm(float tmm) {
        this.tmm = tmm;
    }

    public void setTaux(float taux) {
        this.taux = taux;
    }

    public void setRevenusBruts(float revenusBruts) {
        this.revenusBruts = revenusBruts;
    }

    public void setAgeEmploye(int ageEmploye) {
        this.ageEmploye = ageEmploye;
    }

    public void setDureeRemboursement(int dureeRemboursement) {
        this.dureeRemboursement = dureeRemboursement;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }



    // ✅ toString() amélioré
    @Override
    public String toString() {
        return "Pret{" +
                "idPret=" + idPret +
                ", tmm=" + tmm +
                ", montantPret=" + montantPret +
                ", categorie='" + categorie + '\'' +
                ", ageEmploye=" + ageEmploye +
                ", revenusBruts=" + revenusBruts +
                ", taux=" + taux +
                ", dureeRemboursement=" + dureeRemboursement +
                ", datePret=" + datePret +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pret pret = (Pret) o;
        return idPret == pret.idPret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPret);
    }


}
