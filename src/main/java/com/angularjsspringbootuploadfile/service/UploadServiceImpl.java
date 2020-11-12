package com.angularjsspringbootuploadfile.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.angularjsspringbootuploadfile.dao.UploadFileDao;
import com.angularjsspringbootuploadfile.model.UploadFile;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadFileDao uploadFileDao;

	@Override
	public ResponseMetadata save(MultipartFile multipartFile) throws IOException {
		UploadFile uploadFile = new UploadFile();

		uploadFile.setDocName(multipartFile.getOriginalFilename());
		uploadFile.setFile(multipartFile.getBytes());
		uploadFileDao.save(uploadFile);

		ResponseMetadata metadata = new ResponseMetadata();
		metadata.setMessage("success");
		metadata.setStatus(200);
		return metadata;
	}

	@Override
	public byte[] getUploadFile(Integer id) {
		Optional<UploadFile> optionalUploadFile = uploadFileDao.findById(id);

		if (optionalUploadFile.isPresent()) {
			return optionalUploadFile.get().getFile();
		}

		return null;
	}

	@Override
	public List<UploadFile> findAll() {
		return (List<UploadFile>) uploadFileDao.findAll();
	}

	@Override
	public UploadFile findUploadFile(Integer id) {
		Optional<UploadFile> optionalUploadFile = uploadFileDao.findById(id);

		if (optionalUploadFile.isPresent()) {
			return optionalUploadFile.get();
		}

		return null;
	}

}
