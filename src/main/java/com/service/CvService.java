package com.service;

import com.entity.Cv;

public interface CvService {

	void saveCv(Cv cv);

	Cv findById(Long id);

	void delete(Long id);

}
