package com.twitter.service;

import java.util.List;

import com.twitter.entity.Message;
import com.twitter.entity.TwitterFollower;
import com.twitter.entity.User;

public interface TwitterService {

	public void followingUser(TwitterFollower follow);

	public List<TwitterFollower> followers(String email);

	public Message postTweet(Message message);

	public List<Message> showTweets(String email);

	public User userInfo(String email);
}
