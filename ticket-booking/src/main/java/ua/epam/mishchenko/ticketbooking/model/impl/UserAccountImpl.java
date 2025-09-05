package ua.epam.mishchenko.ticketbooking.model.impl;

import ua.epam.mishchenko.ticketbooking.model.UserAccount;

public class UserAccountImpl implements UserAccount {

    private Long id;
    private Long userId;
    private int userAmount;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public int getUserAmount() {
        return userAmount;
    }

    @Override
    public void setUserAmount(int userAmount) {
        this.userAmount = userAmount;
    }
}
