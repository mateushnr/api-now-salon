package model;

public class Salon {
	private int id;
	private String name;
	private String phone;
	private String daysWeekOpen;
	private String timeOpen;
	private String timeClose;
	private String emailContact;
	private String status;
	private String address;
	private String neighborhood;
	private String cityState;
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDaysWeekOpen() {
		return daysWeekOpen;
	}
	public void setDaysWeekOpen(String daysWeekOpen) {
		this.daysWeekOpen = daysWeekOpen;
	}
	public String getTimeOpen() {
		return timeOpen;
	}
	public void setTimeOpen(String timeOpen) {
		this.timeOpen = timeOpen;
	}
	public String getTimeClose() {
		return timeClose;
	}
	public void setTimeClose(String timeClose) {
		this.timeClose = timeClose;
	}
	public String getEmailContact() {
		return emailContact;
	}
	public void setEmailContact(String emailContact) {
		this.emailContact = emailContact;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getNeighborhood() {
		return neighborhood;
	}
	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}
	public String getCityState() {
		return cityState;
	}
	public void setCityState(String cityState) {
		this.cityState = cityState;
	}
}
