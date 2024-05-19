package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import model.Schedule;
import model.jdbc.ConnectionFactory;

public class ScheduleDAO {
	public void insert(Schedule schedule) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "INSERT INTO agendamento(cliente_idcliente, funcionario_matriculafuncionario, "
											+ "servico_idservico, dataagendamento, horarioagendamento, "
											+ "statusagendamento, observacoes, motivocancelamento, quemcancelou) " 
												+ "VALUES(?,?,?,?,?,?,?,?,?)";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			Date dateSchedule = Date.valueOf(schedule.getDateSchedule());
			Time hourSchedule = Time.valueOf(schedule.getHourSchedule().concat(":00"));
			
			pstmt.setInt(1, schedule.getIdCustomer());
			pstmt.setInt(2, schedule.getIdEmployee());
			pstmt.setInt(3, schedule.getIdService());
			pstmt.setDate(4, dateSchedule);
			pstmt.setTime(5, hourSchedule);
			pstmt.setString(6, schedule.getStatus());
			pstmt.setString(7, schedule.getObservation());
			pstmt.setString(8, schedule.getReasonCancellation());
			pstmt.setString(9, schedule.getWhoCancelled());
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void update(Schedule schedule) {
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "UPDATE agendamento SET funcionario_matriculafuncionario = ?, servico_idservico = ?, "
											+ "dataagendamento = ?, horarioagendamento = ?, statusagendamento = ?, "
											+ "observacoes = ?, motivocancelamento = ?, quemcancelou = ? "
												+ "WHERE idagendamento = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			Date dateSchedule = Date.valueOf(schedule.getDateSchedule());
			Time hourSchedule = Time.valueOf(schedule.getHourSchedule().concat(":00"));
			
			pstmt.setInt(1, schedule.getIdEmployee());
			pstmt.setInt(2, schedule.getIdService());
			pstmt.setDate(3, dateSchedule);
			pstmt.setTime(4, hourSchedule);
			pstmt.setString(5, schedule.getStatus());
			pstmt.setString(6, schedule.getObservation());
			pstmt.setString(7, schedule.getReasonCancellation());
			pstmt.setString(8, schedule.getWhoCancelled());
			pstmt.setInt(9, schedule.getId());
			
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
			
			String sql = "DELETE FROM agendamento "
							+ "WHERE idagendamento = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);
			
			pstmt.execute();
			
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public Schedule selectById(int id){
		Schedule schedule = null;
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT a.idagendamento, a.cliente_idcliente, "
								+ "a.funcionario_matriculafuncionario, a.servico_idservico, "
								+ "(SELECT c.nome FROM cliente c "
									+ "WHERE c.idcliente = a.cliente_idcliente) "
										+ "AS nomecliente, "
								+ "(SELECT f.nome FROM funcionario f "
									+ "WHERE f.matriculafuncionario = a.funcionario_matriculafuncionario) "
										+ "AS nomefuncionario, "
								+ "(SELECT s.nome FROM servico s "
									+ "WHERE s.idservico = a.servico_idservico) "
										+ "AS nomeservico, "
								+ "(SELECT c.telefone FROM cliente c "
									+ "WHERE c.idcliente = a.cliente_idcliente) "
										+ "AS telefonecliente, "
								+ "(SELECT s.preco FROM servico s "
									+ "WHERE s.idservico = a.servico_idservico) "
										+ "AS precoservico, "	
								+ "a.dataagendamento, a.horarioagendamento, "
								+ "a.statusagendamento, a.observacoes, "
								+ "a.motivocancelamento, a.quemcancelou "
									+ "FROM agendamento a "
										+ "WHERE idagendamento = ?";
			
			PreparedStatement pstmt = connection.prepareStatement(sql);
			
			pstmt.setInt(1, id);

			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				schedule = new Schedule();
            
            	schedule.setId(rs.getInt("idagendamento"));
            	schedule.setIdCustomer(rs.getInt("cliente_idcliente"));
            	schedule.setIdEmployee(rs.getInt("funcionario_matriculafuncionario"));
            	schedule.setIdService(rs.getInt("servico_idservico"));
            	schedule.setCustomerName(rs.getString("nomecliente"));
            	schedule.setEmployeeName(rs.getString("nomefuncionario"));
            	schedule.setServiceName(rs.getString("nomeservico"));
            	schedule.setCustomerPhone(rs.getString("telefonecliente"));
            	schedule.setServicePrice(rs.getFloat("precoservico"));
            	schedule.setDateSchedule(rs.getString("dataagendamento"));
            	schedule.setHourSchedule(rs.getString("horarioagendamento").substring(0, 5));
            	schedule.setStatus(rs.getString("statusagendamento"));
            	schedule.setObservation(rs.getString("observacoes"));
            	schedule.setReasonCancellation(rs.getString("motivocancelamento"));
            	schedule.setWhoCancelled(rs.getString("quemcancelou"));
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return schedule;
	}
	
	public ArrayList<Schedule> selectAll(){
		ArrayList<Schedule> schedules = new ArrayList<>();
		
		try {
			Connection connection = new ConnectionFactory().getConnection();
			
			String sql = "SELECT a.idagendamento, a.cliente_idcliente, "
								+ "a.funcionario_matriculafuncionario, a.servico_idservico, "
								+ "(SELECT c.nome FROM cliente c "
									+ "WHERE c.idcliente = a.cliente_idcliente) "
										+ "AS nomecliente, "
								+ "(SELECT f.nome FROM funcionario f "
									+ "WHERE f.matriculafuncionario = a.funcionario_matriculafuncionario) "
										+ "AS nomefuncionario, "
								+ "(SELECT s.nome FROM servico s "
									+ "WHERE s.idservico = a.servico_idservico) "
										+ "AS nomeservico, "
								+ "(SELECT c.telefone FROM cliente c "
									+ "WHERE c.idcliente = a.cliente_idcliente) "
										+ "AS telefonecliente, "
								+ "(SELECT s.preco FROM servico s "
									+ "WHERE s.idservico = a.servico_idservico) "
										+ "AS precoservico, "	
								+ "a.dataagendamento, a.horarioagendamento, "
								+ "a.statusagendamento, a.observacoes, "
								+ "a.motivocancelamento, a.quemcancelou "
									+ "FROM agendamento a";
						
			
			PreparedStatement pstmt = connection.prepareStatement(sql);

			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Schedule schedule = new Schedule();
            
            	schedule.setId(rs.getInt("idagendamento"));
            	schedule.setIdCustomer(rs.getInt("cliente_idcliente"));
            	schedule.setIdEmployee(rs.getInt("funcionario_matriculafuncionario"));
            	schedule.setIdService(rs.getInt("servico_idservico"));
            	schedule.setCustomerName(rs.getString("nomecliente"));
            	schedule.setEmployeeName(rs.getString("nomefuncionario"));
            	schedule.setServiceName(rs.getString("nomeservico"));
            	schedule.setCustomerPhone(rs.getString("telefonecliente"));
            	schedule.setServicePrice(rs.getFloat("precoservico"));
            	schedule.setDateSchedule(rs.getString("dataagendamento"));
            	schedule.setHourSchedule(rs.getString("horarioagendamento").substring(0, 5));
            	schedule.setStatus(rs.getString("statusagendamento"));
            	schedule.setObservation(rs.getString("observacoes"));
            	schedule.setReasonCancellation(rs.getString("motivocancelamento"));
            	schedule.setWhoCancelled(rs.getString("quemcancelou"));
    			
    			schedules.add(schedule);
			}
        
			rs.close();
			pstmt.close();
			connection.close();
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return schedules;
	}
}
