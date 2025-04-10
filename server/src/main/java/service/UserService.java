package service;

import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.DuplicatedException;
import dataaccess.exceptions.UnauthorizedException;
import models.AuthData;
import models.UserData;
import models.requests.LoginRequest;
import models.requests.LogoutRequest;
import models.requests.RegisterRequest;
import models.results.LoginResult;
import models.results.RegisterResult;

import java.util.UUID;

public class UserService {

    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null || request.email() == null
                || request.username().isEmpty() || request.password().isEmpty() || request.email().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        if (userDAO.getUser(request.username()) != null) {
            throw new DuplicatedException("Error: already taken");
        }

        userDAO.createUser(new UserData(request.username(), request.password(), request.email()));

        AuthData auth = new AuthData(UUID.randomUUID().toString(), request.username());
        authDAO.createAuth(auth);

        return new RegisterResult(request.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest request) throws DataAccessException {
        if (request.username() == null || request.password() == null
                || request.username().isEmpty() || request.password().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        UserData user = userDAO.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        if (!userDAO.verifyUser(user.username(), request.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        AuthData auth = new AuthData(UUID.randomUUID().toString(), request.username());
        authDAO.createAuth(auth);

        return new LoginResult(request.username(), auth.authToken());
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        try {
            authDAO.getAuth(request.authToken());
            authDAO.deleteAuth(request.authToken());

        } catch (DataAccessException e) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
