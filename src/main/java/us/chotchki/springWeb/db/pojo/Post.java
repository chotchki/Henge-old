package us.chotchki.springWeb.db.pojo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.joda.time.DateTime;

public class Post {
	private BigDecimal id = null;
	
	@NotNull
	private DateTime published = new DateTime();
	
	@NotNull
	private String title = null;
	
	@NotNull
	private String content = null;
	
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public DateTime getPublished() {
		return published;
	}
	public void setPublished(DateTime published) {
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