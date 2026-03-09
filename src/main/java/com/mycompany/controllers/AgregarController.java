/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
public class AgregarController implements Initializable {
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtPrecio;
    @FXML private ImageView imgPreview;
    @FXML private ComboBox<String> cbCategoria;

    private File archivoImagenSeleccionado;
    private InventarioController principal; // Solo una vez
    private CatalogoTiendaController catalogoPrincipal;

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

    public void setControllerPrincipal(InventarioController principal) {
        this.principal = principal;
    }

    public void setCatalogoPrincipal(CatalogoTiendaController catalogo) {
        this.catalogoPrincipal = catalogo;
    }
    
    @FXML
    private void seleccionarArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen del Producto");

        // Esto obliga a que se abra en la carpeta de imágenes de tu proyecto en NetBeans
        File rutaInicial = new File("src/main/resources/productos");
        if (rutaInicial.exists()) {
            fileChooser.setInitialDirectory(rutaInicial);
        }

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        archivoImagenSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoImagenSeleccionado != null) {
            // Mostramos la vista previa
            Image imagen = new Image(archivoImagenSeleccionado.toURI().toString());
            imgPreview.setImage(imagen);
        }
    }
    
    @FXML
    private void guardarEnBaseDeDatos() {
        String nombre = txtNombre.getText();
        String precioText = txtPrecio.getText();
        String categoria = cbCategoria.getValue(); 

        if (nombre.isEmpty() || precioText.isEmpty() || categoria == null) {
            System.out.println("Por favor llena todos los campos");
            return;
        }

        try {
            double precio = Double.parseDouble(precioText);
            
            // Obtenemos el nombre de la imagen para la BD
            String nombreImagen = (archivoImagenSeleccionado != null) ? archivoImagenSeleccionado.getName() : "default.png";

            // 1. Aquí irá tu SqlLib para el INSERT
            // Ejemplo: sql.insertarProducto(nombre, precio, categoria, nombreImagen);
            System.out.println("Guardando en BD: " + nombre + " Imagen: " + nombreImagen);
            
            // 2. REFLEJO EN INVENTARIO (Tabla)
            if (principal != null) {
                principal.actualizarTabla(); 
            }
            
            // 3. REFLEJO EN CATÁLOGO (Cuadritos) - ¡ESTO FALTABA!
            if (catalogoPrincipal != null) {
                catalogoPrincipal.cargarProductosDinamicos(); 
            }
            
            limpiarFormulario();
            
            // Tip: Es mejor cerrar la ventana para que el usuario vea la actualización
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.close();
            
        } catch (NumberFormatException e) {
            System.out.println("El precio debe ser un número válido");
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
