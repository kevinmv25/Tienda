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
            // Inicializas tu librería con los datos de tu DB "tienda"
            sql = SqlLib.getInstance("jdbc:mysql://userT:contra123@localhost:3306/tienda", "userT", "contra123");
            
            // Configuras cómo se verán los datos en las columnas
            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            colCategoria.setCellValueFactory(new PropertyValueFactory<>("tipo"));
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
                listaProductos.add(new Producto(
                    Integer.parseInt(fila[0]), // idProducto
                    fila[1], (float)           // nombreProducto
                    Double.parseDouble(fila[2]),// precio
                    fila[3],                   // tipoProducto
                    fila[4]                    // caducidad
                ));
            }
            tablaP.setItems(listaProductos);
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
        System.out.println("Botón eliminar presionado");
    }
    
    @FXML
    private void OnCambios(ActionEvent event) {
        // Por ahora puedes dejarlo vacío o con un print para probar
        System.out.println("Botón eliminar presionado");
    }
    
}
