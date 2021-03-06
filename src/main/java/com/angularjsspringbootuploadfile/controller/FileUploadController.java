package com.angularjsspringbootuploadfile.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.angularjsspringbootuploadfile.model.UploadFile;
import com.angularjsspringbootuploadfile.service.UploadService;
import com.angularjsspringbootuploadfile.storage.StorageFileNotFoundException;
import com.angularjsspringbootuploadfile.storage.StorageService;

@Controller
public class FileUploadController {

	@Autowired
	private UploadService uploadService;
	
	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}
	
	@GetMapping("/")
	public String listUploadedFiles(Model model) throws IOException {
		return "index";
	}
	
	@GetMapping("/uploadSingleFile")
	public String index(Model model) {
		model.addAttribute("files", storageService.loadAll()
			.map(path -> MvcUriComponentsBuilder
			.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
			.build().toUri().toString())
			.collect(Collectors.toList()));
		
		return "uploadSingleFile";
	}

	@GetMapping("/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
		Resource file = storageService.loadAsResource(filename);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@PostMapping("/")
	public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		storageService.store(file);
		redirectAttributes.addFlashAttribute("message", "You successfully uploaded " + file.getOriginalFilename() + "!");

		return "redirect:/";
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/uploadMultipleFiles")
	public String uploadMultipleFiles(Model model) {
		model.addAttribute("files", storageService.loadAll()
			.map(path -> MvcUriComponentsBuilder
			.fromMethodName(FileUploadController.class, "serveFile", path.getFileName().toString())
			.build().toUri().toString())
			.collect(Collectors.toList()));
		
		List<UploadFile> list = uploadService.findAll();
		
		for (UploadFile uploadFile : list) {
			System.out.println(uploadFile.getName());
		}
		
		return "uploadMultipleFiles";
	}

}
