package com.twitter.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.twitter.entity.Message;

@Repository

public interface MessageDao extends JpaRepository<Message, Long> {

//select * from Message where email='modi@gmail.com'  order by message_Id asc limit 2

	@Query(value = "select * from Message msg where msg.email= :email order by msg.message_Id desc limit 10", nativeQuery = true)

	public List<Message> showTop10Messges(@Param("email") String email);

}
