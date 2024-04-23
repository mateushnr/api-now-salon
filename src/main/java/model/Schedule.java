package model;

public class Schedule {
	private int id;
	private int idCustomer;
	private int idEmployee;
	private int idService;
	private String dateSchedule;
	private String hourSchedule;
	private String status;
	private String observation;
	private String reasonCancellation;
	private String whoCancelled;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}
	public int getIdEmployee() {
		return idEmployee;
	}
	public void setIdEmployee(int idEmployee) {
		this.idEmployee = idEmployee;
	}
	public int getIdService() {
		return idService;
	}
	public void setIdService(int idService) {
		this.idService = idService;
	}
	public String getDateSchedule() {
		return dateSchedule;
	}
	public void setDateSchedule(String dateSchedule) {
		this.dateSchedule = dateSchedule;
	}
	public String getHourSchedule() {
		return hourSchedule;
	}
	public void setHourSchedule(String hourSchedule) {
		this.hourSchedule = hourSchedule;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getObservation() {
		return observation;
	}
	public void setObservation(String observation) {
		this.observation = observation;
	}
	public String getReasonCancellation() {
		return reasonCancellation;
	}
	public void setReasonCancellation(String reasonCancellation) {
		this.reasonCancellation = reasonCancellation;
	}
	public String getWhoCancelled() {
		return whoCancelled;
	}
	public void setWhoCancelled(String whoCancelled) {
		this.whoCancelled = whoCancelled;
	}
}
