package ua.epam.mishchenko.ticketbooking;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.epam.mishchenko.ticketbooking.facade.BookingFacade;
import ua.epam.mishchenko.ticketbooking.model.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyApplication {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        BookingFacade bookingFacade = context.getBean("bookingFacade", BookingFacade.class);
        System.out.println("Hello World");

        Event event = bookingFacade.getEventById(1);
        System.out.println("Title Kurami: " + event.getTitle());
        event.setTitle("New Title Test");
        bookingFacade.updateEvent(event);

        String dateString = "2023-10-05 17:00:00";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateString);
            int pageSize = 10;
            int pageNum = 0;
            List<Event> events = bookingFacade.getEventsForDay(date, pageSize, pageNum);
            System.out.println("Hello World " + events.size());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
