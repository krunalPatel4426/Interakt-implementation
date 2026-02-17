package com.implintrakt.Impl.interakt.repository;

import com.implintrakt.Impl.interakt.model.ContactDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactDetailRepository extends JpaRepository<ContactDetailEntity, Long> {
    List<ContactDetailEntity> findByStatus(String status);

    @Query(nativeQuery = true, value = """
    SELECT DISTINCT cd.status from contact_details cd;
""")
    List<String> getStatus();
}
