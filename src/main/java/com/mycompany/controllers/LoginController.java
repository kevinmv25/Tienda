/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class LoginController implements Initializable {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMensaje;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void manejarLogin() {
        String user = txtUsuario.getText();
        String pass = txtPassword.getText();

        if (user.equals("admin") && pass.equals("1234")) {
            // SI ES ADMIN: Abrimos la interfaz de gestión
            cambiarEscena("/scenes/Admin.fxml", "Panel de Administración");
        } else if (user.equals("user") && pass.equals("1234")) {
            // SI ES USUARIO: Abrimos el catálogo normal
            cambiarEscena("/scenes/CatalogoTienda.fxml", "Catálogo de Productos");
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
            txtPassword.clear();
        }
    }
    private void cambiarEscena(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(titulo);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            lblMensaje.setText("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
