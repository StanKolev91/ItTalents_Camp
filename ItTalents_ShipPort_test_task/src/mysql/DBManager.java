package mysql;

import port_oop.Package;
import port_oop.Port;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class DBManager extends Thread {
    private static final int STAT_INTERVAL_HOURS = 24;
    private static DBManager instance = new DBManager();

    private DBManager() {
    }

    public static DBManager getInstance() {
        return instance;
    }

    public static void insert(Port.Record record) {
        String query = "INSERT INTO port.port_shipments (boat_name,dock_id,crane_id,unloading_time, package_id) VALUES (? ,?, ?, ?, ?)";
        for (Package pack : record.getPackages()) {
            int res = MYSQLConnection.getInstance().makePrepUpdateNotTransaction(query,
                    record.getShipUnloaded().getName(),
                    record.getDock().getId(),
                    record.getUnloadingCrane().getCraneIdId(),
                    java.sql.Date.valueOf(record.getDateTime().toLocalDate()),
                    pack.getId());
            if (res >= 0) System.out.println("Record #" + res + " added in Date base !");
            else System.out.println("Couldn't add to DB. Error!");
        }
    }

    @Override
    public void run() {
        String dropScheme = "DROP SCHEMA IF EXISTS port; ";

        String sql1 = "CREATE SCHEMA port; ";

        String sql2 = "CREATE TABLE port.port_shipments (" +
                "shipment_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "boat_name VARCHAR(30) NOT NULL," +
                "dock_id INT NOT NULL, " +
                "crane_id INT NOT NULL, " +
                "unloading_time TIMESTAMP NOT NULL, " +
                "package_id INT NOT NULL);";

        MYSQLConnection.getInstance().makeTransaction(dropScheme + sql1 + sql2);
        TreeMap<Integer, HashSet<String>> statDataOne = new TreeMap<>();
        TreeSet<String> statDataTwo = new TreeSet<>();
        TreeSet<String> statDataThree = new TreeSet<>();
        TreeSet<String> statDataFour = new TreeSet<>();

        while (!isInterrupted()) {
            try {
                sleep(STAT_INTERVAL_HOURS * 1000);
                System.out.println("\n===========================");
                printStat1(statDataOne);
                System.out.println("\n===========================");
                printStat2(statDataTwo);
                System.out.println("\n===========================");
                printStat3(statDataThree);
                System.out.println("\n===========================");
                printStat4(statDataFour);
                System.out.println("===========================\n");


            } catch (InterruptedException e) {
                System.out.println("DBManager was interrupted!");
            }
        }

    }


    private void printStat1(TreeMap<Integer, HashSet<String>> statDataOne) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT shipment_id, boat_name,dock_id,crane_id,unloading_time, package_id FROM port.port_shipments");
        Statement ps = null;

        try {
            statDataOne.clear();
            while (rs.next()) {
                if (!statDataOne.containsKey(rs.getInt("dock_id"))) {
                    statDataOne.put(rs.getInt("dock_id"), new HashSet<>());
                }
                statDataOne.get(rs.getInt("dock_id")).add(
                        "пратка " + rs.getString("package_id") +
                                ", кораб " + rs.getString("boat_name") +
                                ", кран " + rs.getString("crane_id") + ", " +
                                "дата " + rs.getDate("unloading_time"));
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (!statDataOne.isEmpty()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm_dd:MM:yyyy"));
            String filePath = "C:\\Users\\SPITE\\Downloads\\report_1.txt";
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }

            try (PrintStream printStreams = new PrintStream(file)) {

                for (Map.Entry<Integer, HashSet<String>> entry : statDataOne.entrySet()) {
                    System.out.println("Dock " + entry.getKey());
                    printStreams.append("Dock ").append(entry.getKey().toString()).append("\n");

                    for (String s : entry.getValue()) {
                        System.out.println("\t" + s);
                        printStreams.append("\t").append(s).append("\n");
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void printStat2(TreeSet<String> statDataTwo) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT dock_id, COUNT(boat_name) AS boats FROM port.port_shipments GROUP BY dock_id");
        Statement ps = null;
        statDataTwo.clear();
        try {
            while (rs.next()) {
                statDataTwo.add("Док " + rs.getString("dock_id") + ": " + rs.getString("boats") + "кораба");
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (!statDataTwo.isEmpty()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm_dd:MM:yyyy"));
            String filePath = "C:\\Users\\SPITE\\Downloads\\report_2.txt";
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
            printInFile(file,statDataTwo);
        }
    }

    private void printStat3(TreeSet<String> statDataThree) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT crane_id, COUNT(package_id) AS packages FROM port.port_shipments GROUP BY crane_id");
        Statement ps = null;
        statDataThree.clear();
        try {
            while (rs.next()) {
                statDataThree.add("Кран " + rs.getString("crane_id") + ": " + rs.getString("packages") + "пратки");
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (!statDataThree.isEmpty()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm_dd:MM:yyyy"));
            String filePath = "C:\\Users\\SPITE\\Downloads\\report_3.txt";
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
            printInFile(file,statDataThree);
        }
    }

    private void printStat4(TreeSet<String> statDataFour) {
        ResultSet rs = MYSQLConnection.getInstance().makeQuery("SELECT boat_name, count(package_id) AS packages FROM port.port_shipments GROUP BY boat_name HAVING COUNT(package_id) = (SELECT COUNT(package_id) FROM port.port_shipments GROUP BY boat_name  ORDER BY COUNT(package_id) DESC LIMIT 1)");
        Statement ps = null;
        statDataFour.clear();
        try {
            System.out.println("Корабът разтоварил най - много пратки: ");
            while (rs.next()) {
                statDataFour.add("\t"+rs.getString("boat_name") +": "+ rs.getString("packages") + " пратки");
            }
            ps = rs.getStatement();
        } catch (SQLException e) {
            System.out.println("Ooops " + e.getMessage());
        } finally {
            MYSQLConnection.closeQuery(ps, rs);
        }

        if (!statDataFour.isEmpty()) {
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm_dd:MM:yyyy"));
            String filePath = "C:\\Users\\SPITE\\Downloads\\report_4.txt";
            File file = new File(filePath);
            if(file.exists()) {
                file.delete();
            }
            printInFile(file,statDataFour);
        }
    }

    private void printInFile(File file, TreeSet<String> statSet){
        try (PrintStream printStreams = new PrintStream(file)) {
            for (String s : statSet) {
                System.out.println(s);
                printStreams.print(s+"\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
