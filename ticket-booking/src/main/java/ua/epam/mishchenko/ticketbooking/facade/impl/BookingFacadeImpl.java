package ua.epam.mishchenko.ticketbooking.facade.impl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.Date;
import java.util.List;

public class BookingFacadeImpl implements BookingFacade {

    private static final Logger LOGGER = LogManager.getLogger(BookingFacadeImpl.class);

    private final EventService eventService;

    private final TicketService ticketService;

    private final UserService userService;

    private final UserAccountService userAccountService;

    public BookingFacadeImpl(EventService eventService,
                             TicketService ticketService,
                             UserService userService,
                             UserAccountService userAccountService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
        this.userAccountService = userAccountService;
    }

    @Override
    public Event getEventById(long eventId) {
        LOGGER.log(Level.DEBUG, "Getting event by id: {}", eventId);
        return eventService.getEventById(eventId);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG, "Getting events by title: {}, pageSize: {}, pageNum: {}", title, pageSize, pageNum);
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG, "Getting events for day: {}, pageSize: {}, pageNum: {}", day, pageSize, pageNum);
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        LOGGER.log(Level.DEBUG, "Creating event: {}", event);
        Event created = eventService.createEvent(event);
        LOGGER.log(Level.DEBUG, "Created event: {}", created);
        return created;
    }

    @Override
    public Event updateEvent(Event event) {
        LOGGER.log(Level.DEBUG, "Updating event: {}", event);
        Event updated = eventService.updateEvent(event);
        LOGGER.log(Level.DEBUG, "Updated event: {}", updated);
        return updated;
    }

    @Override
    public boolean deleteEvent(long eventId) {
        LOGGER.log(Level.DEBUG, "Deleting event with id: {}", eventId);
        boolean deleted = eventService.deleteEvent(eventId);
        LOGGER.log(Level.DEBUG, "Deleted event with id {}: {}", eventId, deleted);
        return deleted;
    }

    @Override
    public User getUserById(long userId) {
        LOGGER.log(Level.DEBUG, "Getting user by id: {}", userId);
        return userService.getUserById(userId);
    }

    @Override
    public User getUserByEmail(String email) {
        LOGGER.log(Level.DEBUG, "Getting user by email: {}", email);
        return userService.getUserByEmail(email);
    }

    @Override
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG, "Getting users by name: {}, pageSize: {}, pageNum: {}", name, pageSize, pageNum);
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        LOGGER.log(Level.DEBUG, "Creating user: {}", user);
        User created = userService.createUser(user);
        LOGGER.log(Level.DEBUG, "Created user: {}", created);
        return created;
    }

    @Override
    public User updateUser(User user) {
        LOGGER.log(Level.DEBUG, "Updating user: {}", user);
        User updated = userService.updateUser(user);
        LOGGER.log(Level.DEBUG, "Updated user: {}", updated);
        return updated;
    }

    @Override
    public boolean deleteUser(long userId) {
        LOGGER.log(Level.DEBUG, "Deleting user with id: {}", userId);
        boolean deleted = userService.deleteUser(userId);
        LOGGER.log(Level.DEBUG, "Deleted user with id {}: {}", userId, deleted);
        return deleted;
    }

    @Override
    @Transactional
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        LOGGER.log(Level.DEBUG, "Booking ticket for userId: {}, eventId: {}, place: {}, category: {}", userId, eventId, place, category);
        Event event = eventService.getEventById(eventId);
        int ticketPrice = event.getTicketPrice();

        UserAccount userAccount = userAccountService.getUserAccountByUserId(userId);
        int userAmount = userAccount.getUserAmount();

        if (ticketPrice > userAmount) {
            LOGGER.log(Level.WARN, "Insufficient funds. userId: {}, eventId: {}, price: {}, balance: {}", userId, eventId, ticketPrice, userAmount);
            return null;
        } else {
            userAccount.setUserAmount(userAccount.getUserAmount() - ticketPrice);
            userAccountService.updateUserAccount(userAccount);
            Ticket ticket = ticketService.bookTicket(userId, eventId, place, category);
            LOGGER.log(Level.DEBUG, "Booked ticket: {}", ticket);
            return ticket;
        }
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG, "Getting booked tickets by user: {}, pageSize: {}, pageNum: {}", user, pageSize, pageNum);
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        LOGGER.log(Level.DEBUG, "Getting booked tickets by event: {}, pageSize: {}, pageNum: {}", event, pageSize, pageNum);
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    @Transactional
    public boolean cancelTicket(long ticketId) {
        LOGGER.log(Level.DEBUG, "Canceling ticket with id: {}", ticketId);
        Ticket ticket = ticketService.getById(ticketId);
        long eventId = ticket.getEventId();
        Event event = eventService.getEventById(eventId);
        int ticketPrice = event.getTicketPrice();
        long userId = ticket.getUserId();
        userAccountService.refillUserAccount(userId, ticketPrice);
        boolean canceled = ticketService.cancelTicket(ticketId);
        LOGGER.log(Level.DEBUG, "Canceled ticket with id {}: {}", ticketId, canceled);
        return canceled;
    }

    @Override
    public int refillUserAccount(long userId, int amount) {
        LOGGER.log(Level.DEBUG, "Refilling user account. userId: {}, amount: {}", userId, amount);
        int newAmount = userAccountService.refillUserAccount(userId, amount);
        LOGGER.log(Level.DEBUG, "Refilled user account. userId: {}, newAmount: {}", userId, newAmount);
        return newAmount;
    }
}
