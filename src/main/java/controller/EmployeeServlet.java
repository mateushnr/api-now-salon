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

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String idStr = request.getParameter("id");
		String json = "";
		
		if (pathInfo.equals("/api") || pathInfo.equals("/api/")) {
			if (idStr != null && !idStr.isEmpty()) {
                try {
                    int registration = Integer.parseInt(idStr);
                    Employee employee = employeeDAO.selectById(registration);

                    if (employee != null) {
                        json = new Gson().toJson(employee);
                    }
                } catch (NumberFormatException e) {
                }
            } else {
                ArrayList<Employee> employees = employeeDAO.selectAll();
                json = new Gson().toJson(employees);
            }
		} else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
		
        response.setContentType("application/json");
        response.getWriter().write(json);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeDAO EmployeeDAO = new EmployeeDAO();
		String dataToSend = "";
		String dataSent = "";
		
		if(pathInfo.equals("/api/auth")) {
			try (BufferedReader reader = request.getReader()) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			      sb.append(line);
			    }
			    dataSent = sb.toString();
			  } catch (IOException e) {
			    e.printStackTrace();
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
		} else if(pathInfo.equals("/api/login")){
			
			  try (BufferedReader reader = request.getReader()) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			      sb.append(line);
			    }
			    dataSent = sb.toString();
			  } catch (IOException e) {
			    e.printStackTrace();
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
			 
		} else if (pathInfo.equals("/api")){
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
			EmployeeDAO.insert(employee);
			response.setStatus(HttpServletResponse.SC_CREATED);
			return;
			
		} else {
			 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			 return;
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeDAO employeeDAO = new EmployeeDAO();
		String jsonSent = "";

		if(pathInfo.equals("/api/")) {
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
        			}
        			
        			Employee employee = new Gson().fromJson(jsonSent, Employee.class);
        			
        			employee.setRegistration(id);
        			employeeDAO.update(employee);
        			
        			response.setStatus(HttpServletResponse.SC_OK);
    				return;
                } catch (NumberFormatException e) {
                	System.out.print(e);
                }
            }
			
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		EmployeeDAO employeeDAO = new EmployeeDAO();

		if(pathInfo.equals("/api/")) {
			String idStr = request.getParameter("id");
			if (idStr != null && !idStr.isEmpty()) {
			    try {
			        int id = Integer.parseInt(idStr);
					employeeDAO.delete(id);
			        
			    } catch (NumberFormatException e) {
			     System.out.print(e);
			    }
			}
			
		}else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			return;
		}	
	}
}
