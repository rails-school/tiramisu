package org.railsschool.tiramisu.models.bll.structs;

/**
 * @class CheckCredentialsRequest
 * @brief
 */
public class CheckCredentialsRequest {
    private String _username;
    private String _password;

    public CheckCredentialsRequest(String username, String password) {
        this._username = username;
        this._password = password;
    }

    public String getUsername() {
        return _username;
    }

    public String getPassword() {
        return _password;
    }
}
