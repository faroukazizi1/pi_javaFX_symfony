package models;

import java.sql.Date;

public class ProjectTask {
    private Integer id;
    private String titre;
    private String description;
    private Date date;
    private Statut statut;
    private Integer project_id;
    private Integer user_id;

    public enum Statut { TODO, IN_PROGRESS, DONE }

    public ProjectTask() {}
    // Constructeur avec ID (utile pour update)
    public ProjectTask(Integer id, String titre, String description, Date date, Statut statut, Integer project_id, Integer user_test_id) {
        this.id = id;
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.project_id = project_id;
        this.user_id = user_test_id;
    }

    // Constructeur sans ID (utile pour insert)
    public ProjectTask(String titre, String description, Date date, Statut statut, Integer project_id, Integer user_test_id) {
        this.titre = titre;
        this.description = description;
        this.date = date;
        this.statut = statut;
        this.project_id = project_id;
        this.user_id = user_test_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public Integer getUser_test_id() {
        return user_id;
    }

    public void setUser_test_id(Integer user_test_id) {
        this.user_id = user_test_id;
    }

    @Override
    public String toString() {
        return "ProjectTask{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                ", statut=" + statut +
                ", project_id=" + project_id +
                ", user_test_id=" + user_id +
                '}';
    }

}
