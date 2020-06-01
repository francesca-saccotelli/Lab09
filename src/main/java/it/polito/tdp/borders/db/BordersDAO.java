package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public List<Country> loadAllCountries() {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		List<Country> result = new ArrayList<Country>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				System.out.format("%d %s %s\n", rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
			}
			
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Country> getCountriesFromYear(int anno,Map<Integer,Country> countriesMap) {
		String sql = "select * from country " + 
				"where CCode in ( " + 
				"select state1no " + 
				"from contiguity " + 
				"where year<=?)" ;
		
		try {
			Connection conn = ConnectDB.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno);
			ResultSet rs = st.executeQuery() ;
			
			List<Country> list = new LinkedList<Country>() ;
			
			while( rs.next() ) {
				
				if(countriesMap.get(rs.getInt("ccode")) == null){
					Country c = new Country(
							rs.getInt("ccode"),
							rs.getString("StateAbb"), 
							rs.getString("StateNme")) ;
					countriesMap.put(c.getcCode(), c);
					list.add(c);
				} else 
					list.add(countriesMap.get(rs.getInt("ccode")));
			}
			
			conn.close() ;
			
			return list ;
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null ;

	}

	public List<Border> getCountryPairs(int anno) {

		String sql="SELECT state1no,state2no " + 
				"FROM contiguity " + 
				"WHERE YEAR<=? AND " + 
				"conttype=1 AND " + 
				"state1no<state2no";
		List<Border>border=new ArrayList<>();
		
		try {
			Connection conn = ConnectDB.getConnection() ;
            PreparedStatement st = conn.prepareStatement(sql) ;
            st.setInt(1, anno);
			ResultSet rs = st.executeQuery() ;
			
			while(rs.next()) {
				border.add(new Border(rs.getInt("state1no"), rs.getInt("state2no"))) ;
			}
			
			conn.close();
			return border ;
			
		}catch(SQLException e){
			e.printStackTrace();
			return null ;
		}
		
	}
	
}
