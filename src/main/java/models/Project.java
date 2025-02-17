package models;

import java.sql.Date;

public class Project {
    //Attributs
    private int id;
    private String titre;
    private String description;
    private String statut;
    private Date date_debut;
    private Date date_fin;

    //Constructeurs
    public Project() {}
    public Project(int id, String titre, String description, String statut, Date date_debut, Date date_fin) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.statut = statut;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }

    public Project(String titre, String description, String statut, Date date_debut, Date date_fin) {
        this.titre = titre;
        this.description = description;
        this.statut = statut;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDate_debut() {
        return date_debut;
    }

    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }

    public Date getDate_fin() {
        return date_fin;
    }

    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }

    //Display
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", statut='" + statut + '\'' +
                ", date_debut=" + date_debut +
                ", date_fin=" + date_fin +
                '}';
    }

}
