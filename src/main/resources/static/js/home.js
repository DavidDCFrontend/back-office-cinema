//Se sobrecarga el botón toggle
$(function() {
  // Sidebar toggle behavior
  $('#sidebarCollapse').on('click', function() {
    $('#sidebar, #content').toggleClass('active');
  });

});

//Se sobrecarga botón de Guardar de ventana modal de Nueva Película y 
//evento onclick de Listar Películas
$(document).ready(function(){
  
/////////////////////////////////////////////////////////////////////////////  
//Llamada asincrona al servidor para enviar por método POST los datos de la nueva película.  
  $('body').on('click', '#GuardarPelicula', function(){
    //En esta zona debería ubicarse la llamada asíncrona al servidor enviando los datos de la nueva película

	let data_title = $("#repoTitle").val();
	let data_sinopsis = $("#repoSinopsis").val();
	let data_genre = $("#repoGenre").val();
	let data_director = $("#repoDirector").val();
	let data_reparto = $("#repoReparto").val();
	let data_year = $("#repoYear").val();
	let data_datePremiere = $("#repoDatePremiere").val();
	let data_producer = $("#repoProducer").val();
	let data_country = $("#repoCountry").val();
	
	// Validaciones
	// Validación título
	if(!data_title.trim()) {
		alert("El título de la película es obligatorio");
		return;
	}
	
	// Validación año
	const currentYear = new Date().getFullYear();

	if(data_year == "") {
		alert("El año de la película es obligatorio");
		return;
	}
	
	if(data_year != "") {
		data_year = parseInt(data_year, 10);
		if(isNaN(data_year) || data_year < 1950) {
			alert("Introduce un año válido");
			return;
		}
	}
	
	if (data_year > currentYear) {
        alert("El año no puede ser mayor que el año actual");
        return;
    }
    
    // Validación pais
    if(data_country == "") {
		alert("El pais de la película es obligatorio");
        return;
	}
	
	  // Validación género
    if(data_genre == "") {
		alert("El género de la película es obligatorio");
        return;
	}
    
	let token = $("meta[name='_csrf']").attr("content");
	let header = $("meta[name='_csrf_header']").attr("content");

	$.ajax({
		url: '/guardar',
		dataType: 'json',
		method: 'POST',
		data: {title:data_title, sinopsis:data_sinopsis, genre:data_genre, director:data_director, reparto:data_reparto, year:data_year, datePremiere:data_datePremiere, producer:data_producer, country:data_country},
		beforeSend: request => request.setRequestHeader(header, token),
		success: function(response) {
			$("#NuevaPeliculaCenter").modal('hide'); //Se oculta la ventana modal

    		//$('body').removeClass('modal-open'); //Eliminamos la clase del body para poder hacer scroll
    		$('.modal-backdrop').remove();//eliminamos el backdrop del modal
    		
    		alert(response.message);
    		
          //Si se pudo guardar una nueva película entonces mostrar la lista de peliculas:
          setTimeout(function() {
			  $('#ListarPeliculasVisual').modal('show');
    	  }, 200);
     		//$('#ListarPeliculasVisual').modal('show');
		},
		error: function(status, error) {
			console.error("Error en la solicitud:", status, error);
		}
   })
  });


  ///////////////////////////////////////////////////////////////////////
  //Llamada asíncrona para obtener los datos de las películas y cargarlos dentro de la capa #htmlListaPeliculas
  
  $('body').on('click', '#IdListarPeliculas', function(){
	  loadRepoList();
  });
  
  $('#ListarPeliculasVisual').on('shown.bs.modal', function() {
    loadRepoList();  // Llamar a la misma función que ejecuta la AJAX cuando se guarda una nueva película
  });

  function loadRepoList() {
	  
		$.ajax({
		    url:'/repolist',
			method: 'GET',
			success: function(list) {
				$("#htmlListaPeliculas").html(list);},
			error: function() {
				$("#htmlListaPeliculas").html('<p>Ha ocurrido un error al cargar la lista</p>');}
		});
  } 
}) 
