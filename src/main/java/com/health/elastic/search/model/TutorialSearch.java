package com.health.elastic.search.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


@Document(indexName = "healthnutritionidx")
public class TutorialSearch {
	
	@Id
	private String id;
	
	@Field(type = FieldType.Text, index = true, store = false)
	private String documentContent;
	
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String documentType;
	
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String documentTypeId;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(String documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentContent() {
		return documentContent;
	}

	public void setDocumentContent(String documentContent) {
		this.documentContent = documentContent;
	}

	public TutorialSearch() {
		super();
	}
	
	

	public TutorialSearch(String id, String documentContent, String documentType, String documentTypeId) {
		super();
		this.id = id;
		this.documentContent = documentContent;
		this.documentType = documentType;
		this.documentTypeId = documentTypeId;
	}

	public TutorialSearch(String id, String documentContent) {
		super();
		this.id = id;
		this.documentContent = documentContent;
	}

	
}
