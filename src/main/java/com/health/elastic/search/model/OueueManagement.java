package com.health.elastic.search.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "queuemanagement")
public class OueueManagement {
	
	@Id
	private int queueId;
	
	@Field(type = FieldType.Long, index = true, store = true)
	private long request;
	
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String requestType;
	
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String status;
	
	@Field(type = FieldType.Long, index = true, store = true)
	private long startTime;
	
	@Field(type = FieldType.Long, index = true, store = true)
	private long endTime;
	
	@Field(type = FieldType.Long, index = true, store = true)
	private long procesingTime;
	
	@Field(type = FieldType.Keyword, index = true, store = true)
	private String message;

	public int getQueueId() {
		return queueId;
	}

	public void setQueueId(int queueId) {
		this.queueId = queueId;
	}

	public long getRequest() {
		return request;
	}

	public void setRequest(long request) {
		this.request = request;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public long getProcesingTime() {
		return procesingTime;
	}

	public void setProcesingTime(long procesingTime) {
		this.procesingTime = procesingTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	
	public OueueManagement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OueueManagement(int queueId, long request, String requestType, String status, long startTime, long endTime,
			long procesingTime, String message) {
		super();
		this.queueId = queueId;
		this.request = request;
		this.requestType = requestType;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procesingTime = procesingTime;
		this.message = message;
	}
	
	
	
	

}
