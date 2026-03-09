/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.objets.Producto;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lib.SqlLib;

/**
 * FXML Controller class
 *
 * @author rojas
 */
public class InventarioController implements Initializable {
    
    @FXML private TableView<Producto> tablaP; 
    @FXML private TableColumn<Producto, String> colNombre;
    @FXML private TableColumn<Producto, String> colCategoria;
    @FXML private TableColumn<Producto, Float> colPrecio;
    @FXML private TableColumn<Producto, String> colCaducidad;
    
    private SqlLib sql; // Tu clase de librería
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            sql = SqlLib.getInstance("jdbc:mysql://userT:contra123@localhost:3306/tienda", "userT", "contra123");

            // LOS NOMBRES DEBEN COINCIDIR CON TU CLASE PRODUCTO.JAVA
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));    // Antes tenías "nombreProducto"
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colCategoria.setCellValueFactory(new PropertyValueFactory<>("tipo"));    // Antes tenías "tipoProducto"
            colCaducidad.setCellValueFactory(new PropertyValueFactory<>("caducidad"));

            actualizarTabla();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    } 
    
    public void actualizarTabla() {
        listaProductos.clear();
        try {
            List<String[]> datos = sql.cargarProductosDesdeBD(); 
            for (String[] fila : datos) {
                try {
                    // Usamos Float.parseFloat para que coincida con tu clase Producto y tu SQL
                    int id = Integer.parseInt(fila[0]);
                    String nombre = fila[1];
                    float precio = Float.parseFloat(fila[2]); 
                    String tipo = fila[3];
                    String caducidad = (fila[4] == null) ? "" : fila[4]; // Evita errores si la fecha es null

                    listaProductos.add(new Producto(id, nombre, precio, tipo, caducidad));
                } catch (Exception e) {
                    System.err.println("Error procesando un producto: " + e.getMessage());
                }
            }
            tablaP.setItems(listaProductos);
            tablaP.refresh(); // Forzamos el redibujado de la tabla
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void OnAgregar(ActionEvent event) {
        try {
            // CAMBIO AQUÍ: La carpeta real es /scenes/
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Agregar.fxml"));
            Parent root = loader.load();

            AgregarController agregarCtrl = loader.getController();
            agregarCtrl.setControllerPrincipal(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            actualizarTabla(); 
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar Agregar.fxml. Revisa la ruta.");
        }
    }
    
    @FXML
    private void OnEliminar(ActionEvent event) {
        // Por ahora puedes dejarlo vacío o con un print para probar
        System.out.println("");
    }
    
    @FXML
    private void OnCambios(ActionEvent event) {
        // Por ahora puedes dejarlo vacío o con un print para probar
        System.out.println("");
    }
    
    @FXML
    private void regresarAlLogin(ActionEvent event) {
        try {
            // Cargamos la escena del Login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Login.fxml"));
            Parent root = loader.load();

            // Obtenemos el Stage actual desde el botón que activó el evento
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // Cambiamos la escena
            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Ajustamos la ventana al tamaño del login y la centramos
            stage.sizeToScene();
            stage.centerOnScreen();
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al intentar volver al Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
