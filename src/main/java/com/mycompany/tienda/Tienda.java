/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.tienda;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author rojas
 */
public class Tienda extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Esta es la ruta que configuramos en la carpeta de tu proyecto
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/scenes/CatalogoTienda.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        stage.setTitle("Catálogo de Mi Tienda");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Esto apaga el "Hello World" y enciende JavaFX
        launch(args);
    }
}
