package com.pbu.agents.repositories;

import com.pbu.agents.models.Telecom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TelecomRepository extends JpaRepository<Telecom, Long> {
    boolean existsById(long id);
    boolean existsByTelecomName(String telecomName);
    boolean existsByTelecomNameAndIdNot(String telecomName, long id);

    Telecom findById(long id);
    Telecom findByTelecomName(@Param("name") String name);

    @Query("SELECT t.id, t.telecomName, t.isDeleted FROM Telecom t WHERE t.isDeleted=false")
    List<Telecom> findActiveTelecoms();

    @Transactional
    @Modifying
    @Query("UPDATE Telecom t SET t.isDeleted = :status WHERE t.id = :id")
    void markAsDeleted(@Param("id") long id, @Param("status") boolean status);

    @Transactional
    @Modifying
    @Query("UPDATE Telecom t SET t.telecomName = :#{#telecom.telecomName}, t.isDeleted = :#{#telecom.isDeleted} WHERE t.id = :#{#telecom.id}")
    void updateTelecom(@Param("telecom") Telecom telecom);
}
