package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import model.Salon;
import model.jdbc.ConnectionFactory;

public class SalonDAO {
	public void insert(Salon salon) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO salao(telefone, nome, diassemanaaberto, "
											+ "horarioabrir, horariofechar, "
											+ "emailcontato, statussalao, "
											+ "endereco, bairro, cidadeestado) " 
												+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			Time timeOpen = Time.valueOf(salon.getTimeOpen().concat(":00"));
			Time timeClose = Time.valueOf(salon.getTimeClose().concat(":00"));
			
			pstmt.setString(1, salon.getPhone());
			pstmt.setString(2, salon.getName());
			pstmt.setString(3, salon.getDaysWeekOpen());
			pstmt.setTime(4, timeOpen);
			pstmt.setTime(5, timeClose);
			pstmt.setString(6, salon.getEmailContact());
			pstmt.setString(7, salon.getStatus());
			pstmt.setString(8, salon.getAddress());
			pstmt.setString(9, salon.getNeighborhood());
			pstmt.setString(10, salon.getCityState());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update(Salon salon) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE salao SET telefone = ?, nome = ?, diassemanaaberto = ?, "
										+ "horarioabrir = ?, horariofechar = ?, "
										+ "emailcontato = ?, statussalao = ?, "
										+ "endereco = ?, bairro = ?, "
										+ "cidadeestado = ? "
											+ "WHERE idsalao = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			Time timeOpen = Time.valueOf(salon.getTimeOpen().concat(":00"));
			Time timeClose = Time.valueOf(salon.getTimeClose().concat(":00"));
			
			pstmt.setString(1, salon.getPhone());
			pstmt.setString(2, salon.getName());
			pstmt.setString(3, salon.getDaysWeekOpen());
			pstmt.setTime(4, timeOpen);
			pstmt.setTime(5, timeClose);
			pstmt.setString(6, salon.getEmailContact());
			pstmt.setString(7, salon.getStatus());
			pstmt.setString(8, salon.getAddress());
			pstmt.setString(9, salon.getNeighborhood());
			pstmt.setString(10, salon.getCityState());
			pstmt.setInt(11, salon.getId());
			
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
			
			String sql = "DELETE FROM salao WHERE idsalao = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Salon selectById(int id) {
		Salon salonResult = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT telefone, nome, diassemanaaberto, "
								+ "horarioabrir, horariofechar, "
								+ "emailcontato, statussalao, "
								+ "endereco, bairro, cidadeestado"
									+ " FROM salao WHERE idsalao = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	salonResult = new Salon();
            	
            	salonResult.setId(id);
    			salonResult.setPhone(rs.getString("telefone"));
    			salonResult.setName(rs.getString("nome"));
    			salonResult.setDaysWeekOpen(rs.getString("diassemanaaberto"));
    			salonResult.setTimeOpen(rs.getString("horarioabrir").substring(0, 5));
    			salonResult.setTimeClose(rs.getString("horariofechar").substring(0, 5));
    			salonResult.setEmailContact(rs.getString("emailcontato"));
    			salonResult.setStatus(rs.getString("statussalao"));
    			salonResult.setAddress(rs.getString("endereco"));
    			salonResult.setNeighborhood(rs.getString("bairro"));
    			salonResult.setCityState(rs.getString("cidadeestado"));
            }
			
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return salonResult;
	}
	
	public Salon selectByName(String name) {
		Salon salonResult = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idsalao, telefone, diassemanaaberto, "
								+ "horarioabrir, horariofechar, "
								+ "emailcontato, statussalao, "
								+ "endereco, bairro, cidadeestado"
									+ " FROM salao WHERE nome = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setString(1, name);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	salonResult = new Salon();
            	
            	salonResult.setId(rs.getInt("idsalao"));
    			salonResult.setPhone(rs.getString("telefone"));
    			salonResult.setName(name);
    			salonResult.setDaysWeekOpen(rs.getString("diassemanaaberto"));
    			salonResult.setTimeOpen(rs.getString("horarioabrir").substring(0, 5));
    			salonResult.setTimeClose(rs.getString("horariofechar").substring(0, 5));
    			salonResult.setEmailContact(rs.getString("emailcontato"));
    			salonResult.setStatus(rs.getString("statussalao"));
    			salonResult.setAddress(rs.getString("endereco"));
    			salonResult.setNeighborhood(rs.getString("bairro"));
    			salonResult.setCityState(rs.getString("cidadeestado"));
            }
			
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return salonResult;
	}
	
	public ArrayList<Salon> selectAll(){
		ArrayList<Salon> salons = new ArrayList<>();
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idsalao, nome, telefone, diassemanaaberto, "
								+ "horarioabrir, horariofechar, "
								+ "emailcontato, statussalao, "
								+ "endereco, bairro, cidadeestado"
									+ " FROM salao";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Salon salon = new Salon();
            
				salon.setId(rs.getInt("idsalao"));
    			salon.setPhone(rs.getString("telefone"));
    			salon.setName(rs.getString("nome"));
    			salon.setDaysWeekOpen(rs.getString("diassemanaaberto"));
    			salon.setTimeOpen(rs.getString("horarioabrir").substring(0, 5));
    			salon.setTimeClose(rs.getString("horariofechar").substring(0, 5));
    			salon.setEmailContact(rs.getString("emailcontato"));
    			salon.setStatus(rs.getString("statussalao"));
    			salon.setAddress(rs.getString("endereco"));
    			salon.setNeighborhood(rs.getString("bairro"));
    			salon.setCityState(rs.getString("cidadeestado"));
    			
    			salons.add(salon);
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return salons;
	}
}
