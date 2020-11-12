package com.angularjsspringbootuploadfile.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.angularjsspringbootuploadfile.model.UploadFile;

public interface UploadService {

	ResponseMetadata save(MultipartFile multipartFile) throws IOException;

	byte[] getUploadFile(Integer id);

	List<UploadFile> findAll();

	UploadFile findUploadFile(Integer id);

}
