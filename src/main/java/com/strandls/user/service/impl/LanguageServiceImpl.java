package com.strandls.user.service.impl;

import com.google.inject.Inject;
import com.strandls.user.dao.LanguageDao;
import com.strandls.user.pojo.Language;
import com.strandls.user.service.LanguageService;

public class LanguageServiceImpl implements LanguageService {
	
	@Inject
	private LanguageDao languageDao;
	
	public Language getCurrentLanguage() {
		return languageDao.findByPropertyWithCondition("name", Language.DEFAULT_LANGUAGE, "=");
	}

	@Override
	public Language getLanguageByThreeLetterCode(String language) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Language getLanguageByTwoLetterCode(String language) {
		Language lang = languageDao.findByPropertyWithCondition("twoLetterCode", language, "=");
		if (lang == null) {
			return getCurrentLanguage();
		}
		return lang;
	}

	@Override
	public Language getLanguageById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Language getLanguageByName(String languageName) {
		// TODO Auto-generated method stub
		return null;
	}

}
