package com.sb3.repository;

import com.sb3.entity.idp.IdpGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdpGeneralInfoRepository extends JpaRepository<IdpGeneralInfo, Long> {
    List<IdpGeneralInfo> findByStudentIdOrderByVersionDesc(Long studentId);
}
