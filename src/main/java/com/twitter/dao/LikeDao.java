package com.twitter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.twitter.entity.LikeTweet;

@Repository

public interface LikeDao extends JpaRepository<LikeTweet, Long> {

	@Query(value = " select l from LikeTweet l join l.message msg where msg.messageId = :messageId ")

	public List<LikeTweet> numberOfLikesByMessage(@Param("messageId") Integer messageId);

}
