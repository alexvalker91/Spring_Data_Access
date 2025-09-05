package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.List;

public class DbUserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(DbUserServiceImpl.class);

    @Override
    public User getUserById(long userId) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return List.of();
    }

    @Override
    public User createUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public boolean deleteUser(long userId) {
        return false;
    }
}
