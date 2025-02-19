package models;

public class penalite {
    private int id_pen;
    private String type;
    private int seuil_abs;

    // Constructeur
    public penalite(int id_pen, String type, int seuil_abs) {
        this.id_pen = id_pen;
        this.type = type;
        this.seuil_abs = seuil_abs;
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

    // Méthode toString
    @Override
    public String toString() {
        return "Penalite{" +
                "id_pen=" + id_pen +
                ", type='" + type + '\'' +
                ", seuil_abs=" + seuil_abs +
                '}';
    }
}

