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
public class UserScore {
    public int LanS;
    public int EngS;
    public int MatS;
    public int HisS;
    public int sClass;
    public String sName;
    public int sID;

    public int getsClass() {
        return sClass;
    }

    public void setsClass(int sClass) {
        this.sClass = sClass;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public int getsID() {
        return sID;
    }

    public void setsID(int sID) {
        this.sID = sID;
    }

    public int getLanS() {
        return LanS;
    }

    public void setLanS(int LanS) {
        this.LanS = LanS;
    }

    public int getEngS() {
        return EngS;
    }

    public void setEngS(int EngS) {
        this.EngS = EngS;
    }

    public int getMatS() {
        return MatS;
    }

    public void setMatS(int MatS) {
        this.MatS = MatS;
    }

    public int getHisS() {
        return HisS;
    }

    public void setHisS(int HisS) {
        this.HisS = HisS;
    }

    public int getSciS() {
        return SciS;
    }

    public void setSciS(int SciS) {
        this.SciS = SciS;
    }
    public int SciS;
    
    public UserScore(){
    }
}
