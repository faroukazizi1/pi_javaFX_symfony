package models;

import java.sql.Date;

public class absence {
    private int id_abs;
    private int cin; // Ajout de cin en deuxième position
    private Date date;  // Utilise java.sql.Date ici
    private int nbr_abs; // Nombre d'absences
    private String type;
    private String imagePath;  // Ajoute imagePath pour stocker le chemin de l'image

    // Constructeur avec imagePath
    public absence(int id_abs, int cin, Date date, int nbr_abs, String type, String imagePath) {
        this.id_abs = id_abs;
        this.cin = cin;
        this.date = date;
        this.nbr_abs = nbr_abs;
        this.type = type;
        this.imagePath = imagePath; // Initialise imagePath
    }

    // Constructeur sans imagePath (par défaut imagePath sera null)
    public absence(int id_abs, int cin, Date date, int nbr_abs, String type) {
        this(id_abs, cin, date, nbr_abs, type, null);  // Appelle l'autre constructeur en passant null pour imagePath
    }

    // Getters et Setters
    public int getId_abs() {
        return id_abs;
    }

    public void setId_abs(int id_abs) {
        this.id_abs = id_abs;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getNbr_abs() {
        return nbr_abs;
    }

    public void setNbr_abs(int nbr_abs) {
        this.nbr_abs = nbr_abs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    // Nouvelle méthode pour obtenir le nombre d'absences
    public int getNombreAbsences() {
        return nbr_abs;
    }

    // Méthode toString
    @Override
    public String toString() {
        return "Absence{" +
                "id_abs=" + id_abs +
                ", cin=" + cin + // Affichage en deuxième position
                ", date=" + date +
                ", nbr_abs=" + nbr_abs +
                ", type='" + type + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
