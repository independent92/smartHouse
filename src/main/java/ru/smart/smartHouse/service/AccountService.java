package ru.smart.smartHouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.smart.smartHouse.entity.Account;
import ru.smart.smartHouse.repository.AccountRepository;
import ru.smart.smartHouse.repository.RoleRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
@Transactional
public class AccountService {
    @Autowired
    private AccountRepository repository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Account save(Account account) {
        account.setPassword(encoder.encode(account.getPassword()));
        account.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole("user"))));

        return repository.save(account);
    }
}
