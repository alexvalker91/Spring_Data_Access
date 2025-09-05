package ua.epam.mishchenko.ticketbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "user_db")
public class UserDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_email")
    private String email;
}

//CREATE TABLE user_db (
//        id SERIAL PRIMARY KEY,
//        user_name VARCHAR(255) NOT NULL,
//user_email VARCHAR(255) NOT NULL
//);