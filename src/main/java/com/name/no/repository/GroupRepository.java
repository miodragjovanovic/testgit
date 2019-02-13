package com.name.no.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.name.no.entity.Group;

public interface GroupRepository extends BaseRepository<Group, String> {
	
	Group findByName(String name);
	
	@Query("select count(distinct g.name) " +
			"from Team as t " +
			"inner join t.group as g " +  
			"on g.name = t.group " +
			"where t.country = :country")
	int countGroupsWithCountry(@Param("country") String country);
	
//	@Query("select name " +
//			"from Group")
//	String countGroupsWithoutTeam(@Param("country") String country);
	
}
