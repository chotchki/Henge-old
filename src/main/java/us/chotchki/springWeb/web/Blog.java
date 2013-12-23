package us.chotchki.springWeb.web;

import javax.validation.Valid;

import org.joda.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.chotchki.springWeb.db.pojo.Post;
import us.chotchki.springWeb.db.service.PostsService;
import us.chotchki.springWeb.service.Markdown;


@Controller
@RequestMapping(value = "/blog")
public class Blog {
	private static final Logger log = LoggerFactory.getLogger(Blog.class);
	
	@Autowired
	private Markdown markdown = null;
	
	@Autowired
	private PostsService postsService = null;
	
	@RequestMapping
	public String index() {
		return "redirect:/blog/page/0";
	}
	
	@RequestMapping(value = "/page/{number}")
	public String showPage(@PathVariable int number, Model mod) {
		mod.addAttribute("posts", postsService.getPostsByPage(number));
		return "index";
	}
	
	@RequestMapping(value = "/post/{number}")
	public String showPost(@PathVariable int number, Model mod) {
		mod.addAttribute("posts", postsService.getPostById(number));
		return "index";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newPostEditor(Model mod) {
		mod.addAttribute("post", new Post());
		return "postAuthoring";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newPostHandler(@Valid Post post, BindingResult rPost, RedirectAttributes redirectAttributes) {
		if(rPost.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rPost.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("post", post);
			return "redirect:/blog/new";
		}
		
		try {
			post.setPublished(new Instant());
			int id = postsService.create(post);
			log.debug("id is {}", id);
		} catch (Exception e){
			log.error("Had an error creating a new post", e);
			redirectAttributes.addFlashAttribute("error", "Had an error creating a new post.");
			redirectAttributes.addFlashAttribute("post", post);
			return "redirect:/blog/new";
		}
		
		redirectAttributes.addFlashAttribute("success", "Created post " + post.getTitle());
		return "redirect:/blog/post/" + post.getId();
	}
	
	@RequestMapping(value = "/preview", method = RequestMethod.POST)
	public ResponseEntity<String> preview(@Valid Post post, BindingResult rPost){
		if(rPost.hasErrors()) {
			return new ResponseEntity<String>(rPost.getFieldError().getDefaultMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<String>(markdown.formatPost(post), HttpStatus.OK);
	}
}
