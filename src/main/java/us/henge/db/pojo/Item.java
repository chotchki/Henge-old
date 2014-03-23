package us.henge.db.pojo;

import java.math.BigDecimal;

import org.joda.time.DateTime;

public class Item {
	private BigDecimal id = null;
	private BigDecimal parentId = null;
	private BigDecimal defaultId = null;
	private String name = null;
	private DateTime date = new DateTime();
	private String mimeType = null;
	private byte[] hash = null;
	private ItemType itemType = null;
	
	public BigDecimal getId() {
		return id;
	}
	public void setId(BigDecimal id) {
		this.id = id;
	}
	public BigDecimal getParentId() {
		return parentId;
	}
	public void setParentId(BigDecimal parentId) {
		this.parentId = parentId;
	}
	public BigDecimal getDefaultId() {
		return defaultId;
	}
	public void setDefaultId(BigDecimal defaultId) {
		this.defaultId = defaultId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DateTime getDate() {
		return date;
	}
	public void setDate(DateTime date) {
		this.date = date;
	}
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public byte[] getHash() {
		return hash;
	}
	public void setHash(byte[] hash) {
		this.hash = hash;
	}
	public ItemType getItemType() {
		return itemType;
	}
	public void setItemType(ItemType itemType) {
		this.itemType = itemType;
	}
}