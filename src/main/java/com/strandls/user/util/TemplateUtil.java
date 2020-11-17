package com.strandls.user.util;

import java.io.StringWriter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateUtil {
	
	private Configuration configuration;
	
	private static final Logger logger = LoggerFactory.getLogger(TemplateUtil.class);
	
	public TemplateUtil(Configuration configuration) {
		this.configuration = configuration;
		this.configuration.setClassForTemplateLoading(getClass(), "/templates/");
	}
	
	public String getTemplateAsString(String templateFile, Map<String, String> model) {
		Template template = null;
		StringWriter writer = new StringWriter();
		try {
			template = configuration.getTemplate(templateFile);
			template.process(model, writer);	
		} catch (Exception ex) {	
			logger.error(ex.getMessage());
		}
		return writer.toString();			
	}

}
