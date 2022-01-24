package project.middlewares;

import project.database.DBHandler;
import project.models.ClubForum;
import project.models.Member;
import project.models.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MemberMiddleware {
    private static final DBHandler dbHandler = new DBHandler();
    private static PreparedStatement pst;
    private static Connection connection;

    public static void enrollMemberIntoCF(Member member) {
        String insert = "UPDATE members SET selected='true' WHERE `enrol_id`=?";

        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(insert);

            pst.setInt(1, member.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void declineJoinRequestIntoCf(Member member) {
        String decline = "DELETE FROM members WHERE `enrol_id`=?";

        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(decline);

            pst.setInt(1,member.getId());

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public static void sendJoinRequestIntoCF(Student student, ClubForum cfName, String reason) throws Exception{
        String insert = "INSERT INTO members(`member`, `cf_ref`, `reason`) VALUES (?,?,?)";

        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(insert);

            pst.setInt(1,student.getMember_id());
            pst.setInt(2, cfName.getForumID());
            pst.setString(3, reason);

            pst.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception("Something went wrong");
        }

    }

    public static ArrayList<Member> getMemberDetails(int cf_ref) {
        ArrayList<Member> members = new ArrayList<>();

        Member member = new Member();
        String get = "SELECT * FROM members WHERE (`cf_ref`=? AND selected='true')";
        connection = dbHandler.getConnection();
        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Student student = StudentMiddleware.getData(rs.getInt("member"));

                member = new Member();
                member.setDesignation(rs.getString("rank"));
                member.setName(student.getName());
                member.setId(rs.getInt("enrol_id"));
                member.setUIUid(student.getUIU_id());
                member.setDepartment(rs.getString("department"));
                member.setPhone(student.getPhone());
                member.setEmail(student.getEmail());
                member.setStudent_id(rs.getInt("member"));

                members.add(member);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public static Member getMemberDetailsByEnrolId(int enrolID) {

        Member member = new Member();
        String get = "SELECT * FROM members WHERE `enrol_id`=?";
        connection = dbHandler.getConnection();
        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1,enrolID);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Student student = StudentMiddleware.getData(rs.getInt("member"));

                member.setDesignation(rs.getString("rank"));
                member.setName(student.getName());
                member.setId(rs.getInt("enrol_id"));
                member.setUIUid(student.getUIU_id());
                member.setDepartment(rs.getString("department"));
                member.setPhone(student.getPhone());
                member.setEmail(student.getEmail());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    public static Member getMemberDetailsByMemberId(int member_id) {

        Member member = new Member();
        Student student = new Student();
        ClubForum clubForum = new ClubForum();
        String get = "SELECT * FROM members WHERE `member`=?";
        connection = dbHandler.getConnection();
        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, member_id);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                student = StudentMiddleware.getData(member_id);

                member.setDesignation(rs.getString("rank"));
                member.setName(student.getName());
                member.setId(rs.getInt("enrol_id"));
                member.setUIUid(student.getUIU_id());
                member.setDepartment(rs.getString("department"));
                member.setPhone(student.getPhone());
                member.setEmail(student.getEmail());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    public static Member getMemberDetailsByUIUId(int member_id) {

        Member member = new Member();
        String get = "SELECT * FROM members WHERE member=?";
        connection = dbHandler.getConnection();
        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1,member_id);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Student student = StudentMiddleware.getData(member_id);

                member.setDesignation(rs.getString("rank"));
                member.setName(student.getName());
                member.setId(rs.getInt("enrol_id"));
                member.setUIUid(student.getUIU_id());
                member.setDepartment(rs.getString("department"));
                member.setPhone(student.getPhone());
                member.setEmail(student.getEmail());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    public static Member getMemberDetailsByUMIDandCFref(int member_id, int cf_ref) {

        Member member = new Member();
        String get = "SELECT * FROM members WHERE (member=? AND `cf_ref`=?)";
        connection = dbHandler.getConnection();
        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1,member_id);
            pst.setInt(2, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Student student = StudentMiddleware.getData(member_id);

                member.setDesignation(rs.getString("rank"));
                member.setName(student.getName());
                member.setId(rs.getInt("enrol_id"));
                member.setUIUid(student.getUIU_id());
                member.setDepartment(rs.getString("department"));
                member.setPhone(student.getPhone());
                member.setEmail(student.getEmail());

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return member;
    }

    public static ArrayList<Member> getMyClubForums(int member) {
        String get = "SELECT * FROM members WHERE (`member`=? AND selected='true')";

        ArrayList<Member> myCFs = new ArrayList<>();

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1,member);

            ResultSet rs = pst.executeQuery();


            while (rs.next()) {
                double regFee = CFMiddleware.getCFById(rs.getInt("cf_ref")).getRegistrationFee();
                if(rs.getString("completed_fee").equals("F")) {
                    myCFs.add(new Member(
                            CFMiddleware.getCFById(rs.getInt("cf_ref")).getName(),
                            rs.getString("rank"),
                            rs.getString("department"),
                            Double.toString((regFee - rs.getDouble("fee_paid"))) + " due"
                    ));
                } else {
                    myCFs.add(new Member(
                            CFMiddleware.getCFById(rs.getInt("cf_ref")).getName(),
                            rs.getString("rank"),
                            rs.getString("department"),
                            "Completed"
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return myCFs;
    }

    public static ArrayList<String> getAllMemberEnrolIds(int cf_ref) {
        ArrayList<String> idList = new ArrayList<>();
        String get = "SELECT `enrol_id` FROM members WHERE `cf_ref`=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idList.add(Integer.toString(rs.getInt("enrol_id")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idList;

    }

    public static ArrayList<Integer> getAllMemberEnrolIdsAsInts(int cf_ref) {
        ArrayList<Integer> idList = new ArrayList<>();
        String get = "SELECT `enrol_id` FROM members WHERE `cf_ref`=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idList.add(rs.getInt("enrol_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idList;

    }

    public static ArrayList<String> getAllMemberUMIds(int cf_ref) {
        ArrayList<Integer> idList = new ArrayList<>();
        String get = "SELECT `member` FROM members WHERE `cf_ref`=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idList.add(rs.getInt("member"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(idList);

        ArrayList<String> idsInString = new ArrayList<>();
        for(int i : idList) {
            idsInString.add(Integer.toString(i));
        }

        return idsInString;

    }

    public static ArrayList<Integer> getAllMemberUMIdsAsInts(int cf_ref) {
        ArrayList<Integer> idList = new ArrayList<>();
        String get = "SELECT `member` FROM members WHERE `cf_ref`=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idList.add(rs.getInt("member"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        Collections.sort(idList);

        return idList;
    }

    public static ArrayList<Integer> getAllMemberEnrolIdsAsInts(String cfName) {
        ArrayList<Integer> idList = new ArrayList<>();
        String get = "SELECT `enrol_id` FROM members WHERE cf=?";

        connection = dbHandler.getConnection();

        try {

            pst = connection.prepareStatement(get);

            pst.setString(1,cfName);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idList.add(rs.getInt("enrol_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idList;

    }

    public static void updateMember(int memberID, String designation, String department, int cf_ref) {
        String update = "UPDATE members SET `rank`=?,`department`=? WHERE (member=? AND `cf_ref`=?)";

        connection = dbHandler.getConnection();

        try {
            pst = connection.prepareStatement(update);

            pst.setString(1, designation);
            pst.setString(2, department);
            pst.setInt(3, memberID);
            pst.setInt(4, cf_ref);

            pst.executeUpdate();

            System.out.println("Updated");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeMember(int enrolId) {
        String remove = "DELETE FROM members WHERE `enrol_id`=?";

        connection = dbHandler.getConnection();
        try {
            pst = connection.prepareStatement(remove);

            pst.setInt(1,enrolId);

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIfMemberIsInCF(String memberName, String UIUid, String cfName) {
        String check = "SELECT * FROM members WHERE member_name=? and `UIU_id`=? and cf=?";

        connection = dbHandler.getConnection();
        try {
            pst = connection.prepareStatement(check);

            pst.setString(1,memberName);
            pst.setString(2,UIUid);
            pst.setString(3,cfName);

            ResultSet rs = pst.executeQuery();

            int count = 0;
            while (rs.next())  {
                count++;
            }

            if(count > 0) {
                return true;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static int getMembersCount(int cf_ref) {
        String get = "SELECT * FROM members WHERE `cf_ref`=?";
        int count = 0;
        try {
            connection = dbHandler.getConnection();

            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public static ArrayList<ClubForum> getClubForum(int member) {
        String get = "SELECT * FROM members WHERE (`member`=? AND selected='true')";
        ArrayList<ClubForum> cfs =  new ArrayList<>();
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, member);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cfs.add(new ClubForum(
                        CFMiddleware.getCFById(rs.getInt("cf_ref")).getName()
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cfs;
    }

    public static double getMembersFee(int id, int cf_ref) {
        String update = "SELECT * FROM members WHERE `member`=? AND `cf_ref`=?";
        double amount = 0;
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(update);

            pst.setInt(1, id);
            pst.setInt(2, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                amount = rs.getDouble("fee_paid");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return amount;
    }

    public static HashMap<Integer, Double> getMultipleMembersFee(ArrayList<Integer> ids, int cf_ref) {
        HashMap<Integer, Double> idFeeMap = new HashMap<>();
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("SELECT * FROM members WHERE `member` IN (");

        for(int i = 0; i < ids.size(); ++i) {
            if(i == ids.size()-1) {
                updateBuilder.append(ids.get(i)).append(")");
            } else {
                updateBuilder.append(ids.get(i)).append(",");
            }
        }

        updateBuilder.append(" AND `cf_ref`=?");

        String update = updateBuilder.toString();

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(update);

            pst.setInt(1,cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                idFeeMap.put(rs.getInt("member"),rs.getDouble("fee_paid"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return idFeeMap;
    }

    public static void updateFeeGiven(double fee, int id, int cf_ref) throws Exception {
        String update = "UPDATE members SET `fee_paid`=? WHERE `member`=? AND `cf_ref`=?";
        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(update);

            pst.setDouble(1, fee);
            pst.setInt(2,id);
            pst.setInt(3,cf_ref);

            pst.executeUpdate();

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static void updateFeesGiven(double fee, HashMap<Integer, Double> idFeeMap, ArrayList<Integer> ids, int cf_ref) throws Exception {
        /*
        StringBuilder updateBuilder = new StringBuilder();
        updateBuilder.append("UPDATE members SET `fee_paid`=? WHERE `enrol_id` IN (");

        for(int i = 0; i < ids.size(); ++i) {
            if(i == ids.size()-1) {
                updateBuilder.append(ids.get(i)).append(")");
            } else {
                updateBuilder.append(ids.get(i)).append(",");
            }
        }
        */


        try {
            String update = "";
            connection = dbHandler.getConnection();

            for(int id : ids) {
                update = "UPDATE members SET `fee_paid`="+(fee + idFeeMap.get(id))+" WHERE `member`=? AND `cf_ref`=?";
                pst = connection.prepareStatement(update);
                pst.setInt(1, id);
                pst.setInt(2, cf_ref);
                pst.executeUpdate();
            }

        } catch (SQLException e) {
            throw new Exception(e.getMessage());
        }
    }

    public static HashMap<String, Double> getCfFees(Member member) {
        String get = "SELECT * FROM members WHERE `UIU_id`=?";

        HashMap<String, Double> map = new HashMap<>();
        try {
            connection = dbHandler.getConnection();

            pst = connection.prepareStatement(get);

            pst.setString(1, member.getUIUid());
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                double regFee = CFMiddleware.getCFByName(rs.getString("cf")).getRegistrationFee();
                if(rs.getDouble("fee_paid") < regFee && rs.getString("completed_fee").equals("F")) {
                    map.put(rs.getString("cf"), (regFee - rs.getDouble("fee-paid")));
                } else if (rs.getDouble("fee_paid") >= regFee) {
                    map.put(rs.getString("cf"), 0d);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static ArrayList<Member> membersWhoseMembershipArePending(int cf_ref) {
        String get = "SELECT * FROM members WHERE `cf_ref`=?";
        ArrayList<Member> members = new ArrayList<>();

        try {
            connection = dbHandler.getConnection();
            pst = connection.prepareStatement(get);

            pst.setInt(1, cf_ref);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                if(rs.getString("selected").equals("false")) {
                    members.add(new Member(
                            rs.getInt("enrol_id"),
                            rs.getInt("member"),
                            StudentMiddleware.getData(rs.getInt("member")).getUIU_id(),
                            StudentMiddleware.getData(rs.getInt("member")).getName(),
                            rs.getString("reason")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

}
