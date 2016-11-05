package com.kousenit.dao;

import com.kousenit.entities.Officer;
import com.kousenit.entities.Rank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OfficerRepository extends JpaRepository<Officer, Integer> {
    List<Officer> findByRank(@Param("rank") Rank rank);

    Officer findByLast(@Param("last") String last);
}
