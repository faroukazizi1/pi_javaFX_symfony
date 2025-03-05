package Gui;

public class UserSession {
    private static UserSession instance;

    private int userId;
    private String userName;
    private String prenom;
    private String role; // RHR ou Employé

    private UserSession(int userId, String userName, String prenom, String role) {
        this.userId = userId;
        this.userName = userName;
        this.prenom = prenom;
        this.role = role;
    }

    public static UserSession getInstance(int userId, String userName, String prenom, String role) {
        if (instance == null) {
            instance = new UserSession(userId, userName, prenom, role);
        }
        return instance;
    }

    public static UserSession getInstance() {
        return instance; // Retourne l'instance actuelle (utile pour récupérer les infos ailleurs)
    }




    public void cleanUserSession() {
        instance = null; // Supprime l'instance pour déconnecter l'utilisateur
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getRole() {
        return role;
    }
}
