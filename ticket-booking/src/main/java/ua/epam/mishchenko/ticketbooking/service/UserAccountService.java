package ua.epam.mishchenko.ticketbooking.service;

import ua.epam.mishchenko.ticketbooking.model.UserAccount;

public interface UserAccountService {

    int refillUserAccount(long userId, int amount);
}
