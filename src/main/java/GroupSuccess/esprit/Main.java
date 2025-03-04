package GroupSuccess.esprit;

import javafx.application.Application;
import javafx.stage.Stage;


import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import Util.DBconnection;
import Model.Formation;
import Service.FormationService;
import Model.Formateur;
import Service.FormateurService;

import java.sql.Connection;
import java.sql.Date;



public class Main extends Application {


    public static void main(String[] args) {

        FormationService fo=new FormationService();
        Date Date_D = Date.valueOf("2025-04-01");  // Format yyyy-mm-dd
        Date Date_F = Date.valueOf("2025-04-10");
        fo.add(new Formation("rrrrrrrrrrrrrrrrr","kedheugyujf",Date_F,Date_D,20,"havxjhvaxkabkxv", 87));
        fo.delete(new Formation(65,"kedhe","kedheugyujf",Date_F,Date_D,20,"havxjhvaxkabkxv", 1));
        fo.update(new Formation(60,"teambulding","pppppppppp",Date_F,Date_D,20,"havxjhvaxkabkxv", 94));
        System.out.println(fo.getAll());
        FormateurService formateurS = new FormateurService();
        formateurS.add(new Formateur(24,"oooooooooo","oooooom ","sai.lftsit.com","math"));
        formateurS.delete(new Formateur(105,24,"menyar","ben ghorbel","said.laffet@esprit.com","math"));
        formateurS.update(new Formateur(87,24,"ppppppppppppp","ben ghorbel","said.laffet@esprit.com","mathhhh"));
        System.out.println(formateurS.getAll());
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Connection conn = DBconnection.getInstance().getConn();

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {







    }
}
