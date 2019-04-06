package es.upm.etsiinf.bbdd.proyecto.conexion;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.PreparedStatement;
import java.time.LocalDate;


public class Conexion {
	private static Connection conn = null;
	private static String servidor, usuario, contrasena, baseDeDatos;

	private static void conectar (){
		if (conn==null){
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
	
				conn = DriverManager.getConnection("jdbc:mysql://"+servidor+"/"+baseDeDatos+"?" +       
						"user="+usuario+"&password=" + contrasena +
						"&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC");
			} catch (SQLException ex) { 
				System.out.println("SQLException: " + ex.getMessage());     
				System.out.println("SQLState: " + ex.getSQLState()); 
				System.out.println("VendorError: " + ex.getErrorCode()); 
			}catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void init (String servidor, String usuario, String contrasena, String baseDeDatos){
		Conexion.servidor = servidor;
		Conexion.usuario = usuario; 
		Conexion.contrasena = contrasena;
		Conexion.baseDeDatos = baseDeDatos;
		
		if (conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;
		}
	}


	public static void insertVinos (String dir,String tabla){
		if (conn == null)
			conectar();
		
		
		File file = new File(dir);
		BufferedReader reader=null;
		Statement stmt = null; 
		
		try { 
			stmt = conn.createStatement() ;
	
			reader = new BufferedReader(new FileReader(file));
			String line=reader.readLine();
			while((line=reader.readLine()) != null){
				stmt.execute("INSERT INTO " + tabla + " VALUES(" +insertInstructionFromCSV(line)+");");
				}

		} 
		catch (SQLException ex){ 
			System.out.println("SQLException: " + ex.getMessage());
		} catch (FileNotFoundException eFile) {
			eFile.printStackTrace();
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} finally {  
			if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) { }  stmt = null; }
			try {
				reader.close();
				} catch (Exception e) {
				}
		}

		
	}
	//Puede que sea mejor cambiarla con prepared statement
	private static String insertInstructionFromCSV(String line) {
		return (line.replace(';', ',')).replace('\"','\'');
	}
	
	public static String numVinos() {
		if (conn == null)
			conectar();
		String resultado="nVinos\n";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			
			rs=stmt.executeQuery("SELECT COUNT(*) as nVinos FROM wine;");
			
			if(rs.next()) {
				resultado+=rs.getString("nVinos");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		
		return resultado;
	}
	
	public static String tablaVinosNombreDescr(String condicion) {
		if (conn == null)
			conectar();
		
		String resultado=
				"wine_id        "
				+ "      title          "
				+ "                                            description";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =  conn.prepareStatement("SELECT wine_id,title,description FROM wine WHERE title = ? OR description = ?;");
			stmt.setString(1, condicion);
			stmt.setString(2, condicion);
			rs=stmt.executeQuery();
			while(rs.next()) {
				resultado+="\n" + rs.getString("wine_id") +
						"         " + rs.getString("title")
						+ "      " + rs.getString("description");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		
		return resultado;
	}
	
	
	public static String valoracionVinos(int condicion) {
		if (conn == null)
			conectar();
		
		String resultado="score";
				
				
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =  conn.prepareStatement("SELECT score FROM wine_user_review WHERE wine_id=?;");
			stmt.setInt(1, condicion);
			
			rs=stmt.executeQuery();
			while(rs.next()) {
				resultado+="\n" + rs.getString("score");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		
		return resultado;
	}
	
	public static String valoracionVinosPRO(int condicion) {
		if (conn == null)
			conectar();
		
		String resultado="num_val_usr   avg_usr   val_prof";
				
				
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt =  conn.prepareStatement(
					"SELECT COUNT(*) as num_val_usr, AVG(wur.score) as avg_usr,wsg.score as val_prof "
					+"FROM wine_scoring_guide as wsg "
					+"JOIN wine_user_review as wur on wur.wine_id = wsg.wine_id "
					+"WHERE wsg.wine_id=?;");
			stmt.setInt(1, condicion);
			
			rs=stmt.executeQuery();
			if(rs.next()) {
				resultado+="\n" + rs.getString("num_val_usr")
							+ "          " + rs.getString("avg_usr")
							+ "     " + rs.getString("val_prof") ;
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		
		return resultado;
	}
	
	public static Wine vinoPorId(int wine_id) {
		if (conn == null)
			conectar();
		Wine resultado = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement("SELECT * FROM wine WHERE wine_id = ?");
			stmt.setInt(1, wine_id);
			rs=stmt.executeQuery();
			
			if(rs.next()) {
				resultado = new Wine(rs.getInt("wine_id"), rs.getInt("grape_variety_id"), rs.getInt("winery_id"), rs.getInt("region_id"),
						rs.getString("name"), rs.getString("title"), rs.getString("designation"), rs.getString("description"));
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		return resultado;
	} 
	
	public static Wine[] vinosNombreDescr(String condicion) {
		if (conn == null)
			conectar();
		
		Wine[] resultado= null;
		PreparedStatement stmtTabla = null;
		PreparedStatement stmtNumFil = null;
		ResultSet rs = null;
		try {
			stmtNumFil =  conn.prepareStatement("SELECT COUNT(*) as num FROM wine WHERE title = ? OR description = ?;");
			stmtNumFil.setString(1, condicion);
			stmtNumFil.setString(2, condicion);
			rs=stmtNumFil.executeQuery();
			if(rs.next())
				resultado= new Wine[rs.getInt("num")];
			
			stmtTabla =  conn.prepareStatement("SELECT * FROM wine WHERE title = ? OR description = ?;");
			stmtTabla.setString(1, condicion);
			stmtTabla.setString(2, condicion);
			rs=stmtTabla.executeQuery();
			
			for(int i =0; rs.next(); i++){
				resultado[i] =new Wine(rs.getInt("wine_id"), rs.getInt("grape_variety_id"), rs.getInt("winery_id"),
							rs.getInt("region_id"),rs.getString("name"), rs.getString("title"), 
							rs.getString("designation"), rs.getString("description"));
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmtTabla!=null){
				try{stmtTabla.close();
				}catch(SQLException sqlEx){}
				stmtTabla=null;
			}
			if (stmtNumFil!=null){
				try{stmtNumFil.close();
				}catch(SQLException sqlEx){}
				stmtNumFil=null;
			}
		}
		
		return resultado;
	}
	
	
	public static User[] usersVal(int wine_id) {
		if (conn == null)
			conectar();
		
		User[] resultado= null;
		PreparedStatement stmtTabla = null;
		PreparedStatement stmtNumFil = null;
		ResultSet rs = null;
		try {
			stmtNumFil =  conn.prepareStatement(
					"SELECT COUNT(*) AS num "
					+"FROM user as u "
					+"JOIN wine_user_review as wu on u.user_id=wu.user_id "
					+"WHERE wu.wine_id=?;");
			stmtNumFil.setInt(1, wine_id);
			rs=stmtNumFil.executeQuery();
			if(rs.next())
				resultado= new User[rs.getInt("num")];
			
			stmtTabla =  conn.prepareStatement(
					"SELECT * "
					+"FROM user as u "
					+"JOIN wine_user_review as wu on u.user_id=wu.user_id "
					+"WHERE wu.wine_id=?;");
			stmtTabla.setInt(1, wine_id);
			rs=stmtTabla.executeQuery();
			
			for(int i =0; rs.next(); i++){
				resultado[i] =new User(rs.getInt("user_id"), rs.getString("name"));
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmtTabla!=null){
				try{stmtTabla.close();
				}catch(SQLException sqlEx){}
				stmtTabla=null;
			}
			if (stmtNumFil!=null){
				try{stmtNumFil.close();
				}catch(SQLException sqlEx){}
				stmtNumFil=null;
			}
		}
		
		return resultado;
	}
	
	

	public static String usuariosSinVal() {
		if (conn == null)
			conectar();
		String resultado="user_id   name";
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			
			rs=stmt.executeQuery(
					"SELECT user_id,name "
					+"FROM user "
					+"WHERE user_id not in ("
					+"SELECT u.user_id FROM user as u JOIN wine_user_review as ur on ur.user_id=u.user_id);");
			
			while(rs.next()) {
				resultado+="\n"+rs.getString("user_id")+"     "
							+ " "+rs.getString("name");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		return resultado;
	}
	
	public static String usuariosMasVal2018() {
		if (conn == null)
			conectar();
		String resultado="user_id   name";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT max(num) as maximo FROM (SELECT COUNT(*) as num "
					+ "FROM wine_user_review "
					+ "WHERE year(date)=2018"
					+ " GROUP BY user_id) as sqMax;");
			
			rs=stmt.executeQuery();
			int maximo=0;
			if(rs.next()) maximo = rs.getInt("maximo");
			
			stmt = conn.prepareStatement(
					"SELECT u.user_id, MIN(u.name) as nombre "
					+ "FROM user as u"
					+ "JOIN wine_scoring_guide as wsu on wsu.user_id=u.user_d"
					+ "WHERE year(u.date)=2018 "
					+ "GROUP BY u.user_id "
					+ "HAVING COUNT(*)=?;");
			
			stmt.setInt(1, maximo);
			rs=stmt.executeQuery();
			while(rs.next()) {
				resultado+="\n"+rs.getString("user_id")+"     "
							+ " "+rs.getString("name");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		return resultado;
	}
	
	public static String wineryReg() {
		if (conn == null)
			conectar();
		String resultado="winery_id   name     numPaises     regiones ";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT wy.winery_id, MIN(wy.name) AS name," + 
					"group_concat(distinct r.name) as regiones, COUNT(distinct c.country_id) as numPaises " + 
					"FROM winery as wy " + 
					"JOIN wine as w on w.winery_id=wy.winery_id " + 
					"JOIN region as r on r.region_id = w.region_id " + 
					"JOIN country as c on c.country_id= r.country_id " + 
					"GROUP By wy.winery_id " + 
					"HAVING numPaises>1;");
			
			rs=stmt.executeQuery();
		
			
			while(rs.next()) {
				resultado+="\n"+rs.getString("winery_id")+"     "
							+ " "+rs.getString("name")
							+"      "+ rs.getString("numPaises")
							+"         "+ rs.getString("regiones");
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		return resultado;
	}
	
	
	public static String usuariosMinDesvPro() {
		if (conn == null)
			conectar();
		String resultado="user_id   name     num_vinos      desviacion_media";
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(
					"SELECT MIN(desviacion_media) as minimo FROM("
					+"SELECT AVG(ABS(wur.score-wsg.score)) AS desviacion_media "
					+"FROM wine_user_review as wur "
					+"JOIN wine_scoring_guide as wsg on wsg.wine_id = wur.wine_id "
					+"GROUP BY wur.user_id) as sqMDesv;");

			rs=stmt.executeQuery();
			BigDecimal minimo=null;
			if(rs.next()) minimo = rs.getBigDecimal("minimo");
			
			stmt = conn.prepareStatement(
					"SELECT wur.user_id, MIN(u.name) as name, "
					+"COUNT(*) as num_vinos, AVG(ABS(wur.score-wsg.score)) AS desviacion_media "
					+"FROM wine_user_review as wur "
					+"JOIN wine_scoring_guide as wsg on wsg.wine_id = wur.wine_id "
					+"JOIN user as u on u.user_id=wur.user_id "
					+"GROUP BY wur.user_id "
					+"HAVING desviacion_media= ?;");
			
			stmt.setBigDecimal(1, minimo); 
			rs=stmt.executeQuery();
			while(rs.next()) {
				resultado+="\n"+rs.getString("user_id")
							+"     "+rs.getString("name")
							+"   "+ rs.getString("num_vinos")
							+"    " +rs.getString("desviacion_media");   
			}
		}catch(SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
		}finally {
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
		}
		return resultado;
	}
	
	
	public static void actualizarVinos(String dir) {
		if (conn == null)
			conectar();
		
		
		File file = new File(dir);
		BufferedReader reader=null;
		PreparedStatement stmt = null; 
		ResultSet rs=null;
		
		try { 
			reader = new BufferedReader(new FileReader(file));
			String line=reader.readLine();
			
			
			
			while((line=reader.readLine()) != null){
				//Lo desctivamos para gestioar las transacciones
				conn.setAutoCommit(false);
				
				String [] fila = line.split(";");
				//Booleano auxiliar para detenrminar si actualizar o no 
				boolean hayCritico=false;

				stmt = conn.prepareStatement("SELECT grape_variety_id FROM grape_variety WHERE name =?");
				//variety
				stmt.setString(1, fila[1]);
				rs=stmt.executeQuery();
				int grape_variety_id=0;
				if(rs.next())
					grape_variety_id=rs.getInt("grape_variety_id");
				
				stmt = conn.prepareStatement("SELECT winery_id FROM winery WHERE name =?");
				//winery
				stmt.setString(1, fila[2]);
				rs=stmt.executeQuery();
				int winery_id=0;
				if(rs.next())
					winery_id=rs.getInt("winery_id");
				
				stmt = conn.prepareStatement(
						"SELECT region_id "
						+"FROM region "
						+"WHERE name = ? and area =? and province =? and country_id = ("
						+"SELECT country_id "
						+"FROM  country "
						+"WHERE name_es=? or name_en=? or name_fr=?);");
				//nombre provincia
				stmt.setString(1, fila[8]);
				//area
				stmt.setString(2, fila[10]);
				//region
				stmt.setString(3, fila[9]);
				//country en los posibles idiomas
				stmt.setString(4, fila[3]);
				stmt.setString(5, fila[3]);
				stmt.setString(6, fila[3]);
				rs=stmt.executeQuery();
				int region_id=0;
				if(rs.next())
					winery_id=rs.getInt("region_id");
				
				stmt = conn.prepareStatement(
						"SELECT taster_id "
						+"FROM taster "
						+"WHERE name = ? and twitter_handle =?;");
				//nombre
				stmt.setString(1, fila[11]);
				//twitter
				stmt.setString(2, fila[12]);
				rs=stmt.executeQuery();
				int taster_id=0;
				if(rs.next())
					winery_id=rs.getInt("taster_id");
				
				stmt = conn.prepareStatement("SELECT wine_id FROM wine WHERE title =?");
				//title
				stmt.setString(1, fila[0]);
				rs=stmt.executeQuery();
				int wine_id=0;
				
				if(rs.next()) {
					//Existe un vino con ese nombre
					wine_id=rs.getInt("wine_id");
					//Actualizamos info relevante a wine 
					//con los parametros obtenidos previamente
					stmt=conn.prepareStatement(
							"UPDATE wine "
							+ "SET 	description=?, grape_variety_id=?, designation=?, "
							+ "winery_id=?, region_id=? "
							+ "WHERE wine_id=?");
					stmt.setString(1, fila[4]);
					stmt.setInt(2, grape_variety_id);
					stmt.setString(3, fila[5]);
					stmt.setInt(4, winery_id);
					stmt.setInt(5, region_id);
					stmt.setInt(6, wine_id);
					stmt.executeUpdate();
					
					//Comprobar si hay review de dicho vino
					stmt=conn.prepareStatement("SELECT taster FROM wine_scoring_guide WHERE wine_id =?");
					stmt.setInt(1,wine_id);
					rs=stmt.executeQuery();
					hayCritico=rs.next();
				
				}else {
					stmt=conn.prepareStatement(
								"INSERT wine(title, description, grape_variety_id, "
								+ "designation, winery_id, region_id) "
								+ "VALUES(?,?,?,?,?,?);");
					
					stmt.setString(1, fila[0]);
					stmt.setString(2, fila[4]);
					stmt.setInt(3, grape_variety_id);
					stmt.setString(4, fila[5]);
					stmt.setInt(5, winery_id);
					stmt.setInt(6, region_id);
					stmt.execute();
				}
				
				 if (hayCritico) {
					//Actualizamos info relevante a la review 
					//con los parametros obtenidos previamente
					stmt=conn.prepareStatement(
							"UPDATE wine_scoring_guide "
							+ "SET 	taster_id=?,date = NOW(),score=?, price=?;");
					stmt.setInt(1, taster_id);
					stmt.setObject(2, (fila[6].equals("NULL")? null: new BigDecimal(fila[6])));
					stmt.setObject(3, (fila[7].equals("NULL")? null: new BigDecimal(fila[7])));
					stmt.executeUpdate();
				}else {
					stmt = conn.prepareStatement("SELECT wine_id FROM wine WHERE title =?");
					//title
					stmt.setString(1, fila[0]);
					rs=stmt.executeQuery();
					
					if(rs.next()) 
						wine_id=rs.getInt("wine_id");
					
					stmt=conn.prepareStatement(
							"INSERT wine_scoring_guide "
							+ "VALUES(?,?,NOW(),?,?);");
				
					stmt.setInt(1, wine_id);
					stmt.setInt(2,taster_id);
					stmt.setObject(3, (fila[6].equals("NULL")? null: new BigDecimal(fila[6])));
					stmt.setObject(4, (fila[7].equals("NULL")? null: new BigDecimal(fila[7])));
					stmt.execute();
				}
				
				 //Actualizar los cambios con la base de datos
				conn.commit();
			}	
			

		} 
		catch (SQLException ex){ 
			System.out.println("SQLException: " + ex.getMessage());
		} catch (FileNotFoundException eFile) {
			eFile.printStackTrace();
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} finally {  
			if (rs!=null){
				try{rs.close();
				}catch(SQLException sqlEx){}
				rs=null;
			}
			if (stmt!=null){
				try{stmt.close();
				}catch(SQLException sqlEx){}
				stmt=null;
			}
			//Activamos otra vez el autocomit
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e1) {
			}
			try {
				reader.close();
				} catch (Exception e) {
				}
		}

		
	}
	
	
	
	
	
	public static void insertVinos2(String dir,String tabla){
		if (conn == null)
			conectar();
		
		
		File file = new File(dir);
		BufferedReader reader=null;
		PreparedStatement stmt = null; 
		
		try { 
			reader = new BufferedReader(new FileReader(file));
			String line=reader.readLine();
			
			if(tabla=="wine_user_review") {
				stmt = conn.prepareStatement("INSERT INTO " + tabla + " VALUES(?,?,?,?);") ;
		
				
				while((line=reader.readLine()) != null){
					String [] fila = insertInstructionFromCSV2(line);
					stmt.setInt(1, Integer.parseInt(fila[0]));
					System.out.println(fila[0]);
					stmt.setInt(2, Integer.parseInt(fila[1]));
					stmt.setObject(3,fila[2].equals("NULL")? null:Timestamp.valueOf(fila[2]));
					stmt.setObject(4,fila[3].equals("NULL")? null : new BigDecimal(fila[3]));
					stmt.execute();
				}	
			}else if (tabla=="wine_scoring_guide") {
				stmt = conn.prepareStatement("INSERT INTO " + tabla + " VALUES(?,?,?,?,?);") ;
				
				while((line=reader.readLine()) != null){
					String [] fila = insertInstructionFromCSV2(line);
					stmt.setInt(1, Integer.parseInt(fila[0]));
					System.out.println(fila[0]);
					stmt.setObject(2, fila[1].equals("NULL")? null:Integer.parseInt(fila[1]));
					stmt.setObject(3,fila[2].equals("NULL")? null:Timestamp.valueOf(fila[2]));
					stmt.setObject(4, fila[3].equals("NULL")? null: new BigDecimal(fila[3]));
					stmt.setObject(5, (fila[4].equals("NULL")? null: new BigDecimal(fila[4])));
					stmt.execute();
				}	
			}else
				throw new IllegalArgumentException();

		} 
		catch (SQLException ex){ 
			System.out.println("SQLException: " + ex.getMessage());
		} catch (FileNotFoundException eFile) {
			eFile.printStackTrace();
		} catch (IOException eIO) {
			eIO.printStackTrace();
		} finally {  
			if (stmt != null) { try { stmt.close(); } catch (SQLException sqlEx) { }  stmt = null; }
			try {
				reader.close();
				} catch (Exception e) {
				}
		}

		
	}
	
	public static String[] insertInstructionFromCSV2(String line) {
		return (line.replaceAll("\"","")).split(";");
	}
	
	
	
	 
	
	
	
}
