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
import senac.senacfx.model.entities.Veiculos;
import senac.senacfx.model.services.VeiculoService;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class VeiculoListController implements Initializable, DataChangeListener {
    //ao inves de implementar um service = new VeiculosService(), ficaria acoplamento forte
    //e seria obrigado a instanciar a classe
    private VeiculoService service;

    @FXML
    private TableView<Veiculos> tableViewVeiculos;

    @FXML
    private TableColumn<Veiculos, Integer> tableColumnid_veiculo;

    @FXML
    private TableColumn<Veiculos, String> tableColumnplaca;
    @FXML
    private TableColumn<Veiculos, String> tableColumnmodelo;
    @FXML
    private TableColumn<Veiculos, Year> tableColumnano;
    @FXML
    private TableColumn<Veiculos, String> tableColumncor;
    @FXML
    private TableColumn<Veiculos, String> tableColumnfabricante;
    @FXML
    private TableColumn<Veiculos, String> tableColumnkm;
    @FXML
    private TableColumn<Veiculos, Double> tableColumnvalor_fipe;


    @FXML
    private TableColumn<Veiculos, Veiculos> tableColumnEDIT;

    @FXML
    private TableColumn<Veiculos, Veiculos> tableColumnREMOVE;

    @FXML
    private Button btNew;

    private ObservableList<Veiculos> obsList;

    @FXML
    public void onBtNewAction(ActionEvent event){
        Stage parentStage = Utils.currentStage(event);
        Veiculos obj = new Veiculos();
        createDialogForm(obj, "/gui/VeiculoForm.fxml", parentStage);
    }

    //feito isso usando um set, para injetar dependencia, boa pratica
    //injecao de dependendencia manual, sem framework pra isso
    public void setVeiculoService(VeiculoService service){
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();

    }

    private void initializeNodes() {
        tableColumnid_veiculo.setCellValueFactory(new PropertyValueFactory<>("id_veiculo"));
        tableColumnplaca.setCellValueFactory(new PropertyValueFactory<>("placa"));
        tableColumnmodelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        tableColumnano.setCellValueFactory(new PropertyValueFactory<>("ano"));
        tableColumncor.setCellValueFactory(new PropertyValueFactory<>("cor"));
        tableColumnfabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        tableColumnkm.setCellValueFactory(new PropertyValueFactory<>("km"));
        tableColumnvalor_fipe.setCellValueFactory(new PropertyValueFactory<>("valor_fipe"));

        Stage stage = (Stage) Main.getMainScene().getWindow();
        tableViewVeiculos.prefHeightProperty().bind(stage.heightProperty());

    }

    public void updateTableView(){
        if (service == null){
            throw new IllegalStateException("Service is null!");
        }
        List<Veiculos> list = service.findAll();
        obsList = FXCollections.observableArrayList(list);
        tableViewVeiculos.setItems(obsList);
        initEditButtons();
        initRemoveButtons();
    }

    private void createDialogForm(Veiculos obj, String absoluteName, Stage parentStage){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            Pane pane = loader.load();

            VeiculoFormController controller = loader.getController();
            controller.setVeiculos(obj);
            controller.setVeiculosService(new VeiculoService());
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Enter veiculos data");
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
        tableColumnEDIT.setCellFactory(param -> new TableCell<Veiculos, Veiculos>() {
            private final Button button = new Button("Editar");
            @Override
            protected void updateItem(Veiculos obj, boolean empty) {
                super.updateItem(obj, empty);
                if (obj == null) {
                    setGraphic(null);
                    return;
                }
                setGraphic(button);
                button.setOnAction(
                        event -> createDialogForm(
                                obj, "/gui/VeiculoForm.fxml",Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons() {
        tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnREMOVE.setCellFactory(param -> new TableCell<Veiculos, Veiculos>() {
            private final Button button = new Button("Remover");

            @Override
            protected void updateItem(Veiculos obj, boolean empty) {
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

    private void removeEntity(Veiculos obj) {
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
