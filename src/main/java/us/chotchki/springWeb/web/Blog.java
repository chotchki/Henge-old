package us.chotchki.springWeb.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import us.chotchki.springWeb.db.dao.PostsDao;
import us.chotchki.springWeb.db.pojo.Post;
import us.chotchki.springWeb.service.Markdown;



@Controller
@RequestMapping(value = "/blog")
public class Blog {
	@Autowired
	private Markdown markdown = null;
	
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

	@RequestMapping(value = "/new")
	public String newPost() {
		return "postAuthoring";
	}
	
	@RequestMapping(value = "/preview", method = RequestMethod.POST)
	public ResponseEntity<String> preview(@Valid Post post, BindingResult rPost){
		if(rPost.hasErrors()) {
			return new ResponseEntity<String>(rPost.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(markdown.formatPost(post), HttpStatus.OK);
	}
}
