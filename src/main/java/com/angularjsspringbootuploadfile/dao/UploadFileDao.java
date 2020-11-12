package com.angularjsspringbootuploadfile.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.angularjsspringbootuploadfile.model.UploadFile;

@Repository
public interface UploadFileDao extends CrudRepository<UploadFile, Integer> {

}
