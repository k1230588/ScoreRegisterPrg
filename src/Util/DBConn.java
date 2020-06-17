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
import object.UserInfo;

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
                System.out.println(cPass + "////" + ui.getuPass());
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

}
