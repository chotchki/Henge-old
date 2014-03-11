package us.henge.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import us.henge.init.Config.Keys;
import us.henge.utility.ImageFilenameFilter;

@Service
public class PhotoService implements ServletContextAware {
	private static final Logger log = LoggerFactory.getLogger(PhotoService.class);
	
	private File uploadFolder = null;
	
	@Override
	public void setServletContext(ServletContext sc) {
		String parentPath = sc.getInitParameter(Keys.uploadFolder.toString());
		
		if(parentPath == null){
			log.info("Upload folder not set, the upload folder will error out.");
			return;
		}
		
		File parentFile = new File(parentPath);
		if(!parentFile.isDirectory()){
			log.error("Upload folder is not a directory! The upload folder will error out.");
		}
		
		uploadFolder = parentFile;
	}
	
	public List<String> listFolder(){
		List<String> fd = new ArrayList<String>();
		
		File[] pictures = uploadFolder.listFiles(new ImageFilenameFilter());
		if(pictures != null){
			for(File pic : pictures){
				fd.add(pic.getName());
			}
		}
		
		return fd;
	}
	
	public File getFile(String fileName){
		return new File(uploadFolder.getPath() + File.separator + fileName);
	}
}
