package ua.epam.mishchenko.ticketbooking.facade.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.service.EventService;
import ua.epam.mishchenko.ticketbooking.service.TicketService;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;
import ua.epam.mishchenko.ticketbooking.service.UserService;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookingFacadeImplUnitTest {

    private BookingFacadeImpl bookingFacade;

    @Mock
    private EventService eventService;

    @Mock
    private TicketService ticketService;

    @Mock
    private UserService userService;

    @Mock
    private UserAccountService userAccountService;

    @Before
    public void setUp() {
        bookingFacade = new BookingFacadeImpl(eventService, ticketService, userService, userAccountService);
    }

    @Test
    public void bookTicketWithSufficientFundsReturnsTicket() {
        long userId = 1L;
        long eventId = 10L;
        int place = 5;

        Event event = new EventImpl("Unit Event", new Date(System.currentTimeMillis()), 100);
        UserAccount account = new UserAccountImpl();
        account.setUserAmount(150);

        when(eventService.getEventById(eventId)).thenReturn(event);
        when(userAccountService.getUserAccountByUserId(userId)).thenReturn(account);
        when(userAccountService.updateUserAccount(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(ticketService.bookTicket(anyLong(), anyLong(), anyInt(), any())).thenAnswer(invocation -> {
            Ticket.Category category = invocation.getArgument(3);
            return new ua.epam.mishchenko.ticketbooking.model.impl.TicketImpl(userId, eventId, place, category);
        });

        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.PREMIUM);

        assertNotNull(ticket);
    }

    @Test
    public void bookTicketWithInsufficientFundsReturnsNull() {
        long userId = 2L;
        long eventId = 20L;
        int place = 15;

        Event event = new EventImpl("Unit Event 2", new Date(System.currentTimeMillis()), 200);
        UserAccount account = new UserAccountImpl();
        account.setUserAmount(50);

        when(eventService.getEventById(eventId)).thenReturn(event);
        when(userAccountService.getUserAccountByUserId(userId)).thenReturn(account);

        Ticket ticket = bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.STANDARD);

        assertNull(ticket);
    }
}
