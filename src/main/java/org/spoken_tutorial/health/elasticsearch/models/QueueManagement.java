package org.spoken_tutorial.health.elasticsearch.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class QueueManagement {
	
	@Id
    @Column(name = "queueId", nullable = false, updatable = false)
	private long queueId;
	
	@Column(name = "requestId", nullable = true)
	private String requestId;
	
	@Column(name = "requestTime", nullable = true)
	private long requestTime;
	
	@Column(name = "requestType", nullable = true)
	private String requestType;
	
	@Column(name = "status", nullable = true)
	private String status;
	
	@Column(name = "startTime", nullable = true)
	private long startTime;
	
	@Column(name = "endTime", nullable = true)
	private long endTime;
	
	@Column(name = "procesingTime", nullable = true)
	private long procesingTime;
	
	@Column(name = "message", nullable = true)
	private String message;

	public long getQueueId() {
		return queueId;
	}

	public void setQueueId(long queueId) {
		this.queueId = queueId;
	}

	

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public long getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(long requestTime) {
		this.requestTime = requestTime;
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

	
	public QueueManagement() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QueueManagement(long queueId, long request, String requestType, String status, long startTime, long endTime,
			long procesingTime, String message) {
		super();
		this.queueId = queueId;
		this.requestTime = request;
		this.requestType = requestType;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
		this.procesingTime = procesingTime;
		this.message = message;
	}

	public QueueManagement(long queueId, String requestId, long requestTime, String requestType) {
		super();
		this.queueId = queueId;
		this.requestId = requestId;
		this.requestTime = requestTime;
		this.requestType = requestType;
	}
	
	
	
	
	
	

}
