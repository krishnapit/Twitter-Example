package com.twitter.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.twitter.entity.LikeTweet;
import com.twitter.entity.Message;
import com.twitter.entity.TwitterFollower;
import com.twitter.entity.User;
import com.twitter.exception.TwitterException;
import com.twitter.service.TwitterService;

@RestController
@RequestMapping("/twitter")
public class TwitterController {
	@Autowired
	private TwitterService twitterService;

	/**
	 *
	 * User can follow other users given a twitter ID.
	 *
	 * @param user
	 * @return
	 *
	 */
	@PostMapping(path = "/follow/{twitterId}")
	public ResponseEntity<String> followAUser(@PathVariable("twitterId") String twitterId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		System.err.println("TwitterController.followAUser()  loged in email" + email);
		// check twitter id is exist or not
		User userInfo = twitterService.userInfo(twitterId);
		Optional<User> checkUser = Optional.ofNullable(userInfo);
		if (!checkUser.isPresent()) {
			throw new TwitterException("Follower Id is not exists ");
		}
		final User userDetails = twitterService.userInfo(email);
		TwitterFollower follower = new TwitterFollower();
		follower.setTwitterId(twitterId);
		follower.setUser(userDetails);
		try {
			twitterService.followingUser(follower);
		} catch (TwitterException e) {
			throw new TwitterException(e.getMessage());
		}
		return new ResponseEntity<String>("Now user is following " + twitterId, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/followers", method = RequestMethod.GET)
	public ResponseEntity<List<User>> followers() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		List<TwitterFollower> followers = twitterService.followers(email);
		List<User> listOfFollowers = getFollowerNames(followers);
		boolean flag = listOfFollowers.isEmpty();
		if (flag) {
			throw new TwitterException("No followers ");
		}
		return new ResponseEntity<List<User>>(listOfFollowers, HttpStatus.OK);
	}

	private List<User> getFollowerNames(List<TwitterFollower> followers) {
		List<User> followersList = new ArrayList<User>();
		for (TwitterFollower p : followers) {
			followersList.add(p.getUser());
		}
		return followersList;
	}

	/**
	 * This method is for post tweet
	 *
	 * @param message
	 * @return
	 */
	@PostMapping("/tweet")
	public ResponseEntity<String> postTweet(@RequestBody @Valid Message message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		try {
			User user = twitterService.userInfo(email);
			message.setUser(user);
			twitterService.postTweet(message);
		} catch (TwitterException e) {
			throw new TwitterException("Problem in posting tweet");
		}
		return new ResponseEntity<String>("Message is posted successfully ", HttpStatus.OK);
	}

	/**
	 * Show top 10 posts
	 *
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/home")
	public ResponseEntity<List<String>> showTweets() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		List<Message> messageList = twitterService.showTweets(email);
		List<String> list = messageList.stream().map(msg -> msg.getMessage()).collect(Collectors.toList());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@PostMapping("/like")
	public ResponseEntity<String> likeTweet(@RequestBody LikeTweet like) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		try {
			User user = twitterService.userInfo(email);
			like.setUser(user);
			twitterService.likeTweet(like);
		} catch (TwitterException e) {
			throw new TwitterException("Problem in liking tweet");
		}

		// Message msg =
		// twitterService.getTwitterIdByMessageId(like.getMessage().getMessageId());

		return new ResponseEntity<String>(email + " likes the tweet posted  ", HttpStatus.OK);
	}

	@GetMapping(value = "/numberOfLikes")
	public Long numberOfLikes(Message message) {
		System.err.println("entered ?");
		return twitterService.numberOfLikes(message);
	}
}
