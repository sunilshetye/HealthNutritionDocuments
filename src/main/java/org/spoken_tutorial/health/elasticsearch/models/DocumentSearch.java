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

    @Field(type = FieldType.Text, index = true, store = false)
    private String outlineIndex;

    @Field(type = FieldType.Text, index = false, store = true)
    private String outlineContent;

    @Field(type = FieldType.Keyword, index = true, store = true)
    private String documentType;

    @Field(type = FieldType.Keyword, index = true, store = true)
    private String documentId;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String language;

    @Field(type = FieldType.Integer, index = true, store = true)
    private int languageId;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String category;

    @Field(type = FieldType.Integer, index = true, store = true)
    private int categoryId;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String topic;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String videoPath;

    @Field(type = FieldType.Integer, index = true, store = true)
    private int topicId;

    @Field(name = "rankView", type = FieldType.Integer, index = true, store = true)
    private int rank;

    @Field(type = FieldType.Integer, index = true, store = false)
    private int orderValue;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String viewUrl;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String documentUrl;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String title;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String description;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String thumbnailPath;

    @Field(type = FieldType.Long, index = true, store = true)
    private Long creationTime;

    @Field(type = FieldType.Long, index = true, store = true)
    private Long modificationTime;

    @Field(type = FieldType.Long, index = true, store = true)
    private Long changeTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDocumentUrl() {
        return documentUrl;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutlineIndex() {
        return outlineIndex;
    }

    public void setOutlineIndex(String outlineIndex) {
        this.outlineIndex = outlineIndex;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getOutlineContent() {
        return outlineContent;
    }

    public void setOutlineContent(String outlineContent) {
        this.outlineContent = outlineContent;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public Long getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Long modificationTime) {
        this.modificationTime = modificationTime;
    }

    public Long getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Long changeTime) {
        this.changeTime = changeTime;
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

    public DocumentSearch(String id, String documentContent, String documentType, String documentId, String videoPath,
            int orderValue) {
        super();
        this.id = id;
        this.documentContent = documentContent;
        this.documentType = documentType;
        this.documentId = documentId;
        this.videoPath = videoPath;
        this.orderValue = orderValue;
    }

    public DocumentSearch(String id, String documentContent) {
        super();
        this.id = id;
        this.documentContent = documentContent;
    }

    @Override
    public String toString() {
        return "DocumentSearch [documentType=" + documentType + ", documentId=" + documentId + ", language=" + language
                + ", rank=" + rank + ", viewUrl=" + viewUrl + ", videoPath=" + videoPath + ", creationTime="
                + creationTime + ", modificationTime=" + modificationTime + ", changeTime=" + changeTime
                + ", orderValue=" + orderValue + "]";
    }

}
