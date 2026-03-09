/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.objets.Producto;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author rojas, marcelo
 */
public class AgregarController implements Initializable {
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private ImageView imgPreview;
    @FXML private ComboBox<String> cbCategoria;

    private InventarioController principal;
    private File archivoImagenSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> opciones = FXCollections.observableArrayList(
            "Abarrotes", 
            "Limpieza", 
            "Electrónica", 
            "Papelería", 
            "Varios"
        );
        cbCategoria.setItems(opciones);
    }
    
    
    @FXML
    private void regresarInventario() { // Le cambiamos el nombre para que sea claro
        try {
            // 1. Cambiamos la ruta a la de tu interfaz principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/Inventario.fxml"));
            Parent root = loader.load();

            // 2. Obtenemos la ventana (Stage) actual
            Stage stage = (Stage) txtNombre.getScene().getWindow();

            // 3. Creamos la nueva escena con el inventario
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.centerOnScreen(); // Opcional: para que la ventana se centre
            stage.show();

        } catch (Exception e) {
            System.err.println("Error al intentar regresar al inventario: " + e.getMessage());
            e.printStackTrace();
        }
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

    public void setControllerPrincipal(InventarioController principal) {
        this.principal = principal;
    }
    
    @FXML
    private void guardarEnBaseDeDatos() {
        String nombre = txtNombre.getText();
        String precioText = txtPrecio.getText();
        String categoria = cbCategoria.getValue(); 

        if (nombre.isEmpty() || precioText.isEmpty() || categoria == null) {
            System.out.println("Por favor llena todos los campos, incluyendo la categoría");
            return;
        }

        try {
            double precio = Double.parseDouble(precioText);
            
            // 1. Aquí harás tu INSERT a MySQL (cuando tengas tu clase DAO o Database)
            System.out.println("Producto guardado: " + nombre + " [" + categoria + "] - $" + precio);
            
            // 2. LA CLAVE: Avisar a la ventana principal que refresque la tabla
            if (principal != null) {
                principal.actualizarTabla(); // <--- Esto actualiza la interfaz principal
            }
            
            limpiarFormulario();
            
            // OPCIONAL: Cerrar la ventana lila automáticamente al guardar
            // Stage stage = (Stage) txtNombre.getScene().getWindow();
            // stage.close();
            
        } catch (NumberFormatException e) {
            System.out.println("El precio debe ser un número válido");
        }
    }
    

    @FXML
   private TableView<Producto> tablaP;
    
    @FXML
    private void abrirEditarProducto() {

    Producto productoSeleccionado = tablaP.getSelectionModel().getSelectedItem();

    if (productoSeleccionado != null) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarProducto.fxml"));
            Parent root = loader.load();

            EditarProductoController controller = loader.getController();
            controller.setProducto(productoSeleccionado);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Editar Producto");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    } else {
        System.out.println("Selecciona un producto primero");
    }
}
    private void limpiarFormulario() {
        txtNombre.clear();
        txtPrecio.clear();
        cbCategoria.getSelectionModel().clearSelection(); // <--- También limpiamos el combo
        imgPreview.setImage(null);
        archivoImagenSeleccionado = null;
    }  
    
}
