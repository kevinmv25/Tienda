/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.objets.Producto;
import com.mycompany.objets.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;

/**
 *
 * @author Miguel Angel Marcelo
 */
public class EditarProductoController {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtCantidad;

    @FXML
    private ChoiceBox<String> choiceTipo;
    
    @FXML
    private ImageView imgProducto;

    private Producto producto;

    @FXML
    public void initialize() {
           
        choiceTipo.getItems().addAll(
            "Bebidas",
            "Comida",
            "Electrónica",
            "Limpieza"
        );

    }
    
    public void setProducto(Producto producto) {

        this.producto = producto;

        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(String.valueOf(producto.getPrecio()));
        txtCantidad.setText(producto.getCantidad());

        choiceTipo.setValue(producto.getTipo());
    }
    
    @FXML
    private void seleccionarImagen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imagenes","*.png","*.jpg","*.jpeg"));
        
        File archivo = fileChooser.showOpenDialog(null);
        
        if (archivo !=null){
            Image imagen = new Image(archivo.toURI().toString());
            imgProducto.setImage(imagen);
        }
    }
    
    @FXML
    private void cancelar(){
        Stage stage = (Stage) txtNombre.getScene(). getWindow();
        stage.close();
    }
    
    @FXML
    private void guardarCambios(){
        producto.setNombre(txtNombre.getText());
        producto.setPrecio(Float.parseFloat(txtPrecio.getText()));
        producto.setTipo(choiceTipo.getValue());
        
        System.out.println("Se han realizado los cambios");
        
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }
    
}
