package us.henge.service;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.stereotype.Service;
import org.springframework.web.context.ServletContextAware;

import us.henge.init.Config.Keys;

@Service
public class InitParamService implements ServletContextAware {
	private File dataDirectory = null;
	private File uploadDirectory = null;

	@Override
	public void setServletContext(ServletContext sc) {
		dataDirectory = new File(sc.getInitParameter(Keys.dataPath.toString()));
		uploadDirectory = new File(sc.getInitParameter(Keys.uploadFolder.toString()));
	}

	public File getDataDirectory() {
		return dataDirectory;
	}

	public File getUploadDirectory() {
		return uploadDirectory;
	}
}
