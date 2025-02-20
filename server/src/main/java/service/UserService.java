package service;

import dataaccess.*;
import models.AuthData;
import models.UserData;

import java.util.UUID;

public class UserService {

    private UserDAO userDAO;
    private AuthDAO authDAO;

    RegisterResult register(RegisterRequest request) throws BadRequestException {
        try {
            if (userDAO.getUser(request.username()) != null) {
                throw new BadRequestException("Error: already taken");
            }
            UserData newUser = new UserData(request.username(), request.password(), request.email());
            userDAO.createUser(newUser);

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(authToken, request.username());
            authDAO.createAuth(authData);

            return new RegisterResult(request.username(), authToken);
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
    }



    void clear() {
        userDAO.clear();
        authDAO.clear();
    }
}
