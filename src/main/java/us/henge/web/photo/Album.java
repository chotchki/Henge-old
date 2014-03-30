package us.henge.web.photo;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import us.henge.db.pojo.Item;
import us.henge.db.service.ItemsService;
import us.henge.service.content.PhotoService;
import us.henge.utility.security.KeyCreation;
import us.henge.web.pojo.AddPreview;

@Controller
@RequestMapping("/photos/album")
public class Album {
	private static final Logger log = LoggerFactory.getLogger(Album.class);
	
	@Autowired
	private ItemsService itemsService = null;
	
	@Autowired
	private PhotoService photoService = null;
	
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
			return "redirect:/photos/album/new";
		}
		
		try {
			itemsService.create(item);
		} catch (Exception e){
			log.error("Had an error creating a new album", e);
			redirectAttributes.addFlashAttribute("error", "Had an error creating a new album.");
			redirectAttributes.addFlashAttribute("item", item);
			return "redirect:/photos/album/new";
		}
		
		redirectAttributes.addFlashAttribute("success", "Created album " + item.getName());
		return "redirect:/photos/album/" + item.getId();
	}
	
	@RequestMapping(value = "/{number}", method = RequestMethod.GET)
	public String viewAlbum(@PathVariable int number, Model mod, RedirectAttributes redirectAttributes) {
		Item item = itemsService.getItemById(number);
		if(item == null){
			redirectAttributes.addFlashAttribute("error", "Could not find album " + number);
			return "redirect:/photos";
		}
		mod.addAttribute("album", item);
		
		List<Item> photos = itemsService.getItemsByParentId(number);
		mod.addAttribute("items", photos);
		
		return "photos/album";
	}
	
	@RequestMapping(value = "/{number}/add", method = RequestMethod.GET)
	public String showPhotoAdder(@PathVariable int number, Model mod, HttpSession session) throws Exception{
		List<AddPreview> contents = photoService.listAddFolder();
		mod.addAttribute("files", contents);
		
		return "photos/add";
	}
	
	@RequestMapping(value = "/{number}/add", method = RequestMethod.POST)
	public String processPhotoAdder(@PathVariable int number, RedirectAttributes redirectAttributes, HttpSession session, @RequestParam("files[]") List<String> files) throws Exception{
		List<String> fileNames = new ArrayList<String>();
		for(String encName : files){
			String decr = KeyCreation.decrypt(session, encName);
			fileNames.add(decr);
		}
		
		photoService.addPhotos(number, fileNames);
		
		redirectAttributes.addFlashAttribute("success", "Added all photos!");
		return "redirect:/photos/album/" + number;
	}
	
	@RequestMapping(value = "/{number}/edit", method = RequestMethod.GET)
	public String editAlbum(@PathVariable int number, Model mod) {
		Item item = itemsService.getItemById(number);
		mod.addAttribute("item", item);
		
		return "photos/edit";
	}
	
	@RequestMapping(value = "/{number}/edit", method = RequestMethod.POST)
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
	
	@RequestMapping(value = "/{number}/delete", method = RequestMethod.POST)
	public String editPostHandler(@PathVariable int number, RedirectAttributes redirectAttributes) {
		try {
			itemsService.delete(number);
		} catch (Exception e){
			log.error("Had an error deleting post", e);
			redirectAttributes.addFlashAttribute("error", "Had an error deleting the album.");
			return "redirect:/photos/album/" + number;
		}
		
		redirectAttributes.addFlashAttribute("success", "Deleted album " + number);
		return "redirect:/photos";
	}
}
