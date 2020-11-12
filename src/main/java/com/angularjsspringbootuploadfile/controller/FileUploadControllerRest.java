package com.angularjsspringbootuploadfile.controller;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.angularjsspringbootuploadfile.form.UploadForm;
import com.angularjsspringbootuploadfile.model.UploadFile;
import com.angularjsspringbootuploadfile.service.ResponseMetadata;
import com.angularjsspringbootuploadfile.service.UploadService;
import com.angularjsspringbootuploadfile.storage.StorageService;

@RestController
public class FileUploadControllerRest {

	private static final Logger LOG = Logger.getLogger(FileUploadControllerRest.class.getName());

	@Autowired
	private UploadService uploadService;

	private final StorageService storageService;

	@Autowired
	public FileUploadControllerRest(StorageService storageService) {
		this.storageService = storageService;
	}

//	public ResponseEntity<?> uploadFile(@RequestPart("file") MultipartFile file) {
//	public ResponseEntity<?> uploadFile(@RequestParam(value="file") MultipartFile file) throws IOException {
	@PostMapping("/uploadSingleFile")
	public ResponseEntity<?> uploadFileMulti(@ModelAttribute UploadForm form) throws Exception {

		if (null == form.getFile().getOriginalFilename()) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		MultipartFile file = form.getFile();

//		try {
//			byte[] bytes = file.getBytes();
//			Path path = Paths.get(file.getOriginalFilename());
//			Files.write(path, bytes);
//			System.out.println(path.getFileName());
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}

		storageService.store(file);
		
		// DB
		ResponseMetadata responseMetadata = uploadService.save(file);

		return new ResponseEntity<>(
				"Description: " + form.getDescription() + " File: " + file.getOriginalFilename() + " Uploaded.",
				HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
    public HttpEntity<byte[]> getUploadFileImage(@PathVariable Integer id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(uploadService.getUploadFile(id), httpHeaders, HttpStatus.OK);
    }
    
    @GetMapping("/file/{id}")
    public ResponseEntity<?> getUploadFile(@PathVariable Integer id) {
    	UploadFile uploadFile = uploadService.findUploadFile(id);
    	
        return new ResponseEntity<UploadFile>(uploadFile, HttpStatus.OK);
    }

    @GetMapping("/listAll")
    public @ResponseBody List<UploadFile> listAll() {
        return uploadService.findAll();
    }

}
