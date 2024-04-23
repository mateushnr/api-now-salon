package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Service;
import model.dao.ServiceDAO;

@WebServlet("/services/*")
public class ServiceServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
    public ServiceServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		ServiceDAO serviceDAO = new ServiceDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api":{
				ArrayList<Service> services = serviceDAO.selectAll();
                jsonToSend = new Gson().toJson(services);
                
                break;
			}
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    Service service = serviceDAO.selectById(id);

	                    if (service != null) {
	                        jsonToSend = new Gson().toJson(service);
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
		
		ServiceDAO serviceDAO = new ServiceDAO();
		String dataSent = "";
		
		switch(pathInfo) {
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
				
				Service service = new Gson().fromJson(dataSent, Service.class);
				
				serviceDAO.insert(service);
								
				response.setStatus(HttpServletResponse.SC_CREATED);
				return;
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
		
		ServiceDAO serviceDAO = new ServiceDAO();
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
	        			
	        			Service service = new Gson().fromJson(jsonSent, Service.class);
	        			
	        			service.setId(id);
	        			serviceDAO.update(service);
	        			
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
		
		ServiceDAO serviceDAO = new ServiceDAO();

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	                	
	                	serviceDAO.delete(id);
	        			
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
