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

import com.google.gson.Gson;

import model.Customer;
import model.CustomerWithToken;
import model.dao.CustomerDAO;

@WebServlet("/*")

public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CustomerServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo();
		
		CustomerDAO customerDAO = new CustomerDAO();
		String idStr = request.getParameter("id");
		String json = "";
		
		if (pathInfo.equals("/api/customers") || pathInfo.equals("/api/customers/")) {
			if (idStr != null && !idStr.isEmpty()) {
                try {
                    int id = Integer.parseInt(idStr);
                    Customer customer = customerDAO.selectById(id);

                    if (customer != null) {
                        json = new Gson().toJson(customer);
                    }
                } catch (NumberFormatException e) {
                }
            } else {
                ArrayList<Customer> customers = customerDAO.selectAll();
                json = new Gson().toJson(customers);
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
		
		CustomerDAO customerDAO = new CustomerDAO();
		String dataToSend = "";
		String dataSent = "";
		
		if(pathInfo.equals("/api/customers/auth")) {
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
			
			Customer customerAuthenticated = customerDAO.authenticate(dataSent);
			
			if(customerAuthenticated != null) {
				dataToSend = new Gson().toJson(customerAuthenticated);
			}else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		        return;
			}
			
			response.setContentType("application/json");
		    response.getWriter().write(dataToSend);
		} else if(pathInfo.equals("/api/customers/login")){
			
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
			  
			  Customer customerSent = new Gson().fromJson(dataSent, Customer.class);
			  
			  Customer customerFound = customerDAO.selectByEmail(customerSent.getEmail());
			 
			 if(customerFound != null) {
				 if(customerFound.getPassword().equals(customerSent.getPassword())) {
					 String token = UUID.randomUUID().toString();
					 customerDAO.setToken(customerFound, token);
					 
					 CustomerWithToken customerWithToken = new CustomerWithToken(customerFound, token);
					 dataToSend = new Gson().toJson(customerWithToken);
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
			 
		} else if (pathInfo.equals("/api/customers")){
			try (BufferedReader reader = request.getReader()) {
			    StringBuilder sb = new StringBuilder();
			    String line;
			    while ((line = reader.readLine()) != null) {
			        sb.append(line);
			    }
			    
			    dataSent = sb.toString();
			}
			
			Customer customer = new Gson().fromJson(dataSent, Customer.class);
			
			if(customerDAO.selectByEmail(customer.getEmail()) == null){
				customerDAO.insert(customer);
				response.setStatus(HttpServletResponse.SC_CREATED);
				return;
			}
			
			response.setStatus(HttpServletResponse.SC_CONFLICT);
			return;
			
		} else {
			 response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			 return;
		}
		
		
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String pathInfo = request.getPathInfo();

		int id = Integer.parseInt(request.getParameter("id"));
		
		
		CustomerDAO customerDAO = new CustomerDAO();
		
		String json = "";
		try (BufferedReader reader = request.getReader()) {
		    StringBuilder sb = new StringBuilder();
		    String line;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line);
		    }
		    json = sb.toString();
		}
		
		Customer customer = new Gson().fromJson(json, Customer.class);
		
		customer.setId(id);
		customerDAO.update(customer);
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String pathInfo = request.getPathInfo();

		
		CustomerDAO customerDAO = new CustomerDAO();
		
		String idStr = request.getParameter("id");

		if (idStr != null && !idStr.isEmpty()) {
		    try {
		        int id = Integer.parseInt(idStr);
				customerDAO.delete(id);
		        
		    } catch (NumberFormatException e) {
		     
		    }
		}
		
	}

}
