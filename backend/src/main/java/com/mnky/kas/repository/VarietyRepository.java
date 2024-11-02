package com.mnky.kas.repository;

import com.mnky.kas.model.Variety;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VarietyRepository extends JpaRepository<Variety, Short> {
    Variety findById(short id);

    List<Variety> findAll();
}
