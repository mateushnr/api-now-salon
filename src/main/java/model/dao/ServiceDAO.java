package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import model.Service;
import model.jdbc.ConnectionFactory;

public class ServiceDAO {
	public void insert(Service service) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO servico(nome, descricao, statusservico, "
											+ "tempoestimado, preco) " 
												+ "VALUES(?,?,?,?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			Time estimatedTime = Time.valueOf(service.getEstimatedTime().concat(":00"));
			
			pstmt.setString(1, service.getName());
			pstmt.setString(2, service.getDescription());
			pstmt.setString(3, service.getStatus());
			pstmt.setTime(4, estimatedTime);
			pstmt.setFloat(5, service.getPrice());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update(Service service) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE servico SET nome = ?, descricao = ?, statusservico = ?, "
											+ "tempoestimado = ?, preco = ? "
												+ "WHERE idservico = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			Time estimatedTime = Time.valueOf(service.getEstimatedTime().concat(":00"));
			
			pstmt.setString(1, service.getName());
			pstmt.setString(2, service.getDescription());
			pstmt.setString(3, service.getStatus());
			pstmt.setTime(4, estimatedTime);
			pstmt.setFloat(5, service.getPrice());
			pstmt.setInt(6, service.getId());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void delete(int id) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "DELETE FROM servico "
							+ "WHERE idservico = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Service selectById(int id) {
		Service serviceResult = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT nome, descricao, statusservico, "
								+ "tempoestimado, preco "
									+ "FROM servico WHERE idservico = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	serviceResult = new Service();
            	
            	serviceResult.setId(id);
    			serviceResult.setName(rs.getString("nome"));
    			serviceResult.setDescription(rs.getString("descricao"));
    			serviceResult.setStatus(rs.getString("statusservico"));
    			serviceResult.setEstimatedTime(rs.getString("tempoestimado").substring(0, 5));
    			serviceResult.setPrice(rs.getFloat("preco"));
            }
			
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return serviceResult;
	}
	
	public ArrayList<Service> selectAll(){
		ArrayList<Service> services = new ArrayList<>();
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idservico, nome, descricao, "
								+ "statusservico, tempoestimado, preco "
									+ "FROM servico";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Service service = new Service();
            
            	service.setId(rs.getInt("idservico"));
            	service.setName(rs.getString("nome"));
    			service.setDescription(rs.getString("descricao"));
    			service.setStatus(rs.getString("statusservico"));
    			service.setEstimatedTime(rs.getString("tempoestimado").substring(0, 5));
    			service.setPrice(rs.getFloat("preco"));
    			
    			services.add(service);
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return services;
	}
}
