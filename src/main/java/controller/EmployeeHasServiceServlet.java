package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import model.EmployeeHasService;
import model.dao.EmployeeHasServiceDAO;

@WebServlet("/employeeJobs/*")
public class EmployeeHasServiceServlet extends HttpServlet {
private static final long serialVersionUID = 1L;
    
    public EmployeeHasServiceServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeHasServiceDAO employeeJobsDAO = new EmployeeHasServiceDAO();
		String jsonToSend = "";
		
		switch(pathInfo) {
			case "/api/jobsbyemployee/":{
				String idStr = request.getParameter("idemployee");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    
	                    ArrayList<EmployeeHasService> employeeJobs = employeeJobsDAO.selectJobsByEmployee(id);
	                    
	                    jsonToSend = new Gson().toJson(employeeJobs);   
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
			case "/api/employeesbyjob/":{
				String idStr = request.getParameter("idemployee");
				
				if (idStr != null && !idStr.isEmpty()) {
	                try {
	                    int id = Integer.parseInt(idStr);
	                    
	                    ArrayList<EmployeeHasService> employeeJobs = employeeJobsDAO.selectJobsByEmployee(id);
	                    
	                    jsonToSend = new Gson().toJson(employeeJobs);   
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
		
		EmployeeHasServiceDAO employeeJobsDAO = new EmployeeHasServiceDAO();
		List<EmployeeHasService> employeeJobs = new ArrayList<>();
		
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
				   
				    TypeToken<List<EmployeeHasService>> employeeHasServiceListType = new TypeToken<List<EmployeeHasService>>() {};
		            employeeJobs = new Gson().fromJson(dataSent, employeeHasServiceListType.getType());
				}catch (IOException e) {
				    e.printStackTrace();
				    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            return;
				}
				
				for (EmployeeHasService employeeJob : employeeJobs) {
		            try {
		                employeeJobsDAO.insert(employeeJob);
		            } catch (Exception e) {
		                e.printStackTrace();
		                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		                return; 
		            }
		        }
								
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
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeHasServiceDAO employeeJobsDAO = new EmployeeHasServiceDAO();
		List<EmployeeHasService> employeeJobs = new ArrayList<>();
		
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
				   
				    TypeToken<List<EmployeeHasService>> employeeHasServiceListType = new TypeToken<List<EmployeeHasService>>() {};
		            employeeJobs = new Gson().fromJson(dataSent, employeeHasServiceListType.getType());
				}catch (IOException e) {
				    e.printStackTrace();
				    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            return;
				}
				
				for (EmployeeHasService employeeJob : employeeJobs) {
		            try {
		                employeeJobsDAO.delete(employeeJob.getIdService(), employeeJob.getIdEmployee());
		            } catch (Exception e) {
		                e.printStackTrace();
		                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		                return; 
		            }
		        }
				
				response.setStatus(HttpServletResponse.SC_OK);
				return;
			}
			default:{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
		}	
	}
}
