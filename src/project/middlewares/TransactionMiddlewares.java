package project.middlewares;

import project.database.DBHandler;
import project.models.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class TransactionMiddlewares {
    private static PreparedStatement pst;
    private static Connection connection;
    private static final DBHandler dbHandler = new DBHandler();

    public static void addTransaction(String type, String category, double amount, String reason, int month, int year, int user_id) {
        String put = "INSERT INTO transactions(`type`, `category`,`amount`, `reason`, `month`, `year`, `accounts_for`) " +
                "VALUES (?,?,?,?,?,?,?)";
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(put);

            pst.setString(1, type);
            pst.setString(2, category);
            pst.setDouble(3, amount);
            pst.setString(4, reason);
            pst.setInt(5, month);
            pst.setInt(6, year);
            pst.setInt(7, user_id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addMultipleTransactions(String type, String category, double amount, String reason, int month, int year, int user_id, ArrayList<Integer> ids) {
        StringBuilder putBuilder = new StringBuilder();
        putBuilder.append("INSERT INTO transactions(`type`, `category`,`amount`, `reason`, `month`, `year`, `accounts_for`) VALUES ");

        for(int i = 0; i < ids.size(); ++i) {
            if(i == ids.size()-1) {
                putBuilder.append("('").append(type).append("','").append(category).append("',").append(amount).append(",'").append(reason).append(MemberMiddleware.getMemberDetailsByMemberId(ids.get(i)).getName()).append(", ").append(MemberMiddleware.getMemberDetailsByMemberId(ids.get(i)).getUIUid()).append("',").append(month).append(",").append(year).append(",").append(user_id).append(")");
            } else {
                putBuilder.append("('").append(type).append("','").append(category).append("',").append(amount).append(",'").append(reason).append(MemberMiddleware.getMemberDetailsByMemberId(ids.get(i)).getName()).append(", ").append(MemberMiddleware.getMemberDetailsByMemberId(ids.get(i)).getUIUid()).append("',").append(month).append(",").append(year).append(",").append(user_id).append("), ");
            }
        }

        String put = putBuilder.toString();

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(put);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Transaction> getTransactionsByFiltering(int user_id, String type, int startMonth, int startYear, int endMonth, int endYear) {
        String get = "SELECT * FROM transactions WHERE `accounts_for`=? AND (type=? AND ((month>=? AND month<=?) AND (year>=? AND year <=?)))";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, user_id);
            pst.setString(2, type);
            pst.setInt(3, startMonth);
            pst.setInt(4, endMonth);
            pst.setInt(5, startYear);
            pst.setInt(6, endYear);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("reason"),
                        Integer.toString(rs.getInt("month")),
                        Integer.toString(rs.getInt("year"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }


    public static ArrayList<Transaction> getTransactionsByCategory(int user_id, String category, int startMonth, int startYear, int endMonth, int endYear) {
        String get = "SELECT * FROM transactions WHERE `accounts_for`=? AND (category=? AND ((month>=? AND month<=?) AND (year>=? AND year <=?)))";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, user_id);
            pst.setString(2, category);
            pst.setInt(3, startMonth);
            pst.setInt(4, endMonth);
            pst.setInt(5, startYear);
            pst.setInt(6, endYear);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("reason"),
                        Integer.toString(rs.getInt("month")),
                        Integer.toString(rs.getInt("year"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public static ArrayList<Transaction> getAllTransactions(int user_id, int startMonth, int startYear, int endMonth, int endYear) {
        String get = "SELECT * FROM transactions WHERE `accounts_for`=? AND ((month>=? AND month<=?) AND (year>=? AND year <=?))";
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, user_id);
            pst.setInt(2, startMonth);
            pst.setInt(3, endMonth);
            pst.setInt(4, startYear);
            pst.setInt(5, endYear);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                transactions.add(new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getString("category"),
                        rs.getDouble("amount"),
                        rs.getString("reason"),
                        Integer.toString(rs.getInt("month")),
                        Integer.toString(rs.getInt("year"))
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }
}
