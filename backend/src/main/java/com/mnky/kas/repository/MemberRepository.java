package com.mnky.kas.repository;

import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Short> {
    Member findByUsername(String username);

    Member findById(short id);

    boolean existsByUsername(String username);

    @Modifying
    @Query(value = "INSERT INTO member (username, password, email, phone, address, " +
                                        "firstname, lastname, gender) " +
                   "VALUES (:username, :password, :email, :phone, :address, " +
                            ":firstname, :lastname, :gender)", nativeQuery = true)
    void saveMember(@Param("username") String username, @Param("password") String password,
                    @Param("email") String email, @Param("phone") String phone,
                    @Param("address") String address, @Param("firstname") String firstname,
                    @Param("lastname") String lastname, @Param("gender") String gender);
}
