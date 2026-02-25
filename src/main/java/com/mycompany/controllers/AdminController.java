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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class AdminController implements Initializable {
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private ImageView imgPreview;

    private File archivoImagenSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void seleccionarArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");
        
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) txtNombre.getScene().getWindow();
        archivoImagenSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoImagenSeleccionado != null) {
            Image imagen = new Image(archivoImagenSeleccionado.toURI().toString());
            imgPreview.setImage(imagen);
        }
    }

    @FXML
    private void guardarEnBaseDeDatos() {
        String nombre = txtNombre.getText();
        String precioText = txtPrecio.getText();

        if (nombre.isEmpty() || precioText.isEmpty()) {
            System.out.println("Por favor llena todos los campos");
            return;
        }

        try {
            double precio = Double.parseDouble(precioText);
            
            System.out.println("Producto guardado: " + nombre + " - $" + precio);
            limpiarFormulario();
            
        } catch (NumberFormatException e) {
            System.out.println("El precio debe ser un número válido");
        }
    }

    private void limpiarFormulario() {
        txtNombre.clear();
        txtPrecio.clear();
        imgPreview.setImage(null);
        archivoImagenSeleccionado = null;
    }   
    
}
