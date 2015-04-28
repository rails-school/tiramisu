package org.railsschool.tiramisu.models.bll.remote;

import retrofit.RequestInterceptor;

/**
 * @class AuthenticationInterceptor
 * @brief Adds authentication cookie to http headers
 */
class AuthenticationInterceptor implements RequestInterceptor {
    private String _authenticationCookie;

    public AuthenticationInterceptor(String authenticationCookie) {
        this._authenticationCookie = authenticationCookie;
    }

    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("Cookie", "remember_user_token=" + _authenticationCookie);
    }
}
