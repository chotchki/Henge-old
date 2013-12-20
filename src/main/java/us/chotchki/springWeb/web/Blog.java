package us.chotchki.springWeb.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import us.chotchki.springWeb.db.dao.PostsDao;

@Controller
@RequestMapping(value = "/blog")
public class Blog {
	@Autowired
	private PostsDao postsDao = null;
	
	@RequestMapping
	public String index() {
		return "redirect:/blog/page/0";
	}
	
	@RequestMapping(value = "/page/{number}")
	public String showPage(@PathVariable int number, Model mod) {
		mod.addAttribute("posts", postsDao.getPostsByPage(number));
		return "index";
	}
}
