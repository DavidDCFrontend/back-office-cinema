package es.dsw.controllers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import es.dsw.connection.MySqlConnection;

@RestController
@SessionAttributes("username")
public class NewRepoController {

	@PostMapping("/guardar")
	public ResponseEntity<String> saveRepo(@RequestParam(name="title") String title,
			               @RequestParam(name="sinopsis") String sinopsis,
			               @RequestParam(name="genre") String genre,
			               @RequestParam(name="director") String director,
			               @RequestParam(name="reparto") String reparto,
			               @RequestParam(name="year") String year,
			               @RequestParam(name="datePremiere") String datePremiere,
			               @RequestParam(name="producer") String producer,
			               @RequestParam(name="country") String country,
			               Model model) {
		
		String username = (String) model.getAttribute("username");
		int iduser = 0;
		int idgenre = 0;
		int idcountry = 0;
		String idproducer = null;
		boolean success = false;
		
		
		MySqlConnection objMySqlConnection = new MySqlConnection();
		//Obtener iduser
	    objMySqlConnection.open();
	    
	    String sqlIdUser = """
	    		SELECT uf.iduser_usf AS iduser
				FROM db_filmcinema.user_film uf
				WHERE uf.username_usf = ?;
	    		"""; 
	    
	    try (ResultSet rs = objMySqlConnection.executeSelect(sqlIdUser, username)) {
            if (rs != null && rs.next()) {
                iduser = rs.getInt("iduser");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            objMySqlConnection.close();
        }
	    
	    
	    //Obtener idgenre
	    objMySqlConnection.open();
	    
	    String sqlIdGenre = """
	    		SELECT gf.idgenre_gf AS idgenre
	    		FROM db_filmcinema.genre_film gf
	    		WHERE gf.genre_gf = ?;
	    		"""; 
	    
	    try (ResultSet rs = objMySqlConnection.executeSelect(sqlIdGenre, genre)) {
            if (rs != null && rs.next()) {
                idgenre = rs.getInt("idgenre");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            objMySqlConnection.close();
        }
	    
	    
	  //Obtener idcountry
	    objMySqlConnection.open();
	    
	    String sqlIdCountry = """
	    		SELECT cf.idcountry_cf AS idcountry
	    		FROM db_filmcinema.country_film cf
	    		WHERE cf.country_cf = ?;
	    		"""; 
	    
	    try (ResultSet rs = objMySqlConnection.executeSelect(sqlIdCountry, country)) {
            if (rs != null && rs.next()) {
                idcountry = rs.getInt("idcountry");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            objMySqlConnection.close();
        }
	    
	  //Obtener idproducer
	    if(!"".equals(producer)) {
	    	objMySqlConnection.open();
		    
		    String sqlIdProducer = """
		    		SELECT pf.idproducer_pf AS idproducer
		    		FROM db_filmcinema.producer_film pf
		    		WHERE pf.producer_pf = ?;
		    		"""; 
		    
		    try (ResultSet rs = objMySqlConnection.executeSelect(sqlIdProducer, producer)) {
	            if (rs != null && rs.next()) {
	                idproducer = String.valueOf(rs.getInt("idproducer"));
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            objMySqlConnection.close();
	        }
	    }
	    
	    
	    // Creamos registro en 'repository_film' dentro de una transacción
        MySqlConnection objMySqlConnection2 = new MySqlConnection(false);
        objMySqlConnection2.open();

        try {
            // Iniciar transacción

            if (!objMySqlConnection2.isError() && idgenre != 0 && idcountry != 0 && iduser != 0) {
            	
            	if("".equals(datePremiere)) {
            		datePremiere = null;
            	}
            	
            	if("".equals(idproducer)) {
            		idproducer = null;
            	}
            	
                String insertNewRepo = """
    		    		INSERT INTO db_filmcinema.repository_film (title_rf, synopsis_rf, idgenre_rf, director_rf, reparto_rf, anio_rf, datepremiere_rf, idproducer_rf, idcountry_rf, s_iduser_cf)
    		    		VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
    		    		"""; 

                objMySqlConnection2.executeUpdateOrDelete(insertNewRepo, title, sinopsis, idgenre, director, reparto, year, datePremiere, idproducer, idcountry, iduser);
            } else {
                throw new SQLException("Error en la conexión: " + objMySqlConnection.msgError());
            }

            // Commit de la transacción si todo fue exitoso
            objMySqlConnection2.commit();
            success = true;
        } catch (SQLException e) {
            objMySqlConnection2.rollback();
            e.printStackTrace();
        } finally {
            objMySqlConnection2.close();
        }
        
        if (success) {
            return ResponseEntity.ok().body("{\"message\": \"Película guardada correctamente\"}");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("{\"message\": \"Error al guardar la película\"}");
        }
	}
}
