package us.henge.service.pojo;

import java.io.File;

public class PathDetails {
	private String name = null;
	private boolean file = false; //Files or directories only!
	private String path = null;
	
	public PathDetails(File path){
		name = path.getName();
		file = path.isFile();
		this.path = path.getPath();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isFile() {
		return file;
	}
	public void setFile(boolean file) {
		this.file = file;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
}
