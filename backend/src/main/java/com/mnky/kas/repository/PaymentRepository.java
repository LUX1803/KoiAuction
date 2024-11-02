package com.mnky.kas.repository;

import com.mnky.kas.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Short> {

}
