package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class ClearService {
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public ClearService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public void clear() {
        gameDAO.clear();
        userDAO.clear();
        authDAO.clear();
    }
}
