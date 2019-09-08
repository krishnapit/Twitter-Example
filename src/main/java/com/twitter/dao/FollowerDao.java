package com.twitter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.twitter.entity.TwitterFollower;

@Repository

public interface FollowerDao extends JpaRepository<TwitterFollower, Long> {

	public List<TwitterFollower> findByTwitterId(String twitterId);

	public List<TwitterFollower> findByUserEmail(String email);

}