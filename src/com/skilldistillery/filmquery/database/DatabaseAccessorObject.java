package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;
import com.skilldistillery.filmquery.entities.Language;

public class DatabaseAccessorObject implements DatabaseAccessor {
//DAO accesses database, in actor example, use the findactorbyid method as example
	// this contains connectors, etc.

	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private static String user = "student";
	private static String pwd = "student";
	private static Connection conn;
//	static {
//		try {
//			conn = DriverManager.getConnection(URL, user, pwd);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Film findFilmById(int filmId) {
		Film film = null;
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, filmId);
			ResultSet filmResult = pstmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
				film.setId(filmResult.getInt("id"));
				film.setTitle(filmResult.getString("title"));
				film.setDescription(filmResult.getString("description"));
				film.setReleaseYear(filmResult.getInt("release_year"));
				film.setLanguageId(filmResult.getInt("language_id"));
				film.setRentalDuration(filmResult.getInt("rental_duration"));
				film.setRentalRate(filmResult.getInt("rental_rate"));
				film.setLength(filmResult.getInt("length"));
				film.setReplacementCost(filmResult.getDouble("replacement_cost"));
				film.setRating(filmResult.getString("rating"));
				film.setSpecialFeatures(filmResult.getString("special_features"));
				film.setCast(findActorsByFilmId(filmId));
				film.setLanguage(getLanguageByFilmID(filmId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement pstmt =null;
		ResultSet actorResult = null;
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, actorId);
			actorResult = pstmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
				actor.setActorId(actorResult.getInt("id"));
				actor.setFirstName(actorResult.getString("first_name"));
				actor.setLastName(actorResult.getString("last_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			} if (pstmt != null) {
				pstmt.close();
			} if (actorResult !=null) {
				actorResult.close();
			}
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) throws SQLException {
		List<Actor> actorList = new ArrayList<>();
		String sql = "SELECT actor.id, actor.first_name, actor.last_name FROM actor JOIN film_actor on actor.id = film_actor.actor_id JOIN film on film.id = film_actor.film_id WHERE film.id = ?";
		PreparedStatement pstmt = null;
		ResultSet actorByFilmResult = null;
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, filmId);
			actorByFilmResult = pstmt.executeQuery();
			while (actorByFilmResult.next()) {
				int actorId = actorByFilmResult.getInt("actor.id");
				String firstName = actorByFilmResult.getString("actor.first_name");
				String lastName = actorByFilmResult.getString("actor.last_name");
				Actor actor = new Actor(actorId, firstName, lastName);
				//System.out.println(actor);
				actorList.add(actor);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			} if (pstmt != null) {
				pstmt.close();
			} if (actorByFilmResult !=null) {
				actorByFilmResult.close();
			}
		}
		return actorList;
	}
	
	@Override
	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		List<Film> films = new ArrayList<>();
		Film film;
		String sql = "SELECT id, title, description, release_year, language_id, rental_duration, rental_rate, length, replacement_cost, rating, special_features FROM film WHERE title LIKE ? OR description LIKE ?";
		PreparedStatement pstmt = null;
		ResultSet filmByKeywordResult = null;
		
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%"+ keyword + "%");
			pstmt.setString(2, "%" + keyword + "%");
			filmByKeywordResult = pstmt.executeQuery();
			while (filmByKeywordResult.next()) {
				film = new Film();
				film.setId(filmByKeywordResult.getInt("id"));
				film.setTitle(filmByKeywordResult.getString("title"));
				film.setDescription(filmByKeywordResult.getString("description"));
				film.setReleaseYear(filmByKeywordResult.getInt("release_year"));
				film.setLanguageId(filmByKeywordResult.getInt("language_id"));
				film.setRentalDuration(filmByKeywordResult.getInt("rental_duration"));
				film.setRentalRate(filmByKeywordResult.getInt("rental_rate"));
				film.setLength(filmByKeywordResult.getInt("length"));
				film.setReplacementCost(filmByKeywordResult.getDouble("replacement_cost"));
				film.setRating(filmByKeywordResult.getString("rating"));
				film.setSpecialFeatures(filmByKeywordResult.getString("special_features"));
				film.setCast(findActorsByFilmId(film.getId()));
				film.setLanguage(getLanguageByFilmID(film.getId()));

				films.add(film);
				
			}
		} catch (SQLException e1) {
//			e1.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			} if (pstmt != null) {
				pstmt.close();
			} if (filmByKeywordResult !=null) {
				filmByKeywordResult.close();
			}
		}
		return films;
		
	}

	@Override
	public Language getLanguageByFilmID(int filmId) throws SQLException {
		Language lang = null;
		String sql = "SELECT language.id, language.name FROM film JOIN language on film.language_id = language.id WHERE film.id = ?";
		PreparedStatement pstmt = null;
		ResultSet langResult = null;
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, filmId);
			langResult = pstmt.executeQuery();
			if (langResult.next()) {
				lang = new Language();
				lang.setId(langResult.getInt("language.id"));
				lang.setName(langResult.getString("language.name"));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			if (conn != null) {
				conn.close();
			} if (pstmt != null) {
				pstmt.close();
			} if (langResult !=null) {
				langResult.close();
			}
		}
		return lang;
	}
	
	@Override
	public List<Film> findFilmsByActorId(int actorId) throws SQLException{
		  List<Film> films = new ArrayList<>();
		  PreparedStatement pstmt = null;
		  ResultSet filmsByActorResult = null;
		  try {
		    Connection conn = DriverManager.getConnection(URL, user, pwd);
		    String sql = "SELECT id, title, description, release_year, language_id, rental_duration, ";
		                sql += " rental_rate, length, replacement_cost, rating, special_features "
		               +  " FROM film JOIN film_actor ON film.id = film_actor.film_id "
		               + " WHERE actor_id = ?";
		    pstmt = conn.prepareStatement(sql);
		    pstmt.setInt(1, actorId);
		    filmsByActorResult = pstmt.executeQuery();
		    while (filmsByActorResult.next()) {
		      Film film = new Film();
		      film.setId(filmsByActorResult.getInt("id"));
				film.setTitle(filmsByActorResult.getString("title"));
				film.setDescription(filmsByActorResult.getString("description"));
				film.setReleaseYear(filmsByActorResult.getInt("release_year"));
				film.setLanguageId(filmsByActorResult.getInt("language_id"));
				film.setRentalDuration(filmsByActorResult.getInt("rental_duration"));
				film.setRentalRate(filmsByActorResult.getInt("rental_rate"));
				film.setLength(filmsByActorResult.getInt("length"));
				film.setReplacementCost(filmsByActorResult.getDouble("replacement_cost"));
				film.setRating(filmsByActorResult.getString("rating"));
				film.setSpecialFeatures(filmsByActorResult.getString("special_features"));
				film.setCast(findActorsByFilmId(film.getId()));
				film.setLanguage(getLanguageByFilmID(film.getId()));
				
		      films.add(film);
		    }
		    filmsByActorResult.close();
		    pstmt.close();
		    conn.close();
		  } catch (SQLException e) {
		    e.printStackTrace();
		  } finally {
				if (conn != null) {
					conn.close();
				} if (pstmt != null) {
					pstmt.close();
				} if (filmsByActorResult !=null) {
					filmsByActorResult.close();
				}
			}
		  return films;
		}
	
	

}
