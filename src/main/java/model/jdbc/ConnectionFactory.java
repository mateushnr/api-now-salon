package model.jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	public Connection getConnection(){
		String stringJDBC = "jdbc:postgresql://localhost:5432/now-salon";
		String usuario = "postgres";
		String senha = "root";

		Connection conexao = null;
		
		try {
			Class.forName("org.postgresql.Driver");
			conexao = DriverManager.getConnection(stringJDBC, usuario, senha);
		}catch(SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		
		return conexao;
	}
}
