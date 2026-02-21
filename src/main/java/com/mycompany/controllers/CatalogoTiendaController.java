package com.mycompany.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import lib.SqlLib;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class CatalogoTiendaController implements Initializable {
    
    private double total =  0;
    
    @FXML
    private VBox listaProductos;
    
    @FXML
    private Label LabelCantidad;
    
    @FXML
    private Label LabelVenta;
    
    @FXML
   
    
    private Map<String, Integer> carrito = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LabelVenta.setText("$ 0.00");
        
        if (LabelCantidad != null) {
            LabelCantidad.setText("Productos agregados");
        } else {
            System.err.println("ADVERTENCIA: no se encontro ningun articulo registrado");
        }
    }
    
    @FXML
    private void handleAgregarProducto(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String nombre = btn.getId();  //guarda el nombre del action o el handle que se le asigno
        
        try {
            
            SqlLib sql = SqlLib.getInstance("jdbc:mysql://localhost:3306/tienda", "root", "contraseña"); 
            
            //cada que se presione el boton, mandara el nombre que guardo del btn y lo pasará como atributo
            String[] producto = sql.buscarProductoPorNombre(nombre); 
            
            if(producto != null){
               String nombreProd = producto[1]; //el indice indica el dato 0: idProducto 1: nombre 2: precio etc
               double precio = Double.parseDouble(producto[2]);
               // contar los productos
               carrito.put(nombreProd,
                       carrito.getOrDefault(nombreProd, 0)+1);
               int cantidad = carrito.get(nombreProd);
               
              
               
               total = total + precio;
               
               actualizarLista();
               
               LabelVenta.setText(
                         "Total: $" + String.format("%.2f", total));
               
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        
    }
    
    private void actualizarContador() {
        int cantidadTotal = 0;
        // toma la cantidad de cada producto y la va sumando
        for (int cant: carrito.values()){  
            cantidadTotal += cant;
        }
        LabelCantidad.setText(cantidadTotal + "  productos");
    }
    
    @FXML
    private void handleCancelar() {
        total = 0;
        carrito.clear();
        listaProductos.getChildren().clear();

        LabelCantidad.setText("Productos");
        LabelVenta.setText("$ 0.00");
    }
    
    private void actualizarLista(){
        listaProductos.getChildren().clear();
        
        for(String nombre : carrito.keySet()){
            int cantidad = carrito.get(nombre);
            
            Label fila = new Label(nombre + "  x" + cantidad);
            listaProductos.getChildren().add(fila);
        }
        actualizarContador();
        
    }
    

}