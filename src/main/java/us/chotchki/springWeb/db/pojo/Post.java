package us.chotchki.springWeb.db.pojo;

import javax.validation.constraints.NotNull;

import org.joda.time.Instant;

public class Post {
	private int id = -1;
	private Instant published = null;
	
	@NotNull
	private String title = null;
	
	@NotNull
	private String content = null;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Instant getPublished() {
		return published;
	}
	public void setPublished(Instant published) {
		this.published = published;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}