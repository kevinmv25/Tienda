/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.controllers;

import com.mycompany.objects.Producto;
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
import lib.SqlLib;
import java.time.LocalDate;
import javafx.scene.control.DatePicker;

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
    @FXML private DatePicker dpCaducidad;

    private InventarioController principal;
    private File archivoImagenSeleccionado;
    private CatalogoTiendaController CatalogoPrincipal;

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
    
    public void setControllerPrincipal(InventarioController principal) {
        this.principal = principal;
    }
    
    public void setCatalogoPrincipal(CatalogoTiendaController catalogo) {
        this.CatalogoPrincipal = catalogo;
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

        // 1. Detectar la ruta del proyecto y apuntar a src/main/resources/productos
        String rutaBase = System.getProperty("user.dir");
        File directorioInicial = new File(rutaBase + File.separator + "src" 
                                        + File.separator + "main" 
                                        + File.separator + "resources" 
                                        + File.separator + "productos");

        // 2. Si la carpeta existe físicamente, abrir ahí
        if (directorioInicial.exists()) {
            fileChooser.setInitialDirectory(directorioInicial);
        }

        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) txtNombre.getScene().getWindow();
        archivoImagenSeleccionado = fileChooser.showOpenDialog(stage);

        if (archivoImagenSeleccionado != null) {
            // Mostramos la vista previa de la imagen seleccionada
            Image imagen = new Image(archivoImagenSeleccionado.toURI().toString());
            imgPreview.setImage(imagen);
        }
    }
    
    @FXML
    private void guardarEnBaseDeDatos() {
        String nombre = txtNombre.getText();
        String precioText = txtPrecio.getText();
        String categoria = cbCategoria.getValue();
        LocalDate fechaSeleccionada = dpCaducidad.getValue();

        if (nombre.isEmpty() || precioText.isEmpty() || categoria == null || fechaSeleccionada == null) {
            System.out.println("Por favor llena todos los campos, incluyendo la fecha de caducidad");
            return;
        }

        try {
            double precio = Double.parseDouble(precioText);
            String caducidad = fechaSeleccionada.toString();

            // LOGICA DE IMAGEN: Si no seleccionó una, usamos una por defecto
            String nombreImagen = (archivoImagenSeleccionado != null) 
                                 ? archivoImagenSeleccionado.getName() 
                                 : "default.png";

            SqlLib db = SqlLib.getInstance(
                "jdbc:mysql://localhost:3306/tienda", 
                "root",
                "Bebe2508_"
            );
            
            boolean guardado = db.agregarProducto(nombre, precio, categoria, caducidad);

            // OPCIÓN B: Si agregaste una columna nueva para la fecha, tendrías que pasar 5 parámetros
            // boolean guardado = db.agregarProducto(nombre, precio, categoria, caducidad, nombreImagen);

            if (guardado) {
                System.out.println("Producto guardado: " + nombre + " | Imagen: " + nombreImagen);
                if (principal != null) {
                    principal.actualizarTabla();
                }
                limpiarFormulario();
            } else {
                System.out.println("Error al guardar en MySQL");
            }

        } catch (NumberFormatException e) {
            System.out.println("El precio debe ser un número válido");
        } catch (Exception e) {
            e.printStackTrace();
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
        cbCategoria.getSelectionModel().clearSelection();
        dpCaducidad.setValue(null);
        imgPreview.setImage(null);
        archivoImagenSeleccionado = null;
    }
}
