package senac.senacfx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.VBox;
import senac.senacfx.application.Main;
import senac.senacfx.gui.util.Alerts;
import senac.senacfx.model.services.VeiculoService;
import senac.senacfx.model.services.EntregadorService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemEntregador;
    @FXML
    private MenuItem menuItemVeiculo;
    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemEntregadorAction(){
        loadView("/gui/EntregadorList.fxml", (EntregadorListController controller) -> {
            controller.setEntregadorService(new EntregadorService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemVeiculoAction(){
        loadView("/gui/VeiculoList.fxml", (VeiculoListController controller) -> {
            controller.setVeiculoService(new VeiculoService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAboutAction(){
        loadView("/gui/About.fxml", x -> {});
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
            VBox newVBox = loader.load();

            Scene mainScene = Main.getMainScene();
            VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T controller = loader.getController();
            initializingAction.accept(controller);

        }catch (IOException e){
            Alerts.showAlert("IO EXCEPTION", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
