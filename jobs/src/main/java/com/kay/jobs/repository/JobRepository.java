package com.kay.jobs.repository;

import com.kay.jobs.model.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job,Long> {
    Page<Job> findAllByCompanyId(Long companyId, Pageable pageable);

    @Query("select j from Job j WHERE j.title like %?1%  OR j.location like %?2% OR j.contractType like %?3%")
    List<Job> searchJobByKeywords(String title, String location, String contractType);

}
