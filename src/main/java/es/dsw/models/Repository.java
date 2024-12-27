package es.dsw.models;

public class Repository {
	
	private String title;
	private String sinopsis;
	private String genre; 
	private String director; 
	private String reparto; 
	private String year; 
	private String datePremiere; 
	private String producer; 
	private String country;
	
	public Repository(String title, String sinopsis, String genre, String director, String reparto, String year, String datePremiere,
			String producer, String country) {
		this.title = title;
		this.sinopsis = sinopsis;
		this.genre = genre;
		this.director = director;
		this.reparto = reparto;
		this.year = year;
		this.datePremiere = datePremiere;
		this.producer = producer;
		this.country = country;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSinopsis() {
		return sinopsis;
	}

	public void setSinopsis(String sinopsis) {
		this.sinopsis = sinopsis;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getReparto() {
		return reparto;
	}

	public void setReparto(String reparto) {
		this.reparto = reparto;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getDatePremiere() {
		return datePremiere;
	}

	public void setDatePremiere(String datePremiere) {
		this.datePremiere = datePremiere;
	}

	public String getProducer() {
		return producer;
	}

	public void setProducer(String producer) {
		this.producer = producer;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
}
