package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Models.*;
import utilitaires.Utilitaire;

public class TableDAO {
    private Connection connection;

    public TableDAO() {
        this.connection = Utilitaire.getConnection();
    }

    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        String query = "SELECT table_name FROM information_schema.tables WHERE table_schema = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, connection.getCatalog());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String tableName = resultSet.getString("table_name");
                    tables.add(new Table(tableName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tables;
    }

    public List<Column> getColumnsForTable(String databaseName, String tableName) {
        List<Column> columns = new ArrayList<>();
        // Modify the SQL query to select columns for the specified database and table
        String query = "SELECT column_name, data_type FROM information_schema.columns " +
                       "WHERE table_schema = ? AND table_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, databaseName);
            statement.setString(2, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String columnName = resultSet.getString("column_name");
                    String columnType = resultSet.getString("data_type");
                    columns.add(new Column(columnName, columnType));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    public PrimaryKey getPrimaryKeyForTable( String tableName) {
        PrimaryKey primaryKey = null;
        String query = "SELECT k.column_name " +
                       "FROM information_schema.table_constraints t " +
                       "JOIN information_schema.key_column_usage k " +
                       "USING (constraint_name, table_schema, table_name) " +
                       "WHERE t.constraint_type = 'PRIMARY KEY' AND t.table_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    primaryKey = new PrimaryKey(resultSet.getString("column_name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return primaryKey;
    }

    public List<ForeignKey> getForeignKeysForTable(String databaseName, String tableName) {
        List<ForeignKey> foreignKeys = new ArrayList<>();
        String query = "SELECT k.column_name, k.referenced_table_name, k.referenced_column_name " +
                       "FROM information_schema.key_column_usage k " +
                       "WHERE k.table_name = ? AND k.referenced_table_name IS NOT NULL";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, tableName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String columnName = resultSet.getString("column_name");
                    String referencedTableName = resultSet.getString("referenced_table_name");
                    String referencedColumnName = resultSet.getString("referenced_column_name");
                    foreignKeys.add(new ForeignKey(columnName, referencedTableName, referencedColumnName));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foreignKeys;
    }
}
