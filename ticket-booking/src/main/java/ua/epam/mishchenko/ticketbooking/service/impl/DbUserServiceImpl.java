package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.epam.mishchenko.ticketbooking.entity.EventDb;
import ua.epam.mishchenko.ticketbooking.entity.UserDb;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.repository.DbUserRepository;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DbUserServiceImpl implements UserService {

    private static final Logger LOGGER = LogManager.getLogger(DbUserServiceImpl.class);

    @Autowired
    private DbUserRepository dbUserRepository;

    @Override
    public User getUserById(long userId) {
        LOGGER.log(Level.DEBUG, "Finding an user by id: {}", userId);
        Optional<UserDb> userDbOptional = this.dbUserRepository.findById(userId);
        if (userDbOptional.isPresent()) {
            User user = mapUserDbToUser(userDbOptional.get());
            LOGGER.log(Level.DEBUG, "User with id {} successfully found ", userId);
            return user;
        } else {
            LOGGER.log(Level.WARN, "Can not to find an user by id: " + userId);
            return null;
        }
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<UserDb> userDbOptional = this.dbUserRepository.findByEmail(email);
        if (userDbOptional.isPresent()) {
            User user = mapUserDbToUser(userDbOptional.get());
            LOGGER.log(Level.DEBUG, "User with email {} successfully found ", email);
            return user;
        } else {
            LOGGER.log(Level.WARN, "Can not to find an user by email: " + email);
            return null;
        }
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<UserDb> usersPage = dbUserRepository.findAllByName(name, pageable);
        return usersPage.getContent()
                .stream()
                .map(userDb -> new UserImpl(
                        userDb.getId(),
                        userDb.getName(),
                        userDb.getEmail()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        if (user == null) {
            LOGGER.log(Level.WARN, "Can not to create an user: {}", user);
            return null;
        }
        UserDb userDb = mapUserToUserDb(user);
        userDb.setId(null);
        this.dbUserRepository.save(userDb);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            LOGGER.log(Level.WARN, "Can not to create an user: {}", user);
            return null;
        }
        UserDb userDb = mapUserToUserDb(user);
        this.dbUserRepository.save(userDb);
        return user;
    }

    @Override
    public boolean deleteUser(long userId) {
        Optional<UserDb> userDbOptional = this.dbUserRepository.findById(userId);
        if (userDbOptional.isPresent()) {
            this.dbUserRepository.deleteById(userId);
            LOGGER.log(Level.DEBUG, "Successfully deletion of the user with id: {}", userId);
            return true;
        } else {
            LOGGER.log(Level.WARN, "Can not to delete an user with id: {}", userId);
            return false;
        }
    }

    private User mapUserDbToUser(UserDb userDb) {
        return new UserImpl(userDb.getId(), userDb.getName(), userDb.getEmail());
    }

    private UserDb mapUserToUserDb(User user) {
        return new UserDb(user.getId(), user.getName(), user.getEmail());
    }
}
