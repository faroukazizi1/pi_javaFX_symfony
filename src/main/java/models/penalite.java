package models;

public class penalite {
    private int id_pen;
    private String type;
    private int seuil_abs;
    private int id_absence; // Ajout de l'ID de l'absence

    // Constructeur avec id_absence
    public penalite(int id_pen, String type, int seuil_abs, int id_absence) {
        this.id_pen = id_pen;
        this.type = type;
        this.seuil_abs = seuil_abs;
        this.id_absence = id_absence;
    }

    // Getters et Setters
    public int getId_pen() {
        return id_pen;
    }

    public void setId_pen(int id_pen) {
        this.id_pen = id_pen;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSeuil_abs() {
        return seuil_abs;
    }

    public void setSeuil_abs(int seuil_abs) {
        this.seuil_abs = seuil_abs;
    }

    public int getId_absence() {
        return id_absence;
    }

    public void setId_absence(int id_absence) {
        this.id_absence = id_absence;
    }

    // Méthode toString mise à jour
    @Override
    public String toString() {
        return "Penalite{" +
                "id_pen=" + id_pen +
                ", type='" + type + '\'' +
                ", seuil_abs=" + seuil_abs +
                ", id_absence=" + id_absence +
                '}';
    }
}
