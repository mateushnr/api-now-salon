package model;

public class Service {
	private int id;
	private String name;
	private String description;
	private String status;
	private String estimatedTime;
	private Float price;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(String estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
}
