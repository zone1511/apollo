package com.ctrip.framework.apollo.portal.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ctrip.framework.apollo.portal.entity.po.AuthorityPO;

public interface AuthorityRepository extends PagingAndSortingRepository<AuthorityPO, Long> {
	AuthorityPO findByUsername(String username);
}
