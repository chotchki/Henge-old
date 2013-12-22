package us.chotchki.springWeb.db.pojo;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.Instant;

public class Post {
	private int id = 0;
	private Instant published = null;
	
	@NotEmpty
	private String title = null;
	
	@NotEmpty
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