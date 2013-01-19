package com.tangpian.sna.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Activity;
import com.google.api.services.plus.model.ActivityFeed;
import com.google.api.services.plus.model.Comment;
import com.google.api.services.plus.model.CommentFeed;
import com.google.api.services.plus.model.Person;
import com.tangpian.sna.dao.ConfigurationDao;
import com.tangpian.sna.dao.PostDao;
import com.tangpian.sna.dao.RelationDao;
import com.tangpian.sna.dao.ReplyDao;
import com.tangpian.sna.dao.UserDao;
import com.tangpian.sna.dao.UserProcessStatusDao;
import com.tangpian.sna.model.Post;
import com.tangpian.sna.model.Relation;
import com.tangpian.sna.model.Reply;
import com.tangpian.sna.model.User;
import com.tangpian.sna.utils.OauthUtil;

@Service
public class GplusService {
	private static final Logger LOGGER = Logger.getLogger(GplusService.class
			.getName());

	@Autowired
	private PostDao postDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ReplyDao replyDao;

	@Autowired
	private RelationDao relationDao;
	
	@Autowired
	private ConfigurationDao configurationDao;

	@Autowired
	private UserProcessStatusDao userProcessStatusDao;

	private static final long DEFAULT_PAGESIZE = 50L;
	private static final int DEFAULT_DAYS = 30;

	public User getUserProfile(String profileid) {
		User user = new User();
		try {
			Plus.People people = OauthUtil.getServicePlus().people();
			Person person = people.get(profileid).execute();
			user.setGplusUser(person);
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (null == userDao.get(user.getId())) {
			userDao.add(user);
		}

		return user;
	}

	private List<Post> getherPosts(String profileid, Long size, Integer days) {
		if (size == null) {
			size = DEFAULT_PAGESIZE;
		}

		if (days == null) {
			days = DEFAULT_DAYS;
		}

		List<Post> posts = new ArrayList<Post>();

		try {
			Plus.Activities.List listActivities = OauthUtil.getServicePlus()
					.activities().list(profileid, "public");
			long pagesize = 100L;
			if (size < 100L) {
				pagesize = size;
			}
			listActivities.setMaxResults(pagesize);

			// Execute the request for the first page
			ActivityFeed activityFeed = listActivities.execute();
			LOGGER.info("Google Crawl ActivityFeed:"
					+ activityFeed.toPrettyString());

			// Unwrap the request and extract the pieces we want
			List<Activity> activities = activityFeed.getItems();
			LOGGER.info("Google Crawl Activity List:" + activities.size());

			LOGGER.info("Google Crawl Start!");
			// Loop through until we arrive at an empty page
			// while (activities != null) {
			int i = 0;
			outside: while (activities != null) {
				for (Activity activity : activities) {
					LOGGER.info("process no:" + i + "/" + size + ", time:"
							+ activity.getPublished() + "|"
							+ activity.getPublished().getValue() + "("
							+ new Date() + "|" + new Date().getTime() + ")");
					LOGGER.info("process flag:"
							+ (i <= size && ((new Date().getTime() - activity
									.getPublished().getValue()) < (1000L * 60 * 60 * 24 * days))));
					if (i <= size
							&& ((new Date().getTime() - activity.getPublished()
									.getValue()) < (1000L * 60 * 60 * 24 * days))) {
						posts.add(new Post(activity));
						i++;
					} else {
						break outside;
					}
				}

				// We will know we are on the last page when the next page token
				// is null.
				// If this is the case, break.
				if (activityFeed.getNextPageToken() == null) {
					break;
				}

				// Prepare to request the next page of activities
				listActivities.setPageToken(activityFeed.getNextPageToken());

				// Execute and process the next page request
				activityFeed = listActivities.execute();
				activities = activityFeed.getItems();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("Google Crawl IOException:" + e.getMessage());
			return null;
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.info("Google Crawl GeneralSecurityException:"
					+ e.getMessage());
		}
		return posts;
	}

	public List<Reply> getherReplies(String postId) {
		List<Reply> replies = new ArrayList<Reply>();
		try {
			Plus.Comments.List listComments = OauthUtil.getServicePlus()
					.comments().list(postId);
			listComments.setMaxResults(500L);

			CommentFeed commentFeed = listComments.execute();
			List<Comment> comments = commentFeed.getItems();
			
			LOGGER.info("comments size:" + comments.size() );

			// Loop through until we arrive at an empty page
			if (comments != null) {
				for (Comment comment : comments) {
					System.out.println(comment.getActor().getDisplayName()
							+ " commented " + comment.getObject().getContent());
					replies.add(new Reply(comment, postId));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return replies;
	}

	public void addPosts(List<Post> posts) {
		LOGGER.info("Save Google Plus Post");
		postDao.addList(posts);
	}

	public void addReplies(List<Reply> replies) {
		LOGGER.info("Save Google comments");
		replyDao.addList(replies);
	}

	public void addRelations(List<Relation> relations) {
		LOGGER.info("Save Google comments");
		relationDao.addList(relations);
	}

	public void handleRelations() {
		List<String> userIds = userDao.listIds();
		List<Relation> relations = new ArrayList<Relation>();
		List<Post> allPosts = new ArrayList<Post>();
		List<Reply> allReplies = new ArrayList<Reply>();
		for (String profileId : userIds) {
			List<Post> posts = getherPosts(profileId, 200L, 30);
			for (Post post : posts) {
				List<Reply> replies = getherReplies(post.getId());
				for (Reply reply : replies) {
					handleRelation(relations, profileId, reply.getId());
				}
				allReplies.addAll(replies);
			}
			allPosts.addAll(posts);
		}

		LOGGER.info("all User post no:" + allPosts.size());
		addPosts(allPosts);

		LOGGER.info("all user replies no:" + allReplies.size());
		addReplies(allReplies);

		LOGGER.info("all user replies no:" + relations.size());

	}

	private void handleRelation(List<Relation> relations, String source,
			String target) {
		for (Relation relation : relations) {
			if (source.equals(relation.getSource())
					&& target.equals(relation.getTarget())) {
				relations.add(new Relation(source, target,
						relation.getValue() + 1));
				return;
			}
		}
		relations.add(new Relation(source, target, 1));
	}

//	public void gather() {
//		List<String> userIds = userDao.listIds();
//		List<Post> posts = new ArrayList<Post>();
//		for (String id : userIds) {
//			posts.addAll(getherPosts(id, 100L, 30));
//		}
//		LOGGER.info("all User post no:" + posts.size());
//		addPosts(posts);
//
//		List<Reply> replies = new ArrayList<Reply>();
//		for (Post post : posts) {
//			replies.addAll(getherReplies(post.getId()));
//		}
//		LOGGER.info("all user replies no:" + replies.size());
//
//		addReplies(replies);
//	}
	
	public void gatherPost() {
		List<String> doneUserIds = configurationDao.list("doneUserIds");
		List<String> userIds = userDao.listIds();
		List<Post> posts = new ArrayList<Post>();
		for (String id : userIds) {
			if (!doneUserIds.contains(id)) {
				posts.addAll(getherPosts(id, 500L, 30));
				configurationDao.add("doneUserIds", id);
				break;
			}
		}
		LOGGER.info("all User post no:" + posts.size());
		addPosts(posts);

	}
	
	public void gatherReply() {
		List<String> donePostIds = configurationDao.list("donePostIds");
		List<Reply> replies = new ArrayList<Reply>();
		List<String> postIds = postDao.listIds();
		for (String postId : postIds) {
			if (!donePostIds.contains(postId)) {
				replies.addAll(getherReplies(postId));
				configurationDao.add("donePostIds", postId);
				break;
			}
		}
		LOGGER.info("all user replies no:" + replies.size());
		addReplies(replies);

	}
	
	// public void gather() {
	// LOGGER.info("start gather!");
	// List<String> userIds = userDao.listIds();
	// List<Post> posts = processUserStatus(userIds);
	//
	// LOGGER.info("all User post no:" + posts.size());
	// addPosts(posts);
	//
	// List<Reply> replies = new ArrayList<Reply>();
	// for (Post post : posts) {
	// replies.addAll(getherReplies(post.getId()));
	// }
	// LOGGER.info("all user replies no:" + replies.size());
	//
	// addReplies(replies);
	// }
	//
	// private List<Post> processUserStatus(List<String> ids) {
	// List<Post> posts = new ArrayList<Post>();
	// List<UserProcessStatus> userProcessStatuses = userProcessStatusDao
	// .list();
	// for (String id : ids) {
	// if (needProcess(id, userProcessStatuses)) {
	// posts.addAll(getherPosts(id, 200L, 30));
	// LOGGER.info("process post size" + posts.size());
	// return posts;
	// }
	// }
	// return posts;
	// }
	//
	// private boolean needProcess(String id,
	// List<UserProcessStatus> userProcessStatuses) {
	// boolean needProcess = true;
	// for (UserProcessStatus userProcessStatus : userProcessStatuses) {
	// if (id.equals(userProcessStatus.getUid())
	// && userProcessStatus.getLastProcessTime() != null
	// && ((new Date().getTime() - userProcessStatus
	// .getLastProcessTime().getTime()) < 15 * 60 * 1000)) {
	// needProcess = false;
	// }
	// }
	// return needProcess;
	// }

	public List<Post> listLastestPosts(String profileid) {
		return getherPosts(profileid, null, null);
	}
	
	public List<Post> listAndUpdateLastestPosts(String profileid) {
		List<Post> posts =  getherPosts(profileid, null, null);
		postDao.addList(posts);
		return posts;
	}

	
	public static void main(String[] args) {
		GplusService gplusService = new GplusService();
		List<Reply> replies = gplusService.getherReplies("z13hithzftbnurqnx232ipixuvqffqsc");
		for (Reply reply : replies) {
			System.out.println(reply.getContent());
			System.out.println(reply.getUserId());
			System.out.println();
		}
	}
}
