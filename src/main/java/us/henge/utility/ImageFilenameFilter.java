package us.henge.utility;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Locale;

public class ImageFilenameFilter implements FilenameFilter {
	private String[] extensions = new String[]{
		".gif",
		".jpg",
		".jpeg",
		".png"
	};
	@Override
	public boolean accept(File dir, String name) {
		String lowerName = name.toLowerCase(Locale.US);
		for(String ext : extensions){
			if(lowerName.endsWith(ext)){
				return true;
			}
		}
		return false;
	}
}
