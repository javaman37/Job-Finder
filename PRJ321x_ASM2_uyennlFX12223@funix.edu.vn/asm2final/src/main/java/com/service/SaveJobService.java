package com.service;

import org.springframework.data.domain.Page;

import com.entity.SaveJob;
import com.entity.User;

public interface SaveJobService {
	boolean saveJob(Long userId, Long recruitmentId);

	Page<SaveJob> getSavedJobs(User user, int page);
	void deleteSaveJob(Long id);

}
