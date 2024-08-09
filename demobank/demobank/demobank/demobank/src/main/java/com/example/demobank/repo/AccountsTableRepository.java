package com.example.demobank.repo;

import com.example.demobank.entity.AccountsTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountsTableRepository extends JpaRepository<AccountsTable, Long> {
}