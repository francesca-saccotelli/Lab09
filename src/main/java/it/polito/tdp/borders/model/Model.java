package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private Graph<Country,DefaultEdge>grafo;
	private Map<Integer,Country>countriesMap;
	
	public Model() {
	      this.countriesMap=new HashMap<>();
	}
	
	public void creaGrafo(int anno) {
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		BordersDAO dao=new BordersDAO();
		
		dao.getCountriesFromYear(anno, this.countriesMap);
		Graphs.addAllVertices(grafo, this.countriesMap.values());
		
		List<Border>border=dao.getCountryPairs(anno);
		for(Border b:border) {
			grafo.addEdge(this.countriesMap.get(b.getState1no()), 
					this.countriesMap.get(b.getState2no())) ;
		}
	}
	public List<Country> getCountries() {
		List<Country> countries = new ArrayList<Country>();
		countries.addAll(this.grafo.vertexSet());
		Collections.sort(countries);
		return countries;
	}
	
	public List<CountryAndNumber> getCountryAndNumber() {
		List<CountryAndNumber> list = new ArrayList<>() ;
		
		for(Country c: grafo.vertexSet()) {
			list.add(new CountryAndNumber(c, grafo.degreeOf(c))) ;
		}
		Collections.sort(list);
		return list ;
	}
	public int getNumeroComponentiConnesse() {
	    if(grafo==null) {
			throw new RuntimeException("Grafo non esistente");
		}
		ConnectivityInspector<Country,DefaultEdge>conInsp=new ConnectivityInspector<Country,DefaultEdge>(grafo);
		return conInsp.connectedSets().size();	}

}
