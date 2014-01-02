package us.chotchki.springWeb.web.photo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/photos")
public class Photos {
	
	@RequestMapping
	public String index() {
		return "photos/photos";
	}
}
