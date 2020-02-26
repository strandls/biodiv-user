package com.strandls.user.util;

import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateUtil {
	
	private Configuration configuration;
	
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
			
		}
		return writer.toString();			
	}

}
