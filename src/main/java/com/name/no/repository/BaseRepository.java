package com.name.no.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface BaseRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
 
    void delete(T deleted);
 
//    List<T> findAll();
     
//    Optional<T> findOne(ID id);
// 
//    T save(T persisted);
    
}