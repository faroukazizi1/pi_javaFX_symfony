package Gui;

import Model.promotion;
import Model.user;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import Service.promotionService;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Date;
import java.util.List;

public class AfficherPromotion {

    @FXML
    private TableColumn<promotion, String> Colavantage;

    @FXML
    private TableColumn<promotion, Date> Coldate;

    @FXML
    private TableColumn<promotion, Void> Coldelete;

    @FXML
    private TableColumn<promotion, Integer> Colid;

    @FXML
    private TableColumn<promotion, Integer> Colid_user;

    @FXML
    private TableColumn<promotion, Void> Colmodifier;

    @FXML
    private TableColumn<promotion, String> Colraison;

    @FXML
    private TableColumn<promotion, Double> Colsalaire;

    @FXML
    private TableColumn<promotion, String> Coltype;

    @FXML
    private TableColumn<?, ?> Colposte;


    @FXML
    private TableView<promotion> Tableview;


    private user currentUser;
    private final promotionService service = new promotionService();

    @FXML
    void initialize(user user) {

    }

    public void initData(user user) {
        if (user == null) {
            System.out.println("⚠ initData: L'utilisateur est NULL !");
            return;
        }

        this.currentUser = user;
        System.out.println("✅ initData: Chargement des promotions pour " + user.getUsername());

        try {
            List<promotion> tab_promotion = service.getByUser_id(user);
            ObservableList<promotion> observableList = FXCollections.observableList(tab_promotion);
            Tableview.setItems(observableList);

            Colid.setCellValueFactory(new PropertyValueFactory<>("id"));
            Coltype.setCellValueFactory(new PropertyValueFactory<>("type_promo"));
            Colraison.setCellValueFactory(new PropertyValueFactory<>("raison"));
            Colposte.setCellValueFactory(new PropertyValueFactory<>("poste_promo"));
            Coldate.setCellValueFactory(new PropertyValueFactory<>("date_prom"));
            Colsalaire.setCellValueFactory(new PropertyValueFactory<>("nouv_sal"));
            Colavantage.setCellValueFactory(new PropertyValueFactory<>("avs"));
            Colid_user.setCellValueFactory(new PropertyValueFactory<>("id_user"));

            System.out.println("✅ initData: Nombre de promotions chargées = " + tab_promotion.size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}