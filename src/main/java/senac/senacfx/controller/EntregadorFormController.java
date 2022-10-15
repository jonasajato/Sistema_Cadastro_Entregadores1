package senac.senacfx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import senac.senacfx.db.DbException;
import senac.senacfx.gui.listeners.DataChangeListener;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.gui.util.Constraints;
import senac.senacfx.gui.util.Utils;
import senac.senacfx.model.entities.Veiculos;
import senac.senacfx.model.entities.Entregador;
import senac.senacfx.model.exceptions.ValidationException;
import senac.senacfx.model.services.VeiculoService;
import senac.senacfx.model.services.EntregadorService;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class EntregadorFormController implements Initializable {

    private Entregador entity;

    private EntregadorService service;

    private VeiculoService veiculoService;

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;
    @FXML
    private TextField txtEndereco;
    @FXML
    private TextField txtTelefone;

    @FXML
    private ComboBox<Veiculos> comboBoxVeiculos;
    @FXML
    private Label labelErrorName;

    @FXML
    private Label labelErrorEmail;

    @FXML
    private Label labelErrorBirthDate;

    @FXML
    private Label labelErrorBaseSalary;

    public Label getLabelErrorEndereco() {
        return labelErrorEndereco;
    }

    @FXML
    private Label labelErrorEndereco;
    @FXML
    private Label labelErrorTelefone;

    @FXML
    private Button btSave;

    @FXML
    private Button btCancel;

    private ObservableList<Veiculos> obsList;


    //Contolador agora tem uma instancia do departamento
    public void setEntregador(Entregador entity){
        this.entity = entity;
    }

    public void setServices(EntregadorService service, VeiculoService veiculoService){
        this.service = service;
        this.veiculoService = veiculoService;
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

    private Entregador getFormData() {
        Entregador obj = new Entregador();

        ValidationException exception = new ValidationException("Erro na validacao");

        obj.setId_entregador(Utils.tryParseToInt(txtId.getText()));

        if (txtName.getText() == null || txtName.getText().trim().equals("")){
            exception.addError("nome", "campo nao pode ser vazio");
        }
        obj.setNome(txtName.getText());

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("email", "campo nao pode ser vazio");
        }
        obj.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() == null){
            exception.addError("data de nascimento", "data nao selecionada");
        } else {
            Instant instant = Instant.from(dpBirthDate.getValue().atStartOfDay(ZoneId.systemDefault()));
            obj.setData_de_nascimento(Date.from(instant));
        }

        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals("")){
            exception.addError("salario", "campo nao pode ser vazio");
        }
        obj.setSalario(Utils.tryParseToDouble(txtBaseSalary.getText()));

        obj.setVeiculos(comboBoxVeiculos.getValue());

        if (txtEndereco.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("endereco", "campo nao pode ser vazio");
        }
        obj.setendereco(txtEndereco.getText());

        if (txtTelefone.getText() == null || txtEmail.getText().trim().equals("")){
            exception.addError("telefone", "campo nao pode ser vazio");
        }
        obj.settelefone(txtTelefone.getText());

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
        Constraints.setTextFieldMaxLength(txtName, 45);
        Constraints.setTextFieldMaxLength(txtEmail, 45);
        Utils.formatDatePicker(dpBirthDate, "yyyy/MM/dd");
        Constraints.setTextFieldDouble(txtBaseSalary);
        Constraints.setTextFieldMaxLength(txtEndereco, 150);
        Constraints.setTextFieldMaxLength(txtTelefone, 20);


        initializeComboBoxVeiculos();

    }

    public void updateFormData(){

        if (entity == null){
            throw new IllegalStateException("Entidade nula");
        }

        txtId.setText(String.valueOf(entity.getId_entregador()));
        txtName.setText(entity.getNome());
        txtEmail.setText(entity.getEmail());

        Locale.setDefault(Locale.US);

        if (entity.getData_de_nascimento() != null) {
            dpBirthDate.setValue(LocalDate.ofInstant(entity.getData_de_nascimento().toInstant(), ZoneId.systemDefault()));
        }

        txtBaseSalary.setText(String.format("%.2f", entity.getSalario()));

        if (entity.getVeiculos() == null) {
            comboBoxVeiculos.getSelectionModel().selectFirst();
        } else {
            comboBoxVeiculos.setValue(entity.getVeiculos());
        }

        txtEndereco.setText(entity.getendereco());
        txtTelefone.setText(entity.gettelefone());

    }

    public void loadAssociatedObjects(){

        if (veiculoService == null){
            throw new IllegalStateException("VeiculosService was null");
        }

        List<Veiculos> list = veiculoService.findAll();
        obsList = FXCollections.observableArrayList(list);
        comboBoxVeiculos.setItems(obsList);
    }

    private void setErrorMessages(Map<String, String> errors){
        Set<String> fields = errors.keySet();

        labelErrorName.setText((fields.contains("nome") ? errors.get("nome") : ""));
        labelErrorEmail.setText((fields.contains("email") ? errors.get("email") : ""));
        labelErrorBirthDate.setText((fields.contains("data_de_nascimento") ? errors.get("data_de_nascimento") : ""));
        labelErrorBaseSalary.setText((fields.contains("salario") ? errors.get("salario") : ""));
        labelErrorEndereco.setText((fields.contains("endereco") ? errors.get("endereco") : ""));
        labelErrorTelefone.setText((fields.contains("telefone") ? errors.get("telefone") : ""));

    }

    private void initializeComboBoxVeiculos() {
        Callback<ListView<Veiculos>, ListCell<Veiculos>> factory = lv -> new ListCell<Veiculos>() {
            @Override
            protected void updateItem(Veiculos item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getPlaca());
            }
        };
        comboBoxVeiculos.setCellFactory(factory);
        comboBoxVeiculos.setButtonCell(factory.call(null));
    }

    public void setLabelErrorEndereco(Label labelErrorEndereco) {
        this.labelErrorEndereco = labelErrorEndereco;
    }

    public Button getBtSave() {
        return btSave;
    }

    public void setBtSave(Button btSave) {
        this.btSave = btSave;
    }

    public Button getBtCancel() {
        return btCancel;
    }

    public void setBtCancel(Button btCancel) {
        this.btCancel = btCancel;
    }

    public void setLabelErrorTelefone(Label labelErrorTelefone) {
        this.labelErrorTelefone = labelErrorTelefone;
    }
}
