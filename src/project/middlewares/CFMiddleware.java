package project.middlewares;


import project.database.DBHandler;
import project.enums.LoginResult;
import project.models.ClubForum;
import project.models.Student;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class CFMiddleware {

    private static PreparedStatement pst;
    private static final DBHandler dbHandler = new DBHandler();

    public static LoginResult loginCF(String username, String password) throws SQLException {

        try (Connection connection = dbHandler.getConnection()) {
            String query = "SELECT * FROM user WHERE username=? and password=?";

            pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();

            int count = 0;

            while (rs.next()) {
                count += 1;
            }

            if (count == 1) {
                return LoginResult.SUCCESS;
            } else {
                return LoginResult.FAILED_BY_CREDENTIALS;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return LoginResult.FAILED_BY_UNEXPECTED_ERROR;
        }
    }

    public static ArrayList<String> getAllCFNamesWhoAreRecruitingAndMemberNotInIt(Student student) {
        ArrayList<String> names = new ArrayList<>();
        String select = "SELECT * FROM user WHERE recruitment=?";

        Set<Integer> cfIds = new HashSet<>();
        String getCfs = "SELECT * FROM members WHERE (member=?)";

        DBHandler dbHandler = new DBHandler();
        Connection connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(getCfs);
            pst.setInt(1, student.getMember_id());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                cfIds.add(rs.getInt("cf_ref"));
            }


            pst = connection.prepareStatement(select);
            pst.setString(1,"true");
            rs = pst.executeQuery();

            while (rs.next()) {
                if(!cfIds.contains(rs.getInt("id"))) {
                    names.add(rs.getString("name"));
                }
            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return names;
    }



    public static ClubForum startOrStopRecruitment(ClubForum clubForum) {
        String toggle = "UPDATE user SET recruitment=? WHERE id=?";

        try (Connection connection = dbHandler.getConnection()) {

            pst = connection.prepareStatement(toggle);

            clubForum.setRecruitmentOn(!clubForum.isRecruitmentOn());

            pst.setString(1,(Boolean.toString(clubForum.isRecruitmentOn())));
            pst.setInt(2,clubForum.getForumID());

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return clubForum;

    }

    public static ClubForum getCF(String username, String password) {
        try (Connection connection = dbHandler.getConnection()) {
            String query = "SELECT * FROM user WHERE username=? and password=?";

            pst = connection.prepareStatement(query);
            pst.setString(1, username);
            pst.setString(2, password);

            ResultSet rs = pst.executeQuery();
            ClubForum clubForum = new ClubForum();

            while (rs.next()) {
                clubForum = new ClubForum(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("settings_pass"),
                        rs.getString("recruitment"),
                        rs.getString("hr_pass"),
                        rs.getString("pr_pass"),
                        rs.getString("rnd_pass"),
                        rs.getString("treasury_pass"),
                        rs.getDouble("accounts"),
                        rs.getDouble("reg_fee")

                );
            }

            System.out.println(clubForum.getName());

            return clubForum;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ClubForum getCFById(int id) {
        ClubForum clubForum = new ClubForum();
        try (Connection connection = dbHandler.getConnection()) {
            String query = "SELECT * FROM user WHERE id=?";

            pst = connection.prepareStatement(query);
            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();


            while (rs.next()) {
                clubForum = new ClubForum(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("settings_pass"),
                        rs.getString("recruitment"),
                        rs.getString("hr_pass"),
                        rs.getString("pr_pass"),
                        rs.getString("rnd_pass"),
                        rs.getString("treasury_pass"),
                        rs.getDouble("accounts"),
                        rs.getDouble("reg_fee")
                );
            }

            System.out.println(clubForum.getName());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clubForum;
    }

    public static ClubForum getCFByName(String name) {
        ClubForum clubForum = new ClubForum();
        try (Connection connection = dbHandler.getConnection()) {
            String query = "SELECT * FROM user WHERE name=?";

            pst = connection.prepareStatement(query);
            pst.setString(1, name);

            ResultSet rs = pst.executeQuery();


            while (rs.next()) {
                clubForum = new ClubForum(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("settings_pass"),
                        rs.getString("recruitment"),
                        rs.getString("hr_pass"),
                        rs.getString("pr_pass"),
                        rs.getString("rnd_pass"),
                        rs.getString("treasury_pass"),
                        rs.getDouble("accounts"),
                        rs.getDouble("reg_fee")
                );
            }

            System.out.println(clubForum.getName());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return clubForum;
    }

    public static boolean ifRnD(int id) {
        String get = "SELECT rnd FROM user WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String s = rs.getString("rnd");
                if(s.equals("T")) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void changeRnd(int id, String condition) {
        String get = "UPDATE user SET rnd=? WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setString(1, condition);
            pst.setInt(2, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void changeTreasury(int id, String condition) {
        String get = "UPDATE user SET treasury=? WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setString(1, condition);
            pst.setInt(2, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static boolean ifTreasury(int id) {
        String get = "SELECT treasury FROM user WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                String s = rs.getString("treasury");
                if(s.equals("T")) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void updateDepartmentPasswords(ArrayList<String> newPassword, ArrayList<String> depts, int id) {
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("UPDATE user SET ");
        for(int i = 0; i < depts.size(); ++i) {
            updateBuilder.append(depts.get(i)).append("=").append(newPassword.get(i));
            if(i==depts.size()-1) {
                updateBuilder.append(" ");
            } else {
                updateBuilder.append(", ");
            }
        }
        updateBuilder.append("WHERE id=").append(id);
        String update = updateBuilder.toString();

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(update);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBalance(double balance, int id) {
        String update = "UPDATE user SET accounts=? WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(update);

            pst.setDouble(1, balance);
            pst.setInt(2, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRegFee(double reg_fee, int id) {
        String update = "UPDATE user SET `reg_fee`=? WHERE id=?";
        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(update);

            pst.setDouble(1, reg_fee);
            pst.setInt(2, id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static double getAccountsReceivable(ClubForum clubForum) {
        String get = "SELECT * FROM members WHERE `cf_ref`=? AND `completed_fee`='F'";
        double sum = 0;

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setInt(1, clubForum.getForumID());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                double getValue = clubForum.getRegistrationFee() - rs.getDouble("fee_paid");
                sum += getValue;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sum;
    }

    public static HashMap<Integer, Double> getPendingRegFees(ClubForum clubForum) {
        String get = "SELECT * FROM members WHERE `cf_ref`=?";
        HashMap<Integer, Double> regFeeMap = new HashMap<>();

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(get);

            pst.setInt(1, clubForum.getForumID());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if(rs.getString("completed_fee").equals("F")) {
                    double getValue = clubForum.getRegistrationFee() - rs.getDouble("fee_paid");
                    regFeeMap.put(rs.getInt("member"), getValue);
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return regFeeMap;

    }

    public static void setRegFeeAsComplete(int enrol_id) {
        String update = "UPDATE members SET `completed_fee`=? WHERE `enrol_id`=?";

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(update);

            pst.setString(1, "T");
            pst.setInt(2, enrol_id);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void updateCompletedFees(ArrayList<Integer> ids, int cf_ref){
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("UPDATE members SET `completed_fee`='T' WHERE `member` IN (");

        for (int i = 0; i < ids.size(); ++i) {
            if(i == ids.size()-1) {
                updateBuilder.append(ids.get(i)).append(")");
            } else {
                updateBuilder.append(ids.get(i)).append(",");
            }
        }

        updateBuilder.append(" AND `cf_ref`=?");
        String update = updateBuilder.toString();
        System.out.println(update);

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(update);
            pst.setInt(1, cf_ref);
            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void uploadImage(String path, int user_id) {
        String upload = "SELECT * FROM images WHERE `poster`=?";

        try (Connection connection = dbHandler.getConnection()) {
            pst = connection.prepareStatement(upload);

            pst.setInt(1, user_id);

            ResultSet rs = pst.executeQuery();

            if (!rs.next()) {
                String sql = "INSERT INTO images(`poster`) VALUES (?)";
                pst = connection.prepareStatement(sql);
                pst.setInt(1, user_id);
                pst.executeUpdate();
            }
            updateImage(path, Integer.toString(user_id));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateImage(String path, String id) {
        String pathToDir = "D:\\Codes\\ECA-Management-System\\src\\project\\database\\profile_pics";
        try (
                FileInputStream fis = new FileInputStream(new File(path));
                FileOutputStream fos = new FileOutputStream(new File(pathToDir+"\\"+id+".jpg"));
                ) {

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for(int n; (n = fis.read(buffer)) != -1; ) {
                byteArrayOutputStream.write(buffer, 0, n);
            }
            byte[] img = byteArrayOutputStream.toByteArray();
            fos.write(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
