package com.example.demobank.repo;

import com.example.demobank.entity.UsersTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersTableRepository extends JpaRepository<UsersTable, Long> {
}
