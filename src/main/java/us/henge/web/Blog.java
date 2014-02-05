package us.henge.web;

import java.util.List;

import javax.validation.Valid;

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

import us.henge.db.pojo.Post;
import us.henge.db.service.PostsService;
import us.henge.service.Markdown;


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
		return "redirect:/blog/page/1";
	}
	
	@RequestMapping(value = "/page/{number}")
	public String showPage(@PathVariable int number, Model mod) {
		List<Post> posts = postsService.getPostsByPage(number);
		mod.addAttribute("posts", markdown.formatPosts(posts));
		
		mod.addAttribute("currentPage", number);
		mod.addAttribute("totalPages", postsService.getPageCount());
		return "blog/page";
	}
	
	@RequestMapping(value = "/post/{number}")
	public String showPost(@PathVariable int number, Model mod) {
		Post post = postsService.getPostById(number);
		
		mod.addAttribute("postMarkdown", markdown.formatPost(post));
		mod.addAttribute("post", post);
		
		return "blog/post";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newPostEditor(Model mod) {
		mod.addAttribute("post", new Post());
		return "blog/edit";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newPostHandler(@Valid Post post, BindingResult rPost, RedirectAttributes redirectAttributes) {
		if(rPost.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rPost.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("post", post);
			return "redirect:/blog/new";
		}
		
		try {
			postsService.create(post);
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
	
	@RequestMapping(value = "/post/{number}/edit", method = RequestMethod.GET)
	public String editPost(@PathVariable int number, Model mod) {
		Post post = postsService.getPostById(number);
		mod.addAttribute("post", post);
		
		return "blog/edit";
	}
	
	@RequestMapping(value = "/post/{number}/edit", method = RequestMethod.POST)
	public String editPostHandler(@PathVariable int number, @Valid Post post, BindingResult rPost, RedirectAttributes redirectAttributes) {
		if(rPost.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rPost.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("post", post);
			return "redirect:/blog/post/" + number + "/edit";
		}
		
		try {
			Post oldPost = postsService.getPostById(number);
			oldPost.setTitle(post.getTitle());
			oldPost.setContent(post.getContent());
			oldPost.setPublished(post.getPublished());
			
			postsService.update(oldPost);
		} catch (Exception e){
			log.error("Had an error creating a new post", e);
			redirectAttributes.addFlashAttribute("error", "Had an error updating the post.");
			redirectAttributes.addFlashAttribute("post", post);
			return "redirect:/blog/new";
		}
		
		redirectAttributes.addFlashAttribute("success", "Updated post " + post.getTitle());
		return "redirect:/blog/post/" + number;
	}
	
	@RequestMapping(value = "/post/{number}/delete", method = RequestMethod.POST)
	public String editPostHandler(@PathVariable int number, RedirectAttributes redirectAttributes) {
		try {
			postsService.delete(number);
		} catch (Exception e){
			log.error("Had an error deleting post", e);
			redirectAttributes.addFlashAttribute("error", "Had an error deleting the post.");
			return "redirect:/blog/post" + number;
		}
		
		redirectAttributes.addFlashAttribute("success", "Deleted post " + number);
		return "redirect:/blog";
	}
}
