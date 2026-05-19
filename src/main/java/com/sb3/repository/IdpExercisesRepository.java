package com.sb3.repository;

import com.sb3.entity.idp.IdpExercises;
import com.sb3.entity.idp.IdpGeneralInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdpExercisesRepository extends JpaRepository<IdpExercises, Long> {
    List<IdpExercises> findByIdpGeneralInfoId(Long idpGeneralInfo_id);
}
