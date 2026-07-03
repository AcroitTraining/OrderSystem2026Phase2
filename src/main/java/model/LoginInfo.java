package model;

import java.io.Serializable;

public class LoginInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String loginId;
    private String loginPassword;

    // コンストラクタ
    public LoginInfo() {}
    
    public LoginInfo(String loginId, String loginPassword) {
        this.loginId = loginId;
        this.loginPassword = loginPassword;
    }

    // ゲッター・セッター
    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getLoginPassword() { return loginPassword; }
    public void setLoginPassword(String loginPassword) { this.loginPassword = loginPassword; }
}
