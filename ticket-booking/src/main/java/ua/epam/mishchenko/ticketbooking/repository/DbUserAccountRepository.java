package ua.epam.mishchenko.ticketbooking.repository;

import org.springframework.data.repository.CrudRepository;
import ua.epam.mishchenko.ticketbooking.entity.UserAccountDb;

public interface DbUserAccountRepository extends CrudRepository<UserAccountDb, Long> {
}
