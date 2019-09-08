package com.twitter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.twitter.dao.FollowerDao;
import com.twitter.dao.MessageDao;
import com.twitter.dao.TwitterDao;
import com.twitter.entity.Message;
import com.twitter.entity.TwitterFollower;
import com.twitter.entity.User;
import com.twitter.exception.TwitterException;

@Service
public class TwitterServiceImpl implements TwitterService {

	@Autowired
	public TwitterDao twitterRepository;
	@Autowired
	public FollowerDao followerRepository;
	@Autowired
	public MessageDao messageRepository;

	@Override
	public void followingUser(TwitterFollower follow) {

		// check Logged user who likes others
		List<TwitterFollower> followUser = followerRepository.findByUserEmail(follow.getUser().getEmail());

		Optional<TwitterFollower> checkFollower = followUser.stream()
				.filter(twt -> twt.getTwitterId().equals(follow.getTwitterId())).findAny();
		if (checkFollower.isPresent()) {
			throw new TwitterException("USER is already Following  " + checkFollower.get().getTwitterId());
		} else {
			followerRepository.save(follow);
		}
	}

	@Override
	public List<TwitterFollower> followers(String email) {
		// List<TwitterFollower> followersList =
		// followerRepository.findByUserEmail(email);
		List<TwitterFollower> followersList = followerRepository.findByTwitterId(email);
		return followersList;
	}

	@Override
	public Message postTweet(Message message) {
		Message persistedMessage = messageRepository.save(message);
		return persistedMessage;
	}

	@Override
	public List<Message> showTweets(String email) {
		// List<Message> messageList = messageRepository.findAll();
		List<Message> messageList = messageRepository.showTop10Messges(email);
		return messageList;
	}

	@Override
	public User userInfo(String email) throws UsernameNotFoundException {
		User user = twitterRepository.findByEmail(email);
		// if (user == null) {
		// throw new UsernameNotFoundException("User not found with email address: " +

		// return user;
		// }
		return user;
	}
}
