/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.controllers;


import com.mycompany.objects.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lib.SqlLib;


/**
 *FXML Controller class
 * @author kevin
 */

public class EliminarProductoController {

    @FXML
    private Label lblNombreProducto;

    private Producto producto;
    
    @FXML
    private Button btnCancelar;
    
    @FXML
    private Button btnEliminar;
    
    private InventarioController controllerPrincipal;
    
    public void setControllerPrincipal(InventarioController controllerPrincipal) {
        this.controllerPrincipal = controllerPrincipal;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;

        lblNombreProducto.setText(producto.getNombreProducto());
    }

    @FXML
    private void eliminarProducto() {

        try {

            SqlLib db = SqlLib.getInstance(
                "jdbc:mysql://localhost:3306/tienda",
                "root",
                "Bebe2508_"
            );

            boolean eliminado = db.eliminarProducto(producto.getIdProducto());

            if (eliminado) {
                System.out.println("Producto eliminado correctamente");
                if (controllerPrincipal != null) {
                    controllerPrincipal.actualizarTabla();
                }
            }

            Stage stage = (Stage) lblNombreProducto.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) lblNombreProducto.getScene().getWindow();
        stage.close();
    }
}
