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
		String sql = "SELECT title FROM film WHERE id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, filmId);
			ResultSet filmResult = pstmt.executeQuery();
			if (filmResult.next()) {
				film = new Film();
				film.setTitle(filmResult.getString("title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return film;
	}

	@Override
	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
//		try {
//			conn = DriverManager.getConnection(URL, user, pwd);
//		} catch (SQLException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		String sql = "SELECT first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement pstmt =null;
		ResultSet actorResult = null;

		try {
			conn = DriverManager.getConnection(URL, user, pwd);
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, actorId);
			actorResult = pstmt.executeQuery();
			if (actorResult.next()) {
				actor = new Actor();
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
	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actorList = new ArrayList<>();
		try {
			conn = DriverManager.getConnection(URL, user, pwd);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return null;
	}

}
