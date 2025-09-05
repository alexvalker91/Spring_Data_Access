package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ua.epam.mishchenko.ticketbooking.entity.UserAccountDb;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.repository.DbUserAccountRepository;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;

import java.util.Optional;

public class DbUserAccountServiceImpl implements UserAccountService {

    private static final Logger LOGGER = LogManager.getLogger(DbUserAccountServiceImpl.class);

    @Autowired
    private DbUserAccountRepository dbUserAccountRepository;

    @Override
    public int refillUserAccount(long userId, int amount) {
        Optional<UserAccountDb> userAccountDbOptional = dbUserAccountRepository.findById(userId);
        if (userAccountDbOptional.isPresent()) {
            UserAccountDb userAccountDb = userAccountDbOptional.get();
            userAccountDb.setAmount(userAccountDb.getAmount() + amount);
            this.dbUserAccountRepository.save(userAccountDb);
            LOGGER.log(Level.DEBUG, "User with id {} successfully found ", userId);
            return amount;
        } else {
            LOGGER.log(Level.WARN, "Can not to find an user by id: " + userId);
            return -1;
        }
    }

    private UserAccountDb mapUserAccountToUserAccountDb(UserAccount userAccount) {
        return new UserAccountDb(userAccount.getId(), userAccount.getUserId(), userAccount.getUserAmount());
    }
}
