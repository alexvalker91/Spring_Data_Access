package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.entity.UserDb;

public interface DbUserRepository extends CrudRepository<UserDb, Long> {
}
