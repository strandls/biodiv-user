package com.strandls.user.service;

import com.strandls.user.pojo.Language;

public interface LanguageService {

	Language getLanguageByThreeLetterCode(String language);
	Language getLanguageByTwoLetterCode(String language);
	Language getLanguageById(Long id);
	Language getLanguageByName(String languageName);
	
}
