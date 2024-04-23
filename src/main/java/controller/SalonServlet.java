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

import model.Salon;
import model.dao.SalonDAO;

@WebServlet("/salons/*")
public class SalonServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public SalonServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		SalonDAO salonDAO = new SalonDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api":{
				ArrayList<Salon> salons = salonDAO.selectAll();
                jsonToSend = new Gson().toJson(salons);
                
                break;
			}
			case "/api/":{
				String idStr = request.getParameter("id");
				String name = request.getParameter("name");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    Salon salon = salonDAO.selectById(id);

	                    if (salon != null) {
	                        jsonToSend = new Gson().toJson(salon);
	                    }
	                } catch (NumberFormatException e) {
	                	System.out.println(e.getMessage());
	                	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			            return;
	                }
	            } else if(name != null && !name.isEmpty()) {
	            	Salon salon = salonDAO.selectByName(name);

                    if (salon != null) {
                        jsonToSend = new Gson().toJson(salon);
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
		
		SalonDAO salonDAO = new SalonDAO();
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
				
				Salon salon = new Gson().fromJson(dataSent, Salon.class);
				
				salonDAO.insert(salon);
								
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
		
		SalonDAO salonDAO = new SalonDAO();
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
	        			
	        			Salon salon = new Gson().fromJson(jsonSent, Salon.class);
	        			
	        			salon.setId(id);
	        			salonDAO.update(salon);
	        			
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
		
		SalonDAO salonDAO = new SalonDAO();

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	                	
	                	salonDAO.delete(id);
	        			
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
