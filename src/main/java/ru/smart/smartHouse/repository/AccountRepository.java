package ru.smart.smartHouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.smart.smartHouse.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByName(String name);
}
