package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.repository.DbTicketRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;

import java.util.List;

public class DbTicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LogManager.getLogger(DbTicketServiceImpl.class);

    @Autowired
    private DbTicketRepository dbTicketRepository;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return null;
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return List.of();
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return List.of();
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return false;
    }
}
