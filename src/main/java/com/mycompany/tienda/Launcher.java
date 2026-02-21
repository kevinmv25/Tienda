/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tienda;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import lib.SqlLib;

/**
 *
* @author rojas
 */
public class Launcher {
    private static SqlLib db;
    
    public static void main(String[] args) {
        String[] credentials = getDBCredentials(); // Obtener credenciales de la base de datos
        if (credentials == null) {
            System.out.println("Error: No se pudieron cargar las credenciales de la base de datos.");
            return;
        }

        try {
            // Inicializar la conexión con la base de datos
            db = SqlLib.getInstance(credentials[0], credentials[1], credentials[2]);
        } catch (SQLException e) {
            System.out.println("Url: " + credentials[0] + "\nUsuario: " + credentials[1]);
            System.out.println("Error: No se pudo conectar a la base de datos.");
            e.printStackTrace();
            return;
        }
        // Esto llama al main de tu clase Tienda sin que 
        // Java crea que es una aplicación JavaFX desde el inicio.
        Tienda.main(args);
    }
    
     private static String[] getDBCredentials() {
        Properties properties = new Properties();
        String[] credentials = new String[3];

        try (FileInputStream fis = new FileInputStream("src/main/java/var/credentials.properties")) {
            properties.load(fis);

            // Obtener los valores de las propiedades
            credentials[0] = properties.getProperty("db.url");
            credentials[1] = properties.getProperty("db.user");
            credentials[2] = properties.getProperty("db.password");

        } catch (IOException e) {
            System.out.println("Error: No se pudo leer el archivo de credenciales.");
            e.printStackTrace();
            return null;
        }
        return credentials;
    }
}
