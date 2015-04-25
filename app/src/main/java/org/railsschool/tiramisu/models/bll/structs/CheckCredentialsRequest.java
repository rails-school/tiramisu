package org.railsschool.tiramisu.models.bll.structs;

/**
 * @class CheckCredentialsRequest
 * @brief
 */
public class CheckCredentialsRequest {
    private String _email;
    private String _password;

    public CheckCredentialsRequest(String email, String password) {
        this._email = email;
        this._password = password;
    }

    public String getEmail() {
        return _email;
    }

    public String getPassword() {
        return _password;
    }
}
