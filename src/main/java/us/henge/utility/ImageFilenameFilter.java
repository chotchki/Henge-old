package us.henge.utility;

import java.io.File;
import java.io.FilenameFilter;

public class ImageFilenameFilter implements FilenameFilter {
	private String[] extensions = new String[]{
		".gif",
		".jpg",
		".jpeg",
		".png"
	};
	@Override
	public boolean accept(File dir, String name) {
		for(String ext : extensions){
			if(name.endsWith(ext)){
				return true;
			}
		}
		return false;
	}
}
