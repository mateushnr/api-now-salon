package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.EmployeeHasService;
import model.jdbc.ConnectionFactory;

public class EmployeeHasServiceDAO {
	public void insert(EmployeeHasService job) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO funcionario_has_servico(servico_idservico, funcionario_matriculafuncionario) " 
												+ "VALUES(?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, job.getIdService());
			pstmt.setInt(2, job.getIdEmployee());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void delete(int idService, int idEmployee) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "DELETE FROM funcionario_has_servico "
							+ "WHERE servico_idservico = ? AND funcionario_matriculafuncionario = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, idService);
			pstmt.setInt(2, idEmployee);
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public ArrayList<EmployeeHasService> selectJobsByEmployee(int idEmployee) {
		ArrayList<EmployeeHasService> employeeJobs = new ArrayList<>();
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT servico_idservico, funcionario_matriculafuncionario "
							+ "FROM funcionario_has_servico "
								+ "WHERE funcionario_matriculafuncionario = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, idEmployee);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				EmployeeHasService employeeJob = new EmployeeHasService();
            
            	employeeJob.setIdEmployee(idEmployee);
            	employeeJob.setIdService(rs.getInt("servico_idservico"));

    			employeeJobs.add(employeeJob);
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return employeeJobs;
	}
}

