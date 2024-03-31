package model.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Customer;

import java.sql.ResultSet;

import model.jdbc.ConnectionFactory;

public class CustomerDAO {
	
	public void insert(Customer customer) {
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO cliente(nome, telefone, email, senha) " + "VALUES(?,?,?,?)";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getPhone());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			
			pstmt.execute();
			
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update(Customer customer) {
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE cliente SET nome = ?, telefone = ?, email = ?, senha = ? WHERE idCliente = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setString(1, customer.getName());
			pstmt.setString(2, customer.getPhone());
			pstmt.setString(3, customer.getEmail());
			pstmt.setString(4, customer.getPassword());
			pstmt.setInt(5, customer.getId());
			
			pstmt.execute();
			
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public void delete(int id) {
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "DELETE FROM cliente WHERE idCliente = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			pstmt.execute();
			
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Customer selectById(int id) {
		Customer customerResult = null;
		
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "SELECT nome, telefone, email FROM cliente WHERE idCliente = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	customerResult = new Customer();
            	
            	customerResult.setId(id);
    			customerResult.setName(rs.getString("nome"));
    			customerResult.setPhone(rs.getString("telefone"));
    			customerResult.setEmail(rs.getString("email"));
            }
			
			rs.close();
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return customerResult;
	}
	
	public Customer selectByEmail(String email) {
		Customer customerResult = null;
		
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idCliente, nome, telefone, email, senha FROM cliente WHERE email = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setString(1, email);
			
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	customerResult = new Customer();
            	
            	customerResult.setId(rs.getInt("idCliente"));
    			customerResult.setName(rs.getString("nome"));
    			customerResult.setPhone(rs.getString("telefone"));
    			customerResult.setEmail(rs.getString("email"));
    			customerResult.setPassword(rs.getString("senha"));
            }
			
			rs.close();
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return customerResult;
	}
	
	public ArrayList<Customer> selectAll(){
		ArrayList<Customer> customers = new ArrayList<>();
		
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idCliente, nome, telefone, email FROM cliente";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Customer customer = new Customer();
            
            	customer.setId(rs.getInt("idCliente"));
            	customer.setName(rs.getString("nome"));
            	customer.setPhone(rs.getString("telefone"));
            	customer.setEmail(rs.getString("email"));
    			
    			customers.add(customer);
			}
        
			rs.close();
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return customers;
	}
	
	public void setToken(Customer customer, String token) {
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE cliente SET idToken = ? WHERE idCliente = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			
			pstmt.setString(1, token);
			pstmt.setInt(2, customer.getId());
			
			pstmt.execute();
			
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			//e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}
	
	public Customer authenticate(String token){
		Customer customerResult = null;
		
		try {
			Connection conexao = new ConnectionFactory().getConnection();
			
			String sql = "SELECT idCliente, nome, telefone, email FROM cliente WHERE idToken = ?";
			
			PreparedStatement pstmt = conexao.prepareStatement(sql);
			pstmt.setString(1, token);
			ResultSet rs = pstmt.executeQuery();
			
            if(rs.next()) {
            	customerResult = new Customer();
            	
            	customerResult.setId(rs.getInt("idCliente"));
    			customerResult.setName(rs.getString("nome"));
    			customerResult.setPhone(rs.getString("telefone"));
    			customerResult.setEmail(rs.getString("email"));
            }
			
			rs.close();
			pstmt.close();
			conexao.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return customerResult;
	}
}
