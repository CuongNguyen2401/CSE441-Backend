package com.cuongnguyen.cse441.repository;

import com.cuongnguyen.cse441.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findByTransactionId(String transactionId);
}
