package es.dsw.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.dsw.connection.MySqlConnection;
import es.dsw.models.Repository;

@Controller
public class RepoController {

	@GetMapping("/repolist")
	public String getRepoList(Model model) {
		
		MySqlConnection objMySqlConnection = new MySqlConnection();
	    objMySqlConnection.open();
	    
	    List<Repository> listaPeliculas = new ArrayList<Repository>();
	    
	    String sql = """
	    		SELECT rf.title_rf AS Titulo, rf.synopsis_rf AS Sinopsis, gf.genre_gf AS Genero, rf.director_rf AS Director, rf.reparto_rf AS Reparto, rf.anio_rf AS Año, rf.datepremiere_rf AS 'Fecha estreno', pf.producer_pf AS Productor, cf.country_cf AS Pais
				FROM db_filmcinema.repository_film rf
				JOIN db_filmcinema.genre_film gf ON rf.idgenre_rf = gf.idgenre_gf
				LEFT JOIN db_filmcinema.producer_film pf ON rf.idproducer_rf = pf.idproducer_pf
				JOIN db_filmcinema.country_film cf ON rf.idcountry_rf = cf.idcountry_cf;
	    		""";
	    
	    try (ResultSet rs = objMySqlConnection.executeSelect(sql)) {
            while (rs != null && rs.next()) {
                String title = rs.getString("Titulo");
                String sinopsis = rs.getString("Sinopsis");
                String genre = rs.getString("Genero");
                String director = rs.getString("Director");
                String reparto = rs.getString("Reparto");
                String year = rs.getString("Año");
                String datePremiere = rs.getString("Fecha estreno");
                String producer = rs.getString("Productor");
                String country = rs.getString("Pais");
                
                Repository repository = new Repository(title, sinopsis, genre, director, reparto, year, datePremiere, producer, country);
                listaPeliculas.add(repository);      
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	model.addAttribute("lista", listaPeliculas);
            objMySqlConnection.close();
        }
		
		return "subviews/repolist";
	}
	
}
