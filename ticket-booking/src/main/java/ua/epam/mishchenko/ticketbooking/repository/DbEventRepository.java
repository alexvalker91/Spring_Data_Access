package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.epam.mishchenko.ticketbooking.entity.EventDb;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface DbEventRepository extends CrudRepository<EventDb, Long> {

    Page<EventDb> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT e FROM EventDb e WHERE e.date BETWEEN :start AND :end")
    Page<EventDb> findByDateBetween(@Param("start") Date start,
                                    @Param("end") Date end,
                                    Pageable pageable);
}
