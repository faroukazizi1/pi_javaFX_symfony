package Model;

import javafx.beans.binding.BooleanExpression;

import java.sql.Date;

public class Formateur {
    int id_Formateur, Numero;
    String Nom_F, Prenom_F, Email, Specialite;

    public Formateur() {
    }

    public Formateur(int id_Formateur, int Numero, String Nom_F, String Prenom_F, String Email, String Specialite) {
        this.id_Formateur = id_Formateur;
        this.Numero = Numero;
        this.Nom_F = Nom_F;
        this.Prenom_F = Prenom_F;
        this.Email = Email;
        this.Specialite = Specialite;

    }

    public Formateur(int Numero, String Nom_F, String Prenom_F, String Email, String Specialite) {
        this.Numero = Numero;
        this.Nom_F = Nom_F;
        this.Prenom_F = Prenom_F;
        this.Email = Email;
        this.Specialite = Specialite;


    }

    public int getid_Formateur() {
        return id_Formateur;
    }

    public void setid_Formateur(int id_Formateur) {
        this.id_Formateur = id_Formateur;
    }

    public int getNumero() {
        return Numero;
    }

    public void setNumero(int Numero) {
        this.Numero = Numero;
    }

    public String getNom_F() {
        return Nom_F;
    }

    public void setNom_F(String Nom_F) {
        this.Nom_F = Nom_F;
    }

    public String getPrenom_F() {
        return Prenom_F;
    }

    public void setPrenom_F(String Prenom_F) {
        this.Prenom_F = Prenom_F;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSpecialite() {
        return Specialite;
    }

    public void setSpecialite(String Specialite) {
        this.Specialite = Specialite;
    }

    @Override
    public String toString() {
        return "Formateur{" +
                "id_Formateur=" + id_Formateur +
                ", Numero=" + Numero +
                ", Nom_F='" + Nom_F + '\'' +
                ", Prenom_F='" + Prenom_F + '\'' +
                ", Email='" + Email + '\'' +
                ", Specialite='" + Specialite + '\'' +


                '}';
    }
}


