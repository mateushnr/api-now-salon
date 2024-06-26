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

import model.Employee;
import model.dao.EmployeeDAO;

@WebServlet("/employees/*")

public class EmployeeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public EmployeeServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api":{
				ArrayList<Employee> employees = employeeDAO.selectAll();
                jsonToSend = new Gson().toJson(employees);
				
				break;
			}
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int registration = Integer.parseInt(idStr);
	                    Employee employee = employeeDAO.selectById(registration);

	                    if (employee != null) {
	                        jsonToSend = new Gson().toJson(employee);
	                    }
	                } catch (NumberFormatException e) {
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
		
		EmployeeDAO EmployeeDAO = new EmployeeDAO();
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
				
				Employee employeeAuthenticated = EmployeeDAO.authenticate(dataSent);
				
				if(employeeAuthenticated != null) {
					dataToSend = new Gson().toJson(employeeAuthenticated);
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
					  
				Employee employeeSent = new Gson().fromJson(dataSent, Employee.class);
					  
				Employee employeeFound = EmployeeDAO.selectById(employeeSent.getRegistration());
	
				if(employeeFound != null) {
					if(BCrypt.checkpw(employeeSent.getPassword(), employeeFound.getPassword())) {
						String token = UUID.randomUUID().toString();
						EmployeeDAO.setToken(employeeFound, token);
						 
						employeeFound.setPassword(null);
						employeeFound.setIdToken(token);
						 
						dataToSend = new Gson().toJson(employeeFound);
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
				}
				
				Employee employee = new Gson().fromJson(dataSent, Employee.class);
				
				String hashedPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt(10));
				
				employee.setPassword(hashedPassword);
				
				Employee insertedEmployee = EmployeeDAO.insert(employee);
				
			    if(insertedEmployee != null) {
			    	dataToSend = new Gson().toJson(insertedEmployee);
			    	
			    	response.setContentType("application/json");
				    response.getWriter().write(dataToSend);
			    }
				
				response.setStatus(HttpServletResponse.SC_CREATED);
				
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
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
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
	        			
	        			Employee employee = new Gson().fromJson(jsonSent, Employee.class);
	        			
	        			if(employee.getPassword() != null) {
		        			String hashedPassword = BCrypt.hashpw(employee.getPassword(), BCrypt.gensalt(10));
		    				
		    				employee.setPassword(hashedPassword);
	        			}
	        			
	        			
	        			employee.setRegistration(id);
	        			employeeDAO.update(employee);
	        			
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
		
		EmployeeDAO employeeDAO = new EmployeeDAO();

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	        			
	                	employeeDAO.delete(id);
	        			
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
