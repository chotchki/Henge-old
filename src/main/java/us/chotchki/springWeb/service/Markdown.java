package us.chotchki.springWeb.service;

import java.math.BigDecimal;

import org.joda.time.Instant;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import us.chotchki.springWeb.db.pojo.Post;

@Service
public class Markdown {
	@Autowired
	private Time time = null;
	
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
		
		Instant postDate = post.getPublished();
		if(postDate == null) {
			postDate = new Instant();
		}
		markedUp.append("*" + time.format(postDate) + "*\n");
		markedUp.append("<hr />");
		markedUp.append(post.getContent());
		
		return this.toHTML(markedUp.toString());
	}
}
