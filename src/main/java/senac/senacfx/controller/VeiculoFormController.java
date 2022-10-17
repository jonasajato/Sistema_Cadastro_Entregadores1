package senac.senacfx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Veiculos;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.VeiculoService;

import java.net.URL;
import java.util.*;

public class VeiculoFormController implements Initializable {

    private Veiculos entity;

    private VeiculoService service;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtPlaca;


    @FXML
    private TextField txtModelo;

    @FXML
    private TextField txtAno;

    @FXML
    private TextField txtCor;
    @FXML
    private TextField txtFabricante;

    @FXML
    private TextField txtKm;

    @FXML
    private TextField txtValor_fipe;

    @FXML
    private Label labelErrorName;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    public VeiculoFormController() {
    }

    //Contolador agora tem uma instancia do departamento
    public void setVeiculos(Veiculos entity){
        this.entity = entity;
    }

    public void setVeiculosService(VeiculoService service){
        this.service = service;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onBtSaveAction(ActionEvent event) {
        //validacao manual pois nao esta sendo usado framework para injetar dependencia
        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }
        if (service == null){
            throw new IllegalStateException("Servico nulo");
        }

        try {
            entity = getFormData();
            service.saveOrUpdate(entity);
            notifyDataChangeListeners();
            Utils.currentStage(event).close();
        } catch (DbException e){
            Alerts.showAlert("Erro ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
        } catch (ValidationException e){
            setErrorMessages(e.getErrors());
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    private Veiculos getFormData() {
        Veiculos obj = new Veiculos();

        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setId_veiculo(Utils.tryParseToInt(txtId.getText()));

        if (txtPlaca.getText() == null || txtPlaca.getText().trim().equals("")){
            exception.addError("placa", "campo nao pode ser vazio");
        }
        obj.setPlaca(txtPlaca.getText());

        if (exception.getErrors().size() > 0){
            throw exception;
        }

        return obj;
    }

    @FXML
    public void onBtCancelAction(ActionEvent event) {
        Utils.currentStage(event).close();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtPlaca);
        Constraints.setTextFieldMaxLength(txtModelo);
        Constraints.setTextFieldMaxLength(txtAno);
        Constraints.setTextFieldMaxLength(txtCor);
        Constraints.setTextFieldMaxLength(txtFabricante);
        Constraints.setTextFieldMaxLength(txtKm);
        Constraints.setTextFieldMaxLength(txtValor_fipe);

    }

    public void updateFormData(){
        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }

        txtId.setText(String.valueOf(entity.getId_veiculo()));
        txtPlaca.setText(entity.getPlaca());
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        if (fields.contains("name")){
            labelErrorName.setText(errors.get("name"));
        }
    }

    public Button getBtSave() {
        return btSave;
    }

    public void setBtSave(Button btSave) {
        this.btSave = btSave;
    }

    public TextField getTxtPlaca() {
        return txtPlaca;
    }

    public void setTxtPlaca(TextField txtPlaca) {
        this.txtPlaca = txtPlaca;
    }

    public TextField getTxtModelo() {
        return txtModelo;
    }

    public void setTxtModelo(TextField txtModelo) {
        this.txtModelo = txtModelo;
    }

    public TextField getTxtAno() {
        return txtAno;
    }

    public void setTxtAno(TextField txtAno) {
        this.txtAno = txtAno;
    }

    public TextField getTxtCor() {
        return txtCor;
    }

    public void setTxtCor(TextField txtCor) {
        this.txtCor = txtCor;
    }

    public TextField getTxtFabricante() {
        return txtFabricante;
    }

    public void setTxtFabricante(TextField txtFabricante) {
        this.txtFabricante = txtFabricante;
    }

    public TextField getTxtKm() {
        return txtKm;
    }

    public void setTxtKm(TextField txtKm) {
        this.txtKm = txtKm;
    }

    public TextField getTxtValor_fipe() {
        return txtValor_fipe;
    }

    public void setTxtValor_fipe(TextField txtValor_fipe) {
        this.txtValor_fipe = txtValor_fipe;
    }
}
