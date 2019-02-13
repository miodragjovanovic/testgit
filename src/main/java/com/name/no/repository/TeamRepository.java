package com.name.no.repository;

import java.util.List;

import com.name.no.entity.Team;

public interface TeamRepository extends BaseRepository<Team, String> {
	
	List<Team> findAllByOrderByCoefficientDesc();
	
	List<Team> findByGroupName(String groupName);
	
	List<Team> findByGroupNameOrderByCoefficientDesc(String groupName);

}
