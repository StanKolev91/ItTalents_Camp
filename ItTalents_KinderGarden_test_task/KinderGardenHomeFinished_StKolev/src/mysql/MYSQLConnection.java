package Test3_StKolev.mysql;

import java.sql.*;
import java.time.LocalDate;

public class MYSQLConnection {
    private static final String DB_URL = "jdbc:mysql://";
    private static final String DB_IP = "127.0.0.1";
    private static final String DB_PORT = "3306";
    private static final String DB_SCHEMA = "hr?allowMultiQueries=true&useUnicode=yes&characterEncoding=UTF-8";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    private static MYSQLConnection ourInstance = new MYSQLConnection();
    private Connection connection;

    private MYSQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver found!");
            this.connection = DriverManager.getConnection(DB_URL + DB_IP + ":" + DB_PORT + "/" + DB_SCHEMA, DB_USERNAME, DB_PASSWORD);
            System.out.println("Connection made successfully!");

        } catch (ClassNotFoundException e) {
            System.out.println("Cannot find connection driver!");
        } catch (SQLException e) {
            System.out.println("Oops - " + e.getMessage());
        }
    }

    public static MYSQLConnection getInstance() {
        return ourInstance;
    }

    public Connection getConnection() {
        return connection;
    }

     ResultSet makeQuery(String query) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection con;

        try {
            con = MYSQLConnection.getInstance().getConnection();
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            System.out.println("QuerySQL error: " + e.getMessage());
            closeQuery(ps, rs);
        }
        return null;
    }

    public int makeUpdate(String query) {
        PreparedStatement ps = null;
        int rs = -1;
        Connection con = null;
        try {
            con = MYSQLConnection.getInstance().getConnection();

            ps = con.prepareStatement(query);
            rs = ps.executeUpdate();
            ResultSet r = ps.getGeneratedKeys();
            if (r.next()) {
                return r.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("UpdateSQL error: " + e.getMessage());
        } finally {
            closeUpdate(ps);
        }
        return -1;
    }

     int makePrepUpdateNotTransaction(String query,String figurine, String drawerName, int drawTime, String painterName, String painterGroup,int paintTime, String drawerGroup, LocalDate date) {
        PreparedStatement ps = null;
        int rs = -1;
        Connection con = null;
        try {
            con = MYSQLConnection.getInstance().getConnection();
            ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,figurine);
            ps.setString(2, drawerName);
            ps.setInt(3, drawTime);
            ps.setString(4, painterName);
            ps.setString(5, painterGroup);
            ps.setInt(6,paintTime);
            ps.setString(7, drawerGroup);
            ps.setDate(8, Date.valueOf(date));
            rs = ps.executeUpdate();
            ResultSet r = ps.getGeneratedKeys();
            if (r.next()) {
                return r.getInt(1);
            }

            return rs;
        } catch (SQLException e) {
            System.out.println("UpdateSQL error: " + e.getMessage());
        } finally {
            closeUpdate(ps);
        }
        return -1;
    }

    int makePrepUpdateNotTransaction2(String query,String figurine, String drawerName, String drawerGroup, int drawTime, String painterName, String painterGroup,int paintTime, int id) {
        PreparedStatement ps = null;
        int rs = -1;
        Connection con = null;
        try {
            con = MYSQLConnection.getInstance().getConnection();
            ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1,figurine);
            ps.setString(2, drawerName);
            ps.setString(3, drawerGroup);
            ps.setInt(4, drawTime);
            ps.setString(5, painterName);
            ps.setString(6, painterGroup);
            ps.setInt(7,paintTime);

            ps.setInt(8,id);
            rs = ps.executeUpdate();
            ResultSet r = ps.getGeneratedKeys();
            if (r.next()) {
                return r.getInt(1);
            }

            return rs;
        } catch (SQLException e) {
            System.out.println("UpdateSQL error: " + e.getMessage());
        } finally {
            closeUpdate(ps);
        }
        return -1;
    }

    public int makePrepUpdateTransaction(String query, String boatName, int craneId, Date date, int packageId) {
        PreparedStatement ps = null;
        int rs = -1;
        Connection con = null;
        try {
            con = MYSQLConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, boatName);
            ps.setInt(2, craneId);
            ps.setInt(3, craneId);
            ps.setDate(4, date);
            ps.setInt(5, packageId);
            ps.executeUpdate();
            ResultSet result = ps.getGeneratedKeys();
            result.next();
            return result.getInt(1);
        } catch (SQLException e) {
            rollbackTransaction();
            System.out.println("UpdateSQL error: " + e.getMessage());
        } finally {
            closeTransaction(ps);
        }
        return -1;
    }

     int makeTransaction(String query) {
        PreparedStatement ps = null;
        int rs = -1;
        Connection con = null;
        try {
            con = MYSQLConnection.getInstance().getConnection();
            con.setAutoCommit(false);
            ps = con.prepareStatement(query);
            rs = ps.executeUpdate();
            con.commit();
            return rs;
        } catch (SQLException e) {
            System.out.println("UpdateSQL error: " + e.getMessage());
            rollbackTransaction();
        } finally {
            closeTransaction(ps);
        }
        return -1;
    }

     static void closeQuery(Statement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException ex) {
            System.out.println("Error while closing instance: " + ex.getMessage());
        }
    }

     static void closeTransaction(Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
            MYSQLConnection.getInstance().getConnection().setAutoCommit(true);
        } catch (SQLException e) {

            System.out.println("Error while closing instance: " + e.getMessage());
        }
    }

     static void rollbackTransaction() {
        try {
            MYSQLConnection.getInstance().getConnection().rollback();
        } catch (SQLException e1) {
            System.out.println("Error trying to rollback a transaction!" + e1.getMessage());
        }

    }

     static void closeUpdate(Statement ps) {
        try {
            if (ps != null) {
                ps.close();
            }

        } catch (SQLException e) {

            System.out.println("Error while closing instance: " + e.getMessage());
        }
    }
}
