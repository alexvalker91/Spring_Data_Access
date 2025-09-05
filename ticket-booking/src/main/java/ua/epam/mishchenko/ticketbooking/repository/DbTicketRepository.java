package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.entity.TicketDb;

public interface DbTicketRepository extends CrudRepository<TicketDb, Long> {

    Page<TicketDb> findAllByUserId(long userId, Pageable pageable);

    Page<TicketDb> findAllByEventId(long eventId, Pageable pageable);

    boolean existsByEventIdAndPlace(long eventId, int place);
}
