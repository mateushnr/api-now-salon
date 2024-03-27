package model;

public class CustomerWithToken {
	 private Customer customer;
	    private String token;
	
	    public CustomerWithToken(Customer customer, String token) {
	        this.customer = customer;
	        this.token = token;
	    }
	
	    public Customer getCustomer() {
	        return customer;
	    }
	
	    public String getToken() {
	        return token;
	    }
}
