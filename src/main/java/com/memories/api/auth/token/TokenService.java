package com.memories.api.auth.token;

import com.memories.api.auth.dto.Credentials;
import com.memories.api.user.User;

public interface TokenService {

   Token createToken(User user, Credentials creds);

    User verifyToken(String authorizationHeader);


}