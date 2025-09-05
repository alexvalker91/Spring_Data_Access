package ua.epam.mishchenko.ticketbooking.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ua.epam.mishchenko.ticketbooking.entity.TicketDb;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl;
import ua.epam.mishchenko.ticketbooking.repository.DbTicketRepository;
import ua.epam.mishchenko.ticketbooking.service.TicketService;

import java.util.List;
import java.util.stream.Collectors;

public class DbTicketServiceImpl implements TicketService {

    private static final Logger LOGGER = LogManager.getLogger(DbTicketServiceImpl.class);

    @Autowired
    private DbTicketRepository dbTicketRepository;

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        if (dbTicketRepository.existsByEventIdAndPlace(eventId, place)) {
            LOGGER.warn("Place {} is already booked for event {}", place, eventId);
            return null;
        }

        TicketDb ticketDb = new TicketDb();
        ticketDb.setId(null);
        ticketDb.setUserId(userId);
        ticketDb.setEventId(eventId);
        ticketDb.setPlace(place);
        ticketDb.setCategory(TicketDb.Category.valueOf(category.name()));
        TicketDb savedTicket = dbTicketRepository.save(ticketDb);
        return mapTicketDbToTicket(savedTicket);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<TicketDb> ticketsPage = dbTicketRepository.findAllByUserId(user.getId(), pageable);

        return ticketsPage.getContent()
                .stream()
                .map(this::mapTicketDbToTicket)
                .collect(Collectors.toList());
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<TicketDb> ticketsPage = dbTicketRepository.findAllByEventId(event.getId(), pageable);

        return ticketsPage.getContent()
                .stream()
                .map(this::mapTicketDbToTicket)
                .collect(Collectors.toList());
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        if (dbTicketRepository.existsById(ticketId)) {
            dbTicketRepository.deleteById(ticketId);
            LOGGER.info("Ticket {} cancelled successfully", ticketId);
            return true;
        } else {
            LOGGER.warn("Ticket {} not found for cancellation", ticketId);
            return false;
        }
    }

    private Ticket mapTicketDbToTicket(TicketDb ticketDb) {
        return new TicketImpl(
                ticketDb.getId(),
                ticketDb.getEventId(),
                ticketDb.getUserId(),
                ticketDb.getPlace(),
                Ticket.Category.valueOf(ticketDb.getCategory().name())
        );
    }

    private TicketDb mapTicketToTicketDb(Ticket ticket) {
        return new TicketDb(
                ticket.getId(),
                ticket.getEventId(),
                ticket.getUserId(),
                TicketDb.Category.valueOf(ticket.getCategory().name()),
                ticket.getPlace()
        );
    }
}
