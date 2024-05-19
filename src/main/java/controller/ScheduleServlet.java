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

import model.Schedule;
import model.dao.ScheduleDAO;

@WebServlet("/schedules/*")
public class ScheduleServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
    public ScheduleServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
String pathInfo = request.getPathInfo();
		
		ScheduleDAO scheduleDAO = new ScheduleDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api":{
				ArrayList<Schedule> schedules = scheduleDAO.selectAll();
                jsonToSend = new Gson().toJson(schedules);
                
                break;
			}
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    Schedule schedule = scheduleDAO.selectById(id);

	                    if (schedule != null) {
	                        jsonToSend = new Gson().toJson(schedule);
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
		
		ScheduleDAO scheduleDAO = new ScheduleDAO();
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
				
				Schedule schedule = new Gson().fromJson(dataSent, Schedule.class);
				
				scheduleDAO.insert(schedule);
								
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
		
		ScheduleDAO scheduleDAO = new ScheduleDAO();
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
	        			
	        			Schedule schedule = new Gson().fromJson(jsonSent, Schedule.class);
	        			
	        			schedule.setId(id);
	        			scheduleDAO.update(schedule);
	        			
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
		
		ScheduleDAO scheduleDAO = new ScheduleDAO();

		switch(pathInfo) {
			case "/api/":{
				String idStr = request.getParameter("id");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                	int id = Integer.parseInt(idStr);
	                	
	                	scheduleDAO.delete(id);
	        			
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
