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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
//        System.out.println(uclass);
//        System.out.println(uadmin);
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
//        System.out.println(sql);
        rset = stmt.executeQuery(sql);
        if (rset != null) {
            while (rset.next()) {
                UserScore usc = new UserScore();
                usc.setsID(rset.getInt("sid"));
                usc.setsClass(rset.getInt("sclass"));
                usc.setsName(rset.getString("sname"));
                usc.setLanS(rset.getInt("language"));
                usc.setEngS(rset.getInt("english"));
                usc.setMatS(rset.getInt("math"));
                usc.setHisS(rset.getInt("history"));
                usc.setSciS(rset.getInt("science"));
                usc.setScid(rset.getLong("scid"));
                list.add(usc);
            }
        }
        return list;
    }

    public List<UserScore> SearchUI(int sIO, UserInfo ui, int uAdmin, int ruiclass) throws SQLException {
        List<UserScore> list = new ArrayList();
        List sqlList = new ArrayList();
        String sql;

        switch (sIO) {
//          　あいまい検索  
            case 1:
                if (ui.getuID() != 0) {
                    sqlList.add("cast(scorelist.sid as varchar(255)) like '%" + ui.getuID() + "%' ");
                }
                if (!ui.getuName().equals("null")) {
                    sqlList.add("sname like '%" + ui.getuName() + "%' ");
                }
                break;
//            完全一致
            case 0:
                if (ui.getuID() != 0) {
                    sqlList.add("scorelist.sid = " + ui.getuID() + " ");
                }
                if (!ui.getuName().equals("null")) {
                    sqlList.add("sname = '" + ui.getuName() + "' ");
                }
                break;
            default:
                return null;
        }
//        SQL命令作成
        sql = "select scorelist.sid,sclass ,sname ,language, math, english, history, science, scid from scorelist left join stulist on scorelist.sid = stulist.sid where ";
        sql += sqlList.get(0);
        if (sqlList.size() > 1) {
            for (int i = 1; i < sqlList.size(); i++) {
                sql += "and " + sqlList.get(i);
            }
        }
        if (uAdmin == 0) {
            sql += ";";
        } else {
            sql += "and sclass = " + ruiclass + ";";
        }
//        System.out.println(sql);
        rset = stmt.executeQuery(sql);
        if (rset != null) {
            while (rset.next()) {
                UserScore usc = new UserScore();
                usc.setsID(rset.getInt("sid"));
                usc.setsClass(rset.getInt("sclass"));
                usc.setsName(rset.getString("sname"));
                usc.setLanS(rset.getInt("language"));
                usc.setEngS(rset.getInt("english"));
                usc.setMatS(rset.getInt("math"));
                usc.setHisS(rset.getInt("history"));
                usc.setSciS(rset.getInt("science"));
                usc.setScid(rset.getLong("scid"));
                list.add(usc);
            }
        }

        return list;
    }

    public void DScore(long DeleteL) throws SQLException {
        String sql = "delete from scorelist where scid = " + DeleteL + ";";
        stmt.executeUpdate(sql);
        conn.commit();
    }

    public String RScore(UserScore usscore, UserInfo rui) throws SQLException {
        int uAdmin = rui.getuAdmin();
        int ssid = usscore.getsID();
        UserInfo sui = new UserInfo();
        String rScore;
        // scid作成
        SimpleDateFormat sdt = new SimpleDateFormat("yyyyMMddhhmmss");
        Date date = new Date(System.currentTimeMillis());
        String scid = usscore.getsID() + String.valueOf(sdt.format(date));
        usscore.setScid(Long.parseLong(scid));

        String sql = "select * from ulist where uid = " + ssid + " and uadmin = 2;";
        System.out.println(sql);
        rset = stmt.executeQuery(sql);
        if (rset != null) {
            while (rset.next()) {
                sui.setuClass(rset.getInt("uclass"));
                sui.setuID(rset.getInt("uid"));
                sui.setuName(rset.getString("uname"));
            }
        }

//        入力したUID存在するか
        if (sui.getuID() != usscore.getsID()) {
            rScore = "Student not exist";
            return rScore;
        }
//       登録者が権限を持っているか
        if (uAdmin == 1) {
            if (rui.getuClass() != sui.getuClass()) {
                rScore = "No permisson to register";
                return rScore;
            }
        }
        
//        スコアを登録する
        sql = "insert into scorelist (sid, language, english, math, history, science, create_time, scid)";
        sql += "VALUES (" + usscore.getsID() + "," + usscore.getLanS() + "," + usscore.getEngS() + ",";
        sql += usscore.getMatS() + "," + usscore.getHisS() + "," + usscore.getSciS() + ",";
        sql += "current_timestamp, " + usscore.getScid() + ");";
        System.out.println(sql);
        
        stmt.executeUpdate(sql);
        conn.commit();
        
        

        rScore = "Finished";
        return rScore;
    }
    
    public void EScore(UserScore sc){

        
    }
}
