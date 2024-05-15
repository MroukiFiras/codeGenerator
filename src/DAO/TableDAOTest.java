package DAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import Models.Column;
import Models.Table;
import utilitaires.Utilitaire;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TableDAOTest {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            System.out.println("Welcome to your database!");

            // Establish database connection using Utilitaire
            String propertiesFilePath = "C:\\Users\\firas\\Desktop\\atelierProg\\Semestre2\\ProjectJava\\codeGenerator\\connection.properties"; 
            Utilitaire.seConnecter(propertiesFilePath);
            connection = Utilitaire.getConnection();

            // Read the database name from the properties file
            String databaseName = readDatabaseName(propertiesFilePath);

            // Create TableDAO instance
            TableDAO tableDAO = new TableDAO();
            List<Table> tables = tableDAO.getAllTables();

            // Print table names and their columns
            for (Table table : tables) {
                System.out.println("Table Name: " + table.getTableName());

                // Retrieve columns for the specified database and table
                List<Column> columns = tableDAO.getColumnsForTable(databaseName, table.getTableName());
                for (Column column : columns) {
                    System.out.println("    Column Name: " + column.getColumnName() + ", Column Type: " + column.getColumnType());
                }
            }

        } finally {
            // Disconnect from the database
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            Utilitaire.seDeconnecter();
        }
    }

    private static String readDatabaseName(String propertiesFilePath) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(propertiesFilePath)) {
            properties.load(input);
            return properties.getProperty("database");
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception appropriately (e.g., fallback to a default database name)
            return null;
        }
    }
}
