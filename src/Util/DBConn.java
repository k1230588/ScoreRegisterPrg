/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import object.UserInfo;
import object.UserScore;

/**
 *
 * @author user
 */
public class DBConn {

    Connection conn = null;
    Statement stmt = null;
    ResultSet rset = null;
    String url = "jdbc:postgresql://localhost:5432/ScoreProject";
    String user = "postgres";
    String password = "KSadmin2020";

    public Statement getDBC() throws ClassNotFoundException {
        if (conn == null) {
            try {
                Class.forName("org.postgresql.Driver");
                //PostgreSQLへ接続
                conn = DriverManager.getConnection(url, user, password);
                //自動コミットOFF
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
            } catch (SQLException e) {

            }
        }
        return stmt;
    }

    public void closeDBC() throws SQLException {
        if (conn != null) {
            stmt.close();
            conn.close();

        }
    }

    public int userLogin(UserInfo ui) throws SQLException {
        String sql = "select * from ulist where uid = " + ui.getuID() + ";";
        rset = stmt.executeQuery(sql);

        if (rset != null) {
            while (rset.next()) {
                String cPass = rset.getString("upass");
//                System.out.println(cPass + "////" + ui.getuPass());
                if (ui.getuPass().equals(cPass)) {
                    switch (rset.getInt("uadmin")) {
                        case 0:
                            return 0;
                        case 1:
                            return 1;
                        case 2:
                            return 2;
                        default:
                            return 9;
                    }
                } else {
                    return 9;
                }
            }
        }

        return 9;

    }

    public List<UserInfo> getLoginInfo(int uid) throws SQLException {
        List<UserInfo> list = new ArrayList();
        String sql = "select * from ulist where uid = " + uid + ";";
        rset = stmt.executeQuery(sql);
        if (rset != null) {
            while (rset.next()) {
                UserInfo ui = new UserInfo();
                ui.setuID(rset.getInt("uid"));
                ui.setuClass(rset.getInt("uclass"));
                ui.setuName(rset.getString("uname"));
                ui.setuClass(rset.getInt("uclass"));
                ui.setuPosi(rset.getString("uposi"));
                ui.setuAdmin(rset.getInt("uadmin"));
                list.add(ui);
            }
        }
        return list;
    }

    public List<UserScore> GetUScoreL(int uid) throws SQLException {
        List<UserScore> list = new ArrayList();
        String sql = "select * from scorelist where sid = " + uid + ";";
        rset = stmt.executeQuery(sql);

        if (rset != null) {
            while (rset.next()) {
                UserScore us = new UserScore();
                us.setLanS(rset.getInt("language"));
                us.setEngS(rset.getInt("english"));
                us.setMatS(rset.getInt("math"));
                us.setHisS(rset.getInt("history"));
                us.setSciS(rset.getInt("science"));
                list.add(us);
            }
        }

        return list;
    }

    public String upPass(String oldPass, String newPass, int uid) throws SQLException {

        String sql = "select * from ulist where uid = " + uid + ";";
        rset = stmt.executeQuery(sql);
        String passCheck = null;
        if (rset != null) {
            while (rset.next()) {
                passCheck = rset.getString("upass");
//                System.out.println(passCheck);
            }
        }

        if (!passCheck.equals(oldPass)) {
            return "Old Password is not Matched";
        } else {
            sql = "update ulist set upass = '" + newPass + "' where uid = '" + uid + "';";
//            System.out.println(sql);
            stmt.executeUpdate(sql);
            conn.commit();
            return "Password has been Updated";
        }

    }

    public List<UserScore> UserAllT(int uid, int uadmin, int uclass) throws SQLException {

        List<UserScore> list = new ArrayList();
        String sql;
        System.out.println(uclass);
        System.out.println(uadmin);
        switch (uadmin) {
            case 1:
                sql = "select * from scorelist left join stulist on scorelist.sid = stulist.sid where sclass = " + uclass + ";";
                break;
            case 0:
                sql = "select * from scorelist left join stulist on scorelist.sid = stulist.sid;";
                break;
            default:
                sql = "select * from scorelist left join stulist on scorelist.sid = stulist.sid where sclass = " + uclass + ";";
                break;
        }
        System.out.println(sql);
        rset = stmt.executeQuery(sql);
        if (rset != null) {
            while (rset.next()) {
                UserScore us = new UserScore();
                us.setsID(rset.getInt("sid"));
                us.setsClass(rset.getInt("sclass"));
                us.setsName(rset.getString("sname"));
                us.setLanS(rset.getInt("language"));
                us.setEngS(rset.getInt("english"));
                us.setMatS(rset.getInt("math"));
                us.setHisS(rset.getInt("history"));
                us.setSciS(rset.getInt("science"));
                list.add(us);
            }
        }
        return list;
    }

    public List<UserInfo> SearchUI(int sIO, UserInfo ui) {
        List<UserInfo> list = new ArrayList();
        List sqlList = new ArrayList();
        String sql;

        if (ui.getuID() == 0 && ui.getuName().equals("null")) {
            System.out.print(ui.getuID() + "///" + ui.getuName());
        }

        switch (sIO) {
//          　あいまい検索  
            case 1:

//            完全一致
            case 0:
                if (ui.getuID() != 0) {
                    sqlList.add("sid = " + ui.getuID());
                }
            if (!ui.getuName().equals("null")) {
                    sqlList.add("sid = " + ui.getuID());
            }
            default:
        }

        return null;
    }
}
