package us.henge.web.pojo;

import org.joda.time.DateTime;

public class AddPreview {
	private String name = null;
	private boolean duplicate = false;
	private DateTime date = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isDuplicate() {
		return duplicate;
	}
	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
}
