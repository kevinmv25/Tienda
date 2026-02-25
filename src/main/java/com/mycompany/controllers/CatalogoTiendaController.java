package com.mycompany.controllers;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */

import java.net.URL;
import java.sql.SQLException;
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
   
    
    private Map<String, Integer> carrito = new HashMap<>(); //contador de veces que sucede un evento por asi decirlo

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
            String urlConCredenciales = "jdbc:mysql://userT:contra123@localhost:3306/tienda";
            SqlLib sql = SqlLib.getInstance(urlConCredenciales, "userT", "contra123");
            
            //cada que se presione el boton, mandara el nombre que guardo del btn y buscara en la base de datos y regresra en forma de array los datos
            String[] producto = sql.buscarProductoPorNombre(nombre); 
            
            if(producto != null){
               String nombreProd = producto[1]; //el indice indica el dato 0:idProducto          1:nombre          2: precio etc
               double precio = Double.parseDouble(producto[2]);
               // contar los productos
               carrito.put(nombreProd,
                       //si ya existe suma 1 sino se queda en 0 
                       carrito.getOrDefault(nombreProd, 0)+1);
               
               total = total + precio; //variable global qeu va sumando el total de los productos
               
               actualizarLista();
               
               LabelVenta.setText(
                         "Total: $" + String.format("%.2f", total));
               
            }
            
        } catch (Exception e){
            e.printStackTrace();
        }
        
        
    }
    /**
     * Este metodo actualiza la cantidad total de todos los productos que sean agregados
     */
    private void actualizarContador() {
        int cantidadTotal = 0;
        // toma la cantidad de cada producto y la va sumando
        for (int cant: carrito.values()){ //obtiene el valor del hash(carrito) y 
            cantidadTotal += cant; //va sumando los valores del hash ejemplo 3 atunes, 2 emrmeladas y el total es 5 productos (CANTIDAD TOTAL)
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
    
    /**
     * este metodo va actualizando la cantidad por producto
     */
    private void actualizarLista() {
        listaProductos.getChildren().clear(); //limpia la lista donde aparecen los productos

        for (String nombre : carrito.keySet()) {
            int cantidad = carrito.get(nombre);

            javafx.scene.layout.HBox filaInteractiva = new javafx.scene.layout.HBox(10); //se crea una fila horizontal
            filaInteractiva.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            
            filaInteractiva.setMaxWidth(Double.MAX_VALUE);
            filaInteractiva.setStyle("-fx-padding: 5;");

            Label textoProducto = new Label(nombre + " x" + cantidad); //nombre del producto
            
            javafx.scene.layout.HBox.setHgrow(textoProducto, javafx.scene.layout.Priority.ALWAYS);
            textoProducto.setMaxWidth(Double.MAX_VALUE); 

            Button btnMenos = new Button("-");
            btnMenos.setOnAction(e -> cambiarCantidad(nombre, -1));

            Button btnMas = new Button("+");
            btnMas.setOnAction(e -> cambiarCantidad(nombre, 1));

            Button btnEliminar = new Button("🗑");
            btnEliminar.setStyle("-fx-text-fill: red; -fx-background-color: transparent; -fx-cursor: hand;");
            btnEliminar.setOnAction(e -> eliminarProductoCompleto(nombre));

            filaInteractiva.getChildren().addAll(textoProducto, btnMenos, btnMas, btnEliminar); //orden en el que aparece
            listaProductos.getChildren().add(filaInteractiva);
        }
        actualizarContador();
    }
    
    private void cambiarCantidad(String nombre, int delta) {
        try {
            int cantidadActual = carrito.get(nombre);
            int nuevaCantidad = cantidadActual + delta;

            if (nuevaCantidad > 0) {
                String urlConCredenciales = "jdbc:mysql://userT:contra123@localhost:3306/tienda";
                SqlLib sql = SqlLib.getInstance(urlConCredenciales, "userT", "contra123");
                
                String[] datos = sql.buscarProductoPorNombre(nombre);
                double precio = Double.parseDouble(datos[2]);

                total += (precio * delta); 
                carrito.put(nombre, nuevaCantidad);

                actualizarLista();
                LabelVenta.setText("Total: $" + String.format("%.2f", total));
            } else {
                eliminarProductoCompleto(nombre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void eliminarProductoCompleto(String nombre) {
        try {
            String urlConCredenciales = "jdbc:mysql://userT:contra123@localhost:3306/tienda";
            SqlLib sql = SqlLib.getInstance(urlConCredenciales, "userT", "contra123");
            
            String[] datos = sql.buscarProductoPorNombre(nombre);
            double precioUnitario = Double.parseDouble(datos[2]);

            total -= (precioUnitario * carrito.get(nombre));
            carrito.remove(nombre);

            actualizarLista();
            LabelVenta.setText("Total: $" + String.format("%.2f", total));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

}