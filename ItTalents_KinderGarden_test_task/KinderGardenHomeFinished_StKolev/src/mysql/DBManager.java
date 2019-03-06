package Test3_StKolev.mysql;

import Test3_StKolev.KinderGarden;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class DBManager extends Thread {
    private static final int STAT_INTERVAL_HOURS = 1;
    private static DBManager instance = new DBManager();

    private DBManager() {
    }

    public static DBManager getInstance() {
        return instance;
    }

    public static int insert(KinderGarden.Record record) {
        String query = "INSERT INTO garden.drawings (figurine, drawer_name,draw_time,painter_name,painter_group,paint_time,drawer_group, date) VALUES (? ,?,?, ?, ?,?, ?,?)";
        int res = MYSQLConnection.getInstance().makePrepUpdateNotTransaction(query,
                record.getFigurine(),
                record.getDrawBy(),
                record.getDrawTime(),
                record.getPaintedBy(),
                record.getPainterGroup(),
                record.getPaintTime(),
                record.getDrawerGroup(),
                record.getDrawDate()
        );
        if (res >= 0) System.out.println("Record #" + res + " added in Date base !");
        else System.out.println("Couldn't add to DB. Error!");
        return res;
    }

    public static void update(int id, KinderGarden.Record record) {
        String query = "UPDATE garden.drawings SET figurine = ?, drawer_name = ?, drawer_group = ?,draw_time = ?, painter_name = ?, painter_group = ?,paint_time = ? WHERE id = ?";
        MYSQLConnection.getInstance().makePrepUpdateNotTransaction2(query,
                record.getFigurine(),
                record.getDrawBy(),
                record.getDrawerGroup(),
                record.getDrawTime(),
                record.getPaintedBy(),
                record.getPainterGroup(),
                record.getPaintTime(),
                id
        );
    }

    @Override
    public void run() {
        String dropScheme = "DROP SCHEMA IF EXISTS garden; ";

        String sql1 = "CREATE SCHEMA garden; ";

        String sql2 = "CREATE TABLE garden.drawings (" +
                "id INT PRIMARY KEY AUTO_INCREMENT, " +
                "figurine VARCHAR(50) ," +
                "drawer_name VARCHAR(50) , " +
                "draw_time INT , " +
                "painter_name VARCHAR(50), " +
                "painter_group VARCHAR(50), " +
                "paint_time INT,  " +
                "drawer_group VARCHAR(50) , " +
                "date DATE);";
        MYSQLConnection.getInstance().makeTransaction(dropScheme + sql1 + sql2);
    }

    public void print() {
        TreeMap<Integer, HashSet<String>> statDataThree = new TreeMap<>((i1, i2) -> {
            return i1.compareTo(i2) * -1;
        });
        TreeMap<Integer, HashSet<String>> statDataFour = new TreeMap<>((i1, i2) -> {
            return i1.compareTo(i2) * -1;
        });
        TreeMap<Integer, HashSet<String>> statDataFive = new TreeMap<>((i1, i2) -> {
            return i1.compareTo(i2) * -1;
        });

        try {
            sleep(STAT_INTERVAL_HOURS * 1000);
            System.out.println("\n===========================");
            printStat1();
            System.out.println("\n===========================");
            printStat2();
            System.out.println("\n===========================");
            printStat3(statDataThree);
            System.out.println("\n===========================");
            printStat4(statDataFour);
            System.out.println("===========================\n");
            printStat5(statDataFive);
            System.out.println("\n===========================");

        } catch (InterruptedException e) {
            System.out.println("DBManager was interrupted!");
        }

    }


    private void printStat1() {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT COUNT(*) FROM garden.drawings");
        Statement ps = null;
        int result = -1;
        try {
            while (rs.next()) {
                result = rs.getInt(1);
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (result >= 0) {
            System.out.println("Нарисувани рисунки: " + result);
        }
    }

    private void printStat2() {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT COUNT(*) FROM garden.drawings WHERE painter_name IS NOT NULL");
        Statement ps = null;
        int result = -1;
        try {
            while (rs.next()) {
                result = rs.getInt(1);
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (result >= 0) {
            System.out.println("Оцветени рисунки: ");
            System.out.println( result);
        }
    }

    private void printStat3(TreeMap<Integer, HashSet<String>> statDataThree) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT g.drawer_group, COUNT(*) AS drawings FROM garden.drawings AS g GROUP BY drawer_group");
        Statement ps = null;
        statDataThree.clear();
        try {
            if (rs != null) {
                while (rs.next()) {
                    if (!statDataThree.containsKey(rs.getInt("drawings"))) {
                        statDataThree.put(rs.getInt("drawings"), new HashSet<>());
                    }
                    statDataThree.get(rs.getInt("drawings")).add(rs.getString("g.drawer_group"));
                }
                ps = rs.getStatement();
            }
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }
        System.out.println("Група с най _ много картинки:");
        if (!statDataThree.isEmpty()) {
            int key = statDataThree.keySet().iterator().next();
            for (String s : statDataThree.get(key)) {
                System.out.println(s + ": " + key + " картинки");
            }
        }
    }

    private void printStat4(TreeMap<Integer, HashSet<String>> statDataFour) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT g.painter_name, COUNT(*) AS drawings FROM garden.drawings AS g WHERE g.painter_name IS NOT NULL GROUP BY g.painter_name");
        Statement ps = null;
        statDataFour.clear();
        try {
            while (rs.next()) {
                if (!statDataFour.containsKey(rs.getInt("drawings"))) {
                    statDataFour.put(rs.getInt("drawings"), new HashSet<>());
                }
                statDataFour.get(rs.getInt("drawings")).add(rs.getString("g.painter_name"));
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }
        System.out.println("Дете с най - ммного изрисувани картинки:");
        if (!statDataFour.isEmpty()) {
            int key = statDataFour.keySet().iterator().next();
            for (String s : statDataFour.get(key)) {
                System.out.println(s + ": " + key + " картинки");
            }
        }

    }

    private void printStat5(TreeMap<Integer, HashSet<String>> statDataFive) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT g.drawer_group, COUNT(*)/(SELECT COUNT(*) FROM garden.drawings AS d) * 100   AS percent FROM garden.drawings AS g GROUP BY g.drawer_group");
        Statement ps = null;
        statDataFive.clear();
        if (rs != null) {
            try {
                while (rs.next()) {
                    if (!statDataFive.containsKey(rs.getInt("percent"))) {
                        statDataFive.put(rs.getInt("percent"), new HashSet<>());
                    }
                    statDataFive.get(rs.getInt("percent")).add(rs.getString("g.drawer_group"));
                }
                ps = rs.getStatement();
            } catch (SQLException e) {
                System.out.println("Ooops " + e.getMessage());
            } finally {
                MYSQLConnection.closeQuery(ps, rs);
            }
        }
        System.out.println("Отношение на рисунките на всяка група към общия брой:");
        if (!statDataFive.isEmpty()) {
            for (Map.Entry<Integer, HashSet<String>> entry : statDataFive.entrySet()) {
                int key = entry.getKey();
                for (String s : statDataFive.get(key)) {
                    System.out.println(s + ": " + key + "%");
                }
            }
        }

    }
}
