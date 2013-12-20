package us.chotchki.springWeb.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Index {	
	@RequestMapping(value = "/")
	public String index() {
		return "forward:/blog/page/0";
	}
}
