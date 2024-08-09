package com.example.demobank.repo;

import com.example.demobank.entity.TransactionsTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionsTableRepository extends JpaRepository<TransactionsTable, Long> {
    @Query("SELECT t FROM TransactionsTable t WHERE t.account.accountId = :accountId")
    List<TransactionsTable> getByAccountId(@Param("accountId") Long accountId);


}
