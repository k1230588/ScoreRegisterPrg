/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

/**
 *
 * @author user
 */
public class UserInfo {

    public int uID;
    public String uName;
    public String uPass;
    public int uClass;
    public String uPosi;

    public UserInfo() {
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public String getuName() {
        return uName;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuPass() {
        return uPass;
    }

    public void setuPass(String uPass) {
        this.uPass = uPass;
    }

    public int getuClass() {
        return uClass;
    }

    public void setuClass(int uClass) {
        this.uClass = uClass;
    }

    public String getuPosi() {
        return uPosi;
    }

    public void setuPosi(String uPosi) {
        this.uPosi = uPosi;
    }

    public int getuAdmin() {
        return uAdmin;
    }

    public void setuAdmin(int uAdmin) {
        this.uAdmin = uAdmin;
    }
    public int uAdmin;

    public UserInfo(int uID, String uName, String uPass, int uClass, String uPosi) {
        this.uID = uID;
        this.uName = uName;
        this.uPass = uPass;
        this.uClass = uClass;
        this.uPosi = uPosi;
        switch (uPosi) {
            case "学生":
                this.uAdmin = 2;
                break;
            case "教師":
                this.uAdmin = 1;
                break;
            case "管理職":
                this.uAdmin = 0;
                break;
            default:
                this.uAdmin = 3;
        }

    }

}
