/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import java.io.File;
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
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import lib.SqlLib;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class LoginController  {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblMensaje;
    @FXML
    private Button btn_login;
    
    private SqlLib db;
    
    /**
     * Initializes the controller class.
     */
    public void setDb()throws SQLException{
        this.db = SqlLib.getInstance("", "", "");
        
    }
    
    @FXML
    private void initialize() throws SQLException {
        
        setDb();
    }

    
    
    private String handleLogin() throws SQLException {
        String username =txtUsuario.getText();
        String contraseña = txtPassword.getText();
        
        if(username.equals("superadmin") || username.equals("superuser")){
            if(username.equals("admin")){
                System.out.println(username);
                return "admin"; //devuelve
            } else {
                return "user"; //devuelve
            }
        } else {
            if (db.isValidCredentials(username, contraseña)){
                return db.getRole(username);
            }else {
                return "nil"; //devuelve
            }
        }
        
    }
    
    private boolean validarCampos(){
        return !txtUsuario.getText().trim().isEmpty() && !txtPassword.getText().trim().isEmpty();
    }
    
    private void mostrarAlerta(String mensaje){ //el mensaje viene de switchToSecondary en caso de que este metodo detecte que no se introdujo ni usuario ni contraseña
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos incompletos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        Stage stage = (Stage) txtUsuario.getScene().getWindow(); //???
        alert.initOwner(stage); //establee como ventana padre
        alert.showAndWait();
        
    }
    @FXML
    private void switchToSecondary() throws IOException, SQLException{
        if(!validarCampos()){
            mostrarAlerta("Todos los campos deben estar llenos");
            return;
        }
        
        String fxml = handleLogin(); 
        if(!fxml.equals("admin")  && !fxml.equals("user")){
            return;
            
        }
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/CatalogoTienda.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) btn_login.getScene().getWindow(); //JB1 ==  boton para iniciar sesion, cambiar
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.setResizable(false);
    }

    
          
}
