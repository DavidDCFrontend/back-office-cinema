package es.dsw.controllers;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.dsw.connection.MySqlConnection;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("username")
public class MainController {

	@GetMapping({"/", "/home"})
	public String home(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = null;
		
        if (authentication != null && authentication.isAuthenticated()) {	
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {    	
                UserDetails userDetails = (UserDetails) principal;
                username = userDetails.getUsername();
                model.addAttribute("username", username);
            }
        }
        
        if(username != null) {
        	MySqlConnection objMySqlConnection = new MySqlConnection();
    	    objMySqlConnection.open();
    	    
    	    String sql = """
    	    		SELECT user_film.name_usf as name, user_film.firstsurname_usf as firstsurname
    	    		FROM db_filmcinema.user_film
    	    		WHERE user_film.username_usf = ?;
    	    		""";
    	    
    	    try (ResultSet rs = objMySqlConnection.executeSelect(sql, username)) {
                if (rs != null && rs.next()) {
                    String name = rs.getString("name");
                    String firstsurname = rs.getString("firstsurname");

                    // Agregar datos al modelo para usarlos en la vista
                    model.addAttribute("name", name);
                    model.addAttribute("firstsurname", firstsurname);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                objMySqlConnection.close();
            }
        }
        
        if(model.containsAttribute("name")) {
        	MySqlConnection objMySqlConnection = new MySqlConnection();
    		objMySqlConnection.open();
    		
        	try { 
	    	    String sqlGenres = """
	    	    		SELECT DISTINCT
						    genre_gf AS genre
						FROM 
						    db_filmcinema.genre_film
	    	    		""";
	    	    
	    	    ResultSet rsGenres = objMySqlConnection.executeSelect(sqlGenres);
    	    	
    	    	List<String> genres = new ArrayList<>();
    	    	
                if (rsGenres != null) {
                	while(rsGenres.next()) {
                		String genre = rsGenres.getString("genre");
                        genres.add(genre);
                	}
                	model.addAttribute("genreList", genres);
                }
                
                String sqlCountries = """
	    	    		SELECT DISTINCT
						    country_cf AS country
						FROM 
						    db_filmcinema.country_film
	    	    		""";
	    	    
	    	    ResultSet rsCountries = objMySqlConnection.executeSelect(sqlCountries);
    	    	
    	    	List<String> countries = new ArrayList<>();
    	    	
                if (rsCountries != null) {
                	while(rsCountries.next()) {
                		String country = rsCountries.getString("country");
                        countries.add(country);
                	}
                	model.addAttribute("countryList", countries);
                } 
                
                String sqlProducers = """
	    	    		SELECT DISTINCT
						    producer_pf AS producer
						FROM 
						    db_filmcinema.producer_film
	    	    		""";
	    	    
	    	    ResultSet rsProducers = objMySqlConnection.executeSelect(sqlProducers);
    	    	
    	    	List<String> producers = new ArrayList<>();
    	    	
                if (rsProducers != null) {
                	while(rsProducers.next()) {
                		String producer = rsProducers.getString("producer");
                        producers.add(producer);
                	}
                	model.addAttribute("producerList", producers);
                }    
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                objMySqlConnection.close();
            }
        }
        
		return "home";
	}
	
	@GetMapping("/login")
	public String login(HttpServletRequest request, Model model) {
		
		Cookie[] cookies = request.getCookies();
		
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if("lastAccess".equals(cookie.getName())) {
					try {
	                    String valorDecodificado = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8.toString());
	                    model.addAttribute("lastAccess", valorDecodificado);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
					break;
				}
			}
		}
		
		return "login";
	}
}
