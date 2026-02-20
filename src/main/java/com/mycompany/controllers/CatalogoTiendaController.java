package com.mycompany.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class CatalogoTiendaController implements Initializable {
    @FXML
    private VBox listaProductos;
    
    @FXML
    private Label LabelCantidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (LabelCantidad != null) {
            LabelCantidad.setText("Productos agregados");
        } else {
            System.err.println("ADVERTENCIA: no se encontro ningun articulo registrado");
        }
    }
    
    @FXML
    private void handleAgregarProducto(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String nombre = btn.getId(); 
        
        if (nombre == null) {
            nombre = "Producto sin nombre";
        }

        Label fila = new Label(nombre);
        fila.setStyle("-fx-padding: 8; -fx-font-size: 15px; -fx-text-fill: #333333;");
        fila.setMaxWidth(Double.MAX_VALUE);

        listaProductos.getChildren().add(fila);
        actualizarContador();
    }
    
    private void actualizarContador() {
        int cantidad = listaProductos.getChildren().size();
        if (LabelCantidad != null) {
            LabelCantidad.setText(cantidad + " productos seleccionados");
        }
    }
    
    @FXML
    private void handleCancelar() {
        listaProductos.getChildren().clear();
        if (LabelCantidad != null) {
            LabelCantidad.setText("Productos agregados");
        }
        System.out.println("Lista de productos limpiada.");
    }
}