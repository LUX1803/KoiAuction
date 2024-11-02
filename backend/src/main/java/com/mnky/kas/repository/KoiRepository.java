package com.mnky.kas.repository;

import com.mnky.kas.dto.response.KoiResponse;
import com.mnky.kas.model.Gender;
import com.mnky.kas.model.Koi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface KoiRepository extends JpaRepository<Koi, Short> {
    Koi findById(short id);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO koi (yob, length, gender, image, message, variety_id, farm_id) " +
            "VALUES (?1, ?2, ?3, ?4, ?5, ?6, ?7)", nativeQuery = true)
    int saveKoi(short yob, short length, Gender gender, String image, String message, short varietyId, short farmId);

    @Modifying
    @Query("UPDATE Koi k SET k.yob = :yob, k.length = :length, k.gender = :gender, k.image = :image, k.status = :status WHERE k.id = :id")
    int updateKoi(@Param("id") short id,
                        @Param("yob") short yob,
                        @Param("length") short length,
                        @Param("gender") Gender gender,
                        @Param("image") String image,
                        @Param("status") Koi.KoiStatus status);

    List<Koi> findAllByFarmId(short farmId);

    List<Koi> findAllByStatus(Koi.KoiStatus status);

    List<Koi> findAllByVarietyId(short varietyId);

    Koi findByFarmIdAndId(short farmId, short koiId);


}
