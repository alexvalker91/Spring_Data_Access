package ua.epam.mishchenko.ticketbooking.entity;

import jakarta.persistence.*;

@Entity
@Table(schema = "public", name = "ticket_db")
public class TicketDb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "event_id")
    private long eventId;

    @Column(name = "user_id")
    private long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "ticket_place")
    private int place;

    public enum Category {STANDARD, PREMIUM, BAR}
}

//CREATE TYPE ticket_category AS ENUM ('STANDARD', 'PREMIUM', 'BAR');

//CREATE TABLE ticket_db (
//        id SERIAL PRIMARY KEY,
//        event_id INT REFERENCES event_db(id),
//user_id INT REFERENCES user_db(id),
//category ticket_category,
//ticket_place INTEGER
//);