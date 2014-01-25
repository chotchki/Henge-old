package us.henge.web.photo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/photos")
public class Photos {
	
	@RequestMapping
	public String index() {
		return "photos/photos";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newAlbumEditor(Model mod) {
		return "photos/edit";
	}
}
