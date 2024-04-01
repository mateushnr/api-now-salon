package controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;
import com.google.gson.Gson;

import model.Customer;
import model.dao.CustomerDAO;

@WebServlet("/customers/*")

public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CustomerServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		CustomerDAO customerDAO = new CustomerDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api":{
				ArrayList<Customer> customers = customerDAO.selectAll();
                jsonToSend = new Gson().toJson(customers);
                
                break;
			}
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    Customer customer = customerDAO.selectById(id);

	                    if (customer != null) {
	                        jsonToSend = new Gson().toJson(customer);
	                    }
	                } catch (NumberFormatException e) {
	                	System.out.println(e.getMessage());
	                	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            return;
	                }
	            } else {
	            	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		            return;
	            }
				
				break;
			}
			default:{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            return;
			}
		}
		
        response.setContentType("application/json");
        response.getWriter().write(jsonToSend);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		CustomerDAO customerDAO = new CustomerDAO();
		String dataToSend = "";
		String dataSent = "";
		
		switch(pathInfo) {
			case "/api/auth":{
				try (BufferedReader reader = request.getReader()) {
					StringBuilder sb = new StringBuilder();
				    String line;
				    
				    while ((line = reader.readLine()) != null) {
				      sb.append(line);
				    }
				    
				    dataSent = sb.toString();
				} catch (IOException e) {
				    e.printStackTrace();
				    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            return;
				}
				
				Customer customerAuthenticated = customerDAO.authenticate(dataSent);
				
				if(customerAuthenticated != null) {
					dataToSend = new Gson().toJson(customerAuthenticated);
				}else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			        return;
				}
				
				response.setContentType("application/json");
			    response.getWriter().write(dataToSend);
			    
			    break;
			}
			case "/api/login":{
				  try (BufferedReader reader = request.getReader()) {
					  StringBuilder sb = new StringBuilder();
					  String line;
					  
					  while ((line = reader.readLine()) != null) {
						  sb.append(line);
					  }
					  
					  dataSent = sb.toString();
				  } catch (IOException e) {
					  e.printStackTrace();
					  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			          return;
				  }
				  
				  Customer customerSent = new Gson().fromJson(dataSent, Customer.class);
				  
				  Customer customerFound = customerDAO.selectByEmail(customerSent.getEmail());
				 
				 if(customerFound != null) {
					 if(BCrypt.checkpw(customerSent.getPassword(), customerFound.getPassword())) {
						 String token = UUID.randomUUID().toString();
						 customerDAO.setToken(customerFound, token);
						 
						 customerFound.setPassword(null);
						 customerFound.setIdToken(token);
						 
						 dataToSend = new Gson().toJson(customerFound);
						 response.setStatus(HttpServletResponse.SC_OK);
					 }else {
						 response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				         return;
					 }
				 }else {
					 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			         return;
				 }
				 
				 response.setContentType("application/json");
			     response.getWriter().write(dataToSend);
			     
			     break;
			}
			case "/api":{
				try (BufferedReader reader = request.getReader()) {
				    StringBuilder sb = new StringBuilder();
				    String line;
				    
				    while ((line = reader.readLine()) != null) {
				        sb.append(line);
				    }
				    
				    dataSent = sb.toString();
				}catch (IOException e) {
				    e.printStackTrace();
				    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            return;
				}
				
				Customer customer = new Gson().fromJson(dataSent, Customer.class);
				
				if(customerDAO.selectByEmail(customer.getEmail()) == null){
					String hashedPassword = BCrypt.hashpw(customer.getPassword(), BCrypt.gensalt(10));

					customer.setPassword(hashedPassword);
					customerDAO.insert(customer);
					
					response.setStatus(HttpServletResponse.SC_CREATED);
					return;
				}
				
				response.setStatus(HttpServletResponse.SC_CONFLICT);
				break;
			}
			default:{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		CustomerDAO customerDAO = new CustomerDAO();
		String jsonSent = "";

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	        			
	        			try (BufferedReader reader = request.getReader()) {
	        			    StringBuilder sb = new StringBuilder();
	        			    String line;
	        			    while ((line = reader.readLine()) != null) {
	        			        sb.append(line);
	        			    }
	        			    jsonSent = sb.toString();
	        			}catch (IOException e) {
	    				    e.printStackTrace();
	    				    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    		            return;
	    				}
	        			
	        			Customer customer = new Gson().fromJson(jsonSent, Customer.class);
	        			
	        			customer.setId(id);
	        			customerDAO.update(customer);
	        			
	        			response.setStatus(HttpServletResponse.SC_OK);
	    				return;
	                } catch (NumberFormatException e) {
	                	 e.printStackTrace();
	    				 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    		         return;
	                }
	            }else {
	            	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
	            }
			}
			default:{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		CustomerDAO customerDAO = new CustomerDAO();

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	                	
	                	customerDAO.delete(id);
	        			
	        			response.setStatus(HttpServletResponse.SC_OK);
	    				return;
	                } catch (NumberFormatException e) {
	                	 e.printStackTrace();
	    				 response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	    		         return;
	                }
	            }else {
	            	response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					return;
	            }
			}
			default:{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}	
	}
}
