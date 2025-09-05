package ua.epam.mishchenko.ticketbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "user_account_db")
public class UserAccountDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "user_amount")
    private int amount;
}

//CREATE TABLE user_account_db (
//        id SERIAL PRIMARY KEY,
//user_id INT REFERENCES user_db(id),
//user_amount INTEGER
//);