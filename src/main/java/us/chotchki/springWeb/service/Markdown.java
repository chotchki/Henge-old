package us.chotchki.springWeb.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.chotchki.springWeb.db.pojo.Post;

@Service
public class Markdown {
	@Autowired
	private TimeFormatter time = null;
	
	private PegDownProcessor pegDown = new PegDownProcessor(Extensions.ALL);
	
	public synchronized String toHTML(String content) {
		return pegDown.markdownToHtml(content);
	}
	
	public String formatPost(Post post) {
		StringBuilder markedUp = new StringBuilder();
		
		String postUrl = "/blog/post/";
		BigDecimal id = post.getId();
		if(id == null) {
			postUrl = "#";
		} else {
			postUrl += id;
		}
		
		String postTitle = post.getTitle();
		if(postTitle.isEmpty() || postTitle.matches("^[\\s]*$")) {
			postTitle = "No Title!";
		}
		
		markedUp.append("[" + postTitle + "](" + postUrl + ")\n");
		markedUp.append("========\n");
		
		DateTime postDate = post.getPublished();
		if(postDate == null) {
			postDate = new DateTime();
		}
		markedUp.append("*" + time.format(postDate) + "*\n");
		markedUp.append("<hr />\n");
		markedUp.append(post.getContent());
		
		return this.toHTML(markedUp.toString());
	}
	
	public List<String> formatPosts(List<Post> posts){
		List<String> formatted = new ArrayList<String>(posts.size());
		for(Post post: posts){
			formatted.add(this.formatPost(post));
		}
		return formatted;
	}
}
