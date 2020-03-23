package com.strandls.user.service.impl;

import com.google.inject.Inject;
import com.strandls.user.dao.LanguageDao;
import com.strandls.user.pojo.Language;
import com.strandls.user.service.LanguageService;

public class LanguageServiceImpl implements LanguageService {

	@Inject
	private LanguageDao languageDao;

	@Override
	public Language getLanguageByTwoLetterCode(String language) {
		Language lang = languageDao.findByPropertyWithCondition("twoLetterCode", language, "=");
		if (lang == null) {
			return getCurrentLanguage();
		}
		return lang;
	}
	
	private Language getCurrentLanguage() {
		return languageDao.findByPropertyWithCondition("name", Language.DEFAULT_LANGUAGE, "=");
	}

}
