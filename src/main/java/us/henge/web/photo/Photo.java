package us.henge.web.photo;

import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import us.henge.service.content.PhotoOpService;
import us.henge.service.content.PhotoService;

@Controller
@RequestMapping("/photos/photo")
public class Photo {
	@Autowired
	private PhotoService photoService = null;
	
	@Autowired
	private PhotoOpService photoOpService = null;
	
	@RequestMapping(value = "/preview/{path:.+}", method = RequestMethod.GET)
	public void getPhotoPreview(@PathVariable String path, HttpServletResponse response) throws Exception{
		File file = photoService.getPreviewFile(path);
		photoOpService.serveResized(response, file, 100);
	}
}
