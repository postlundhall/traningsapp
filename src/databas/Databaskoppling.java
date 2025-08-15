package databas;

import register.Rad;
import register.Rader;

import java.sql.*;

public class Databaskoppling {

    static void stangDatabasKoppling(Connection dbConnection, Statement statement, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Ett fel har uppstått: " + ex.toString());
        }
    }

    public static Rader runSelectQuery(String tabell) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet rs = null;
        Rader rader = new Rader();
        // Kontrollera JDBC driver
        try {
            Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();

            // Försök logga in till db
            String url = "insert db url";
            String user = "insert user";
            Password mypw = new Password();
            String password = mypw.getPassword();

            try {
                dbConnection = DriverManager.getConnection(url, user, password);

                // Kör SQL fråga
                String strSql = "SELECT * FROM " + tabell;
                assert dbConnection != null;
                statement = dbConnection.createStatement();
                rs = statement.executeQuery(strSql);

                // samla tabellens kolumnbenämningar i en array.
                ResultSetMetaData tabellMetaData = rs.getMetaData();
                int antalKolumner = tabellMetaData.getColumnCount();
                String[] columnLabels = new String[antalKolumner];
                for (int i = 1; i <= antalKolumner; i++) {
                    columnLabels[i-1] = tabellMetaData.getColumnLabel(i);
                }
                StringBuilder kolumnrad = new StringBuilder();
                for (String columnLabel: columnLabels) {
                    kolumnrad.append(String.format("%-25s", columnLabel));
                }

                // Skriv ut tabellen och dess innehåll i konsol.
                if (!rs.next()) {
                    System.out.println("Inget innehåll finns registrerat i " + tabell +". Lägg först till rader. ");
                } else {
                    System.out.println(kolumnrad);
                    System.out.println("-------------------------------------------------------------------------------------------------------------");

                    StringBuilder forstaRadStrang = new StringBuilder();
                    Rad forstaRad = new Rad();
                    for (String columnLabel : columnLabels) {
                        String varde = rs.getString(columnLabel);
                        forstaRadStrang.append(String.format("%-25s", varde));
                        forstaRad.addAttribut(varde);
                    }
                    System.out.println(forstaRadStrang);

                    while (rs.next()) {
                        StringBuilder rad = new StringBuilder();
                        Rad nyRad = new Rad();
                        for (String columnLabel : columnLabels) {
                            String varde = rs.getString(columnLabel);
                            rad.append(String.format("%-25s", varde));
                            nyRad.addAttribut(varde);
                        }
                        System.out.println(rad);
                        rader.addRad(nyRad);
                    }
                }
                } catch (SQLException ex) {
                System.err.println("Visning misslyckades: " + ex.toString());
            }
        } catch (Exception e) {
            System.err.println("JDBC-drivrutiner kunde INTE hittas.");
        }
        stangDatabasKoppling(dbConnection, statement, rs);
        return rader;
    }

    public static void runInsertQuery(String tabell, String kolumner, String varden) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet rs = null;

        // Kontrollera JDBC driver
        try {
            Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();

            // Försök logga in till db
            String url = "insert db url";
            String user = "insert user";
            Password mypw = new Password();
            String password = mypw.getPassword();
            try {
                dbConnection = DriverManager.getConnection(url, user, password);

                // Kör SQL fråga
                String strSql = String.format("INSERT INTO %s (%s)VALUES (%s)", tabell, kolumner, varden);
                System.out.println(strSql);

                assert dbConnection != null;
                statement = dbConnection.createStatement();
                rs = statement.executeQuery(strSql);
                System.out.println(String.format("Lade till %s med följande värden: %s.\n", tabell, varden));
            } catch (SQLException ex) {
                System.err.println("Tillägg misslyckades: " + ex.toString());
            }

        } catch (Exception e) {
            System.err.println("JDBC-drivrutiner kunde INTE hittas.");
        }
        stangDatabasKoppling(dbConnection, statement, rs);
    }
    public static void runUpdateQuery(String tabell, String kolumn, String primaryKeyKolumn, String primaryKey, String nyttVarde) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet rs = null;

        // Kontrollera JDBC driver
        try {
            Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();

            // Försök logga in till db
            String url = "insert db url";
            String user = "insert user";
            Password mypw = new Password();
            String password = mypw.getPassword();
            try {
                dbConnection = DriverManager.getConnection(url, user, password);

                // Kör SQL fråga
                String strSql = String.format("UPDATE %s SET %s = %s WHERE %s = %s",
                        tabell, kolumn, nyttVarde, primaryKeyKolumn, primaryKey);
                assert dbConnection != null;
                statement = dbConnection.createStatement();
                rs = statement.executeQuery(strSql);
                System.out.println(String.format("Uppdaterade %s till %s för %s %s.\n", kolumn, nyttVarde, tabell, primaryKey));

            } catch (SQLException ex) {
                System.err.println("Uppdatering misslyckades: " + ex.toString());
            }
        } catch (Exception e) {
            System.err.println("JDBC-drivrutiner kunde INTE hittas.");
        }
        stangDatabasKoppling(dbConnection, statement, rs);
    }

    public static void runDeleteQuery(String tabell, String primaryKeyKolumn, String primaryKey) {
        Connection dbConnection = null;
        Statement statement = null;
        ResultSet rs = null;

        // Kontrollera JDBC driver
        try {
            Class.forName("org.mariadb.jdbc.Driver").getDeclaredConstructor().newInstance();

            // Försök logga in till db
            String url = "insert db url";
            String user = "insert user";
            Password mypw = new Password();
            String password = mypw.getPassword();
            try {
                dbConnection = DriverManager.getConnection(url, user, password);

                // Kör SQL fråga
                String strSql = String.format("DELETE FROM %s WHERE %s = %s", tabell, primaryKeyKolumn, primaryKey);
                System.out.println(strSql);
                assert dbConnection != null;
                statement = dbConnection.createStatement();
                rs = statement.executeQuery(strSql);
                System.out.println(String.format("Raderade %s med %s %s.\n", tabell, primaryKeyKolumn, primaryKey));

            } catch (SQLException ex) {
                System.err.println("Radering misslyckades: " + ex.toString());
            }

        } catch (Exception e) {
            System.err.println("JDBC-drivrutiner kunde INTE hittas.");
        }
        stangDatabasKoppling(dbConnection, statement, rs);
    }
}