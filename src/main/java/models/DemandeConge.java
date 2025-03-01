package models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class DemandeConge {

    private SimpleIntegerProperty id;
    private SimpleIntegerProperty employeId;
    private SimpleStringProperty type;
    private SimpleStringProperty debut;
    private SimpleStringProperty fin;
    private SimpleStringProperty statut;
    private SimpleStringProperty dateDemande;

    public DemandeConge(int id, int employeId, String type, String debut, String fin, String dateDemande, String statut) {
        this.id = new SimpleIntegerProperty(id);
        this.employeId = new SimpleIntegerProperty(employeId);
        this.type = new SimpleStringProperty(type);
        this.debut = new SimpleStringProperty(debut);
        this.fin = new SimpleStringProperty(fin);
        this.dateDemande = new SimpleStringProperty(dateDemande);
        this.statut = new SimpleStringProperty(statut);
    }

    // Getters and setters

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public int getEmployeId() {
        return employeId.get();
    }

    public SimpleIntegerProperty employeIdProperty() {
        return employeId;
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public String getDebut() {
        return debut.get();
    }

    public SimpleStringProperty debutProperty() {
        return debut;
    }

    public String getFin() {
        return fin.get();
    }

    public SimpleStringProperty finProperty() {
        return fin;
    }

    public String getStatut() {
        return statut.get();
    }

    public SimpleStringProperty statutProperty() {
        return statut;
    }

    public String getDateDemande() {
        return dateDemande.get();
    }

    public SimpleStringProperty dateDemandeProperty() {
        return dateDemande;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setEmployeId(int employeId) {
        this.employeId.set(employeId);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setDebut(String debut) {
        this.debut.set(debut);
    }

    public void setFin(String fin) {
        this.fin.set(fin);
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }

    public void setDateDemande(String dateDemande) {
        this.dateDemande.set(dateDemande);
    }
}
