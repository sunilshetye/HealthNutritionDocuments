package org.spoken_tutorial.health.elasticsearch.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "healthnutritionidx")
public class DocumentSearch {

    @Id
    private String id;

    @Field(type = FieldType.Text, index = true, store = false)
    private String documentContent;

    @Field(type = FieldType.Keyword, index = true, store = true)
    private String documentType;

    @Field(type = FieldType.Keyword, index = true, store = true)
    private String documentId;

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

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDocumentContent() {
        return documentContent;
    }

    public void setDocumentContent(String documentContent) {
        this.documentContent = documentContent;
    }

    public DocumentSearch() {
        super();
    }

    public DocumentSearch(String id, String documentContent, String documentType, String documentId) {
        super();
        this.id = id;
        this.documentContent = documentContent;
        this.documentType = documentType;
        this.documentId = documentId;
    }

    public DocumentSearch(String id, String documentContent) {
        super();
        this.id = id;
        this.documentContent = documentContent;
    }

}
