package model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


import java.sql.ResultSet;

import model.Employee;
import model.jdbc.ConnectionFactory;

public class EmployeeDAO {
	public Employee insert(Employee employee) {
		Employee insertedEmployee = null;
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO funcionario(nome, telefone, cargo, "
												+ "nivelacesso, senha) " 
													+ "VALUES(?,?,?,?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, employee.getName());
			pstmt.setString(2, employee.getPhone());
			pstmt.setString(3, employee.getRole());
			pstmt.setString(4, employee.getAccessLevel());
			pstmt.setString(5, employee.getPassword());
			
			int result = pstmt.executeUpdate();
				
			if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
    
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt("matriculafuncionario");
                        
                        insertedEmployee = new Employee();
                        insertedEmployee.setRegistration(generatedId);
                    }
                }
            }
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return insertedEmployee;
	}
	
	public void update(Employee employee) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql;
			PreparedStatement pstmt;
			
			if(employee.getPassword() != null) {
				sql = "UPDATE funcionario "
						+ "SET nome = ?, telefone = ?, cargo = ?, "
						+ "nivelacesso = ?, senha = ? "
							+ "WHERE matriculafuncionario = ?";
				
				pstmt = connection.prepareStatement(sql);
				
				pstmt.setString(1, employee.getName());
				pstmt.setString(2, employee.getPhone());
				pstmt.setString(3, employee.getRole());
				pstmt.setString(4, employee.getAccessLevel());
				pstmt.setString(5, employee.getPassword());
				pstmt.setInt(6, employee.getRegistration());
			}else {
				sql = "UPDATE funcionario "
						+ "SET nome = ?, telefone = ?, cargo = ?, "
						+ "nivelacesso = ? "
							+ "WHERE matriculafuncionario = ?";
				
				pstmt = connection.prepareStatement(sql);
				
				pstmt.setString(1, employee.getName());
				pstmt.setString(2, employee.getPhone());
				pstmt.setString(3, employee.getRole());
				pstmt.setString(4, employee.getAccessLevel());
				pstmt.setInt(5, employee.getRegistration());
			}
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void delete(int matricula) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "DELETE FROM funcionario "
							+ "WHERE matriculafuncionario = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, matricula);
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Employee selectById(int registration) {
		Employee employeeResult = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT nome, telefone, cargo, "
							+ "nivelacesso, senha "
								+ "FROM funcionario WHERE matriculafuncionario = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, registration);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	employeeResult = new Employee();
            	
            	employeeResult.setRegistration(registration);
            	employeeResult.setName(rs.getString("nome"));
            	employeeResult.setPhone(rs.getString("telefone"));
            	employeeResult.setRole(rs.getString("cargo"));
            	employeeResult.setAccessLevel(rs.getString("nivelacesso"));
            	employeeResult.setPassword(rs.getString("senha"));
            }
			
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return employeeResult;
	}
	
	public ArrayList<Employee> selectAll(){
		ArrayList<Employee> employees = new ArrayList<>();
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT matriculafuncionario, nome, telefone, "
								+ "cargo, nivelacesso "
									+ "FROM funcionario";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Employee employee = new Employee();
            
				employee.setRegistration(rs.getInt("matriculafuncionario"));
				employee.setName(rs.getString("nome"));
				employee.setPhone(rs.getString("telefone"));
				employee.setRole(rs.getString("cargo"));
				employee.setAccessLevel(rs.getString("nivelacesso"));
    			
            	employees.add(employee);
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return employees;
	}
	
	public void setToken(Employee employee, String token) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE funcionario "
							+ "SET idtoken = ? "
								+ "WHERE matriculafuncionario = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setString(1, token);
			pstmt.setInt(2, employee.getRegistration());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Employee authenticate(String token){
		Employee employeeResult = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT matriculafuncionario, nome, telefone, "
								+ "cargo, nivelacesso FROM funcionario "
									+ "WHERE idToken = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	employeeResult = new Employee();
            	
            	employeeResult.setRegistration(rs.getInt("matriculafuncionario"));
            	employeeResult.setName(rs.getString("nome"));
            	employeeResult.setPhone(rs.getString("telefone"));
            	employeeResult.setRole(rs.getString("cargo"));
            	employeeResult.setAccessLevel(rs.getString("nivelacesso"));
            }
			
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return employeeResult;
	}
}
