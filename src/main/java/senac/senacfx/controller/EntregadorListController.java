package senac.senacfx.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import senac.senacfx.application.Main;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.services.VeiculoService;
import senac.senacfx.model.services.EntregadorService;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EntregadorListController implements Initializable, DataChangeListener {
    //ao inves de implementar um service = new EntregadorService(), ficaria acoplamento forte
    //e seria obrigado a instanciar a classe
    private EntregadorService service;

    @FXML
    private TableView<Entregador> tableViewEntregador;

    @FXML
    private TableColumn<Entregador, Integer> tableColumnId;

    @FXML
    private TableColumn<Entregador, String> tableColumnName;

    @FXML
    private TableColumn<Entregador, String> tableColumnEmail;

    @FXML
    private TableColumn<Entregador, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Entregador, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Entregador, Entregador> tableColumnEDIT;

    @FXML
    private TableColumn<Entregador, Entregador> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Entregador> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Entregador obj = new Entregador();
        createDialogForm(obj,"/gui/EntregadorForm.fxml", parentStage);
    }

    //feito isso usando um set, para injetar dependencia, boa pratica
    //injecao de dependendencia manual, sem framework pra isso
    public void setEntregadorService(EntregadorService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);


        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewEntregador.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service is null!");
        }
        List<Entregador> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewEntregador.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Entregador obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            EntregadorFormController controller = loader.getController();
            controller.setEntregador(obj);
            controller.setServices(new EntregadorService(), new VeiculoService());
            controller.loadAssociatedObjects();
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter Entregador data");
            dialogStage.setScene(new Scene(pane));
            dialogStage.setResizable(false);
            dialogStage.initOwner(parentStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

        } catch (IOException e){
            e.printStackTrace();
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }

    private void initEditButtons() {
        tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEDIT.setCellFactory(param -> new TableCell<Entregador, Entregador>() {
            private final Button button = new Button("Editar");
            @Override
            protected void updateItem(Entregador obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/EntregadorForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Entregador, Entregador>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Entregador obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(event -> removeEntity(obj));
            }
        });
    }

    private void removeEntity(Entregador obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Confirma que quer deletar?");

        if (result.get() == ButtonType.OK){
            if (service == null){
                throw new IllegalStateException("Service estava null");
            }
            try {
                service.remove(obj);
                updateTableView();
            } catch (DbException e){
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

}
