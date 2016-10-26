package com.kousenit.dao;

import com.kousenit.entities.Officer;

import java.util.Collection;

public interface OfficerDAO {
    Officer save(Officer officer);
    Officer findOne(Integer id);
    Collection<Officer> findAll();
    Long count();
    void delete(Officer officer);
    boolean exists(Integer id);
}
