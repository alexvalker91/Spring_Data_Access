package ua.epam.mishchenko.ticketbooking.facade.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ua.epam.mishchenko.ticketbooking.model.Event;
import ua.epam.mishchenko.ticketbooking.model.Ticket;
import ua.epam.mishchenko.ticketbooking.model.User;
import ua.epam.mishchenko.ticketbooking.model.UserAccount;
import ua.epam.mishchenko.ticketbooking.model.impl.EventImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserAccountImpl;
import ua.epam.mishchenko.ticketbooking.model.impl.UserImpl;
import ua.epam.mishchenko.ticketbooking.service.UserAccountService;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration({"classpath:/test-applicationContext.xml"})
public class BookingFacadeImplTest {

    @Autowired
    private BookingFacadeImpl bookingFacade;

    @Autowired
    private UserAccountService userAccountService;

    @Before
    public void setUp() {
        UserAccount mockAccount = new UserAccountImpl();
        mockAccount.setUserAmount(1000);

        when(userAccountService.getUserAccountByUserId(anyLong()))
                .thenReturn(mockAccount);

        when(userAccountService.updateUserAccount(any(UserAccount.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void createUserThenCreateEventThenBookTicketForThisEventForUserAndThenCancelItShouldBeOk() {
        User user = new UserImpl("Andrii", "andrii@gmail.com");
        Event event = new EventImpl("Integration Event", new Date(System.currentTimeMillis()), 100);
        int place = 10;

        user = bookingFacade.createUser(user);

        assertNotNull(bookingFacade.getUserById(user.getId()));

        event = bookingFacade.createEvent(event);

        assertNotNull(bookingFacade.getEventById(event.getId()));

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), place, Ticket.Category.STANDARD);

        List<Ticket> bookedTicketsByUserBeforeCanceling = bookingFacade.getBookedTickets(user, 1, 1);
        List<Ticket> bookedTicketsByEventBeforeCanceling = bookingFacade.getBookedTickets(event, 1, 1);

        assertTrue(bookedTicketsByUserBeforeCanceling.contains(ticket));
        assertTrue(bookedTicketsByEventBeforeCanceling.contains(ticket));

        bookingFacade.cancelTicket(ticket.getId());

        List<Ticket> bookedTicketsByUserAfterCanceling = bookingFacade.getBookedTickets(user, 1, 1);
        List<Ticket> bookedTicketsByEventAfterCanceling = bookingFacade.getBookedTickets(event, 1, 1);

        assertNull(bookedTicketsByUserAfterCanceling);
        assertNull(bookedTicketsByEventAfterCanceling);
    }

}