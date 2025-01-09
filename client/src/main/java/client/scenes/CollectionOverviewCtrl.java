package client.scenes;

import client.Main;
import client.utils.CollectionUtils;
import com.google.inject.Inject;
import commons.CollectionPreview;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.swing.*;
import java.util.List;

public class CollectionOverviewCtrl {
    private CollectionUtils server;



    private final MainCtrl mainCtrl;
    private List<CollectionPreview> collections;
    private ObservableList<CollectionPreview> data;
    @FXML
    private TableView<CollectionPreview> table;
    @FXML
    private TableColumn<CollectionPreview,String> collectionTitles;

    public void showAdd() {
        mainCtrl.showAddCollection();
    }
    public List<CollectionPreview> getCollections() {
        return collections;
    }

    public void init(){
        collectionTitles.setCellValueFactory(q -> new SimpleStringProperty(q.getValue().getName()));

    }
    @Inject
    public CollectionOverviewCtrl(MainCtrl mainCtrl, Main main, CollectionUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;


    }

    public void refresh(){

        fetchCollections();
        setViewableCollections(collections);
    }

    public void fetchCollections() {
        try {
            collections = server.getIdsAndTitles();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            String errorMessage = "Error retrieving data from the server, unable to refresh collections";
            JOptionPane.showMessageDialog(null, errorMessage, "ERROR", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void setViewableCollections(List<CollectionPreview> notes) {
        data = FXCollections.observableList(notes);
        table.setItems(data);
    }
}
