package us.henge.web.photo;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.henge.db.pojo.Item;
import us.henge.db.service.ItemsService;

@Controller
@RequestMapping(value = "/photos")
public class Photos {
	private static final Logger log = LoggerFactory.getLogger(Photos.class);
	
	@Autowired
	private ItemsService itemsService = null;
	
	@RequestMapping
	public String index() {
		return "photos/photos";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newAlbumEditor(Model mod) {
		mod.addAttribute("item", new Item());
		return "photos/edit";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.POST)
	public String newAlbumHandler(@Valid Item item, BindingResult rItem, RedirectAttributes redirectAttributes) {
		if(rItem.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rItem.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("item", item);
			return "redirect:/photos/new";
		}
		
		try {
			itemsService.create(item);
		} catch (Exception e){
			log.error("Had an error creating a new album", e);
			redirectAttributes.addFlashAttribute("error", "Had an error creating a new album.");
			redirectAttributes.addFlashAttribute("item", item);
			return "redirect:/photos/new";
		}
		
		redirectAttributes.addFlashAttribute("success", "Created album " + item.getName());
		return "redirect:/photos/album/" + item.getId();
	}
	
	@RequestMapping(value = "/album/{number}", method = RequestMethod.GET)
	public String viewAlbum(@PathVariable int number, Model mod, RedirectAttributes redirectAttributes) {
		Item item = itemsService.getItemById(number);
		if(item == null){
			redirectAttributes.addFlashAttribute("error", "Could not find album " + number);
			return "redirect:/photos";
		}
		mod.addAttribute("album", item);
		
		return "photos/album";
	}
	
	@RequestMapping(value = "/album/{number}/edit", method = RequestMethod.GET)
	public String editAlbum(@PathVariable int number, Model mod) {
		Item item = itemsService.getItemById(number);
		mod.addAttribute("item", item);
		
		return "photos/edit";
	}
	
	@RequestMapping(value = "/album/{number}/edit", method = RequestMethod.POST)
	public String editAlbumHandler(@PathVariable int number, @Valid Item item, BindingResult rItem, RedirectAttributes redirectAttributes) {
		if(rItem.hasErrors()) {
			redirectAttributes.addFlashAttribute("error", rItem.getFieldError().getDefaultMessage());
			redirectAttributes.addFlashAttribute("item", item);
			return "redirect:/photos/album/" + number + "/edit";
		}
		
		try {
			Item oldItem = itemsService.getItemById(number);
			oldItem.setName(item.getName());
			oldItem.setDate(item.getDate());
			
			itemsService.update(oldItem);
		} catch (Exception e){
			log.error("Had an error editing the album", e);
			redirectAttributes.addFlashAttribute("error", "Had an error updating the album.");
			redirectAttributes.addFlashAttribute("item", item);
			return "redirect:/photos/album/" + number + "/edit";
		}
		
		redirectAttributes.addFlashAttribute("success", "Updated album " + item.getName());
		return "redirect:/photos/album/" + number;
	}
	
	@RequestMapping(value = "/album/{number}/delete", method = RequestMethod.POST)
	public String editPostHandler(@PathVariable int number, RedirectAttributes redirectAttributes) {
		try {
			itemsService.delete(number);
		} catch (Exception e){
			log.error("Had an error deleting post", e);
			redirectAttributes.addFlashAttribute("error", "Had an error deleting the album.");
			return "redirect:/photos/album" + number;
		}
		
		redirectAttributes.addFlashAttribute("success", "Deleted post " + number);
		return "redirect:/photos";
	}
}
