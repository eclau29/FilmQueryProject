package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException {
		FilmQueryApp app = new FilmQueryApp();
		System.out.println("Welcome to FilmQuery(TM)!");
//    try {
//		app.test();
//	} catch (SQLException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	} // run this first to make sure you can connect to DB
		try {
			app.launch();
		} catch (Exception e) {
			System.out.println("Invalid entry, please try again.\n");
			app.launch();
//			e.printStackTrace();
		}
	}

	private void test() throws SQLException {
		Film film = db.findFilmById(1);
		Actor actor = db.findActorById(1);
		List<Actor> cast = db.findActorsByFilmId(1);
		System.out.println(film);
		System.out.println(actor);
		System.out.println(cast);
	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		System.out.println("Would you like to:");
		System.out.println(
				"1. Look up a film using a Film ID\n2. Look up a film using a keyword\n3. Exit\n(Please choose 1, 2, or 3)");
		int resp = input.nextInt();
		String contResp;
		boolean contLoop = true;
		Film film;
		switch (resp) {
		case 1:
			System.out.print("Please enter a film ID: ");
			int filmIdInput;
			try {
				filmIdInput = input.nextInt();
				film = db.findFilmById(filmIdInput);
				if (film != null) {
					System.out.println(film + "\n");
				} 
			} catch (Exception e) {
//				e.printStackTrace();
				input.nextLine();
				System.out.println("\nI'm sorry, invalid film ID number. Please try again.\n");
			} finally {
				startUserInterface(input);
			}
			break;

		case 2:
			System.out.print("Please enter a KEYWORD: ");
			String keywordInput = input.next();
			List<Film> filmList = db.findFilmByKeyword(keywordInput);

			if (filmList.size() > 0) {
				for (Film film2 : filmList) {
					System.out.println(film2);
				}
			} else {
				System.out.println("I'm sorry, there are no results.\n");

			}
			startUserInterface(input);
			break;
		case 3:
			System.out.println("Exiting...\nThank you for using FilmQuery(TM)");
			contLoop = false;
			break;
		default:
			System.out.println("Invalid entry. Please try again\n");
			startUserInterface(input);
		}
	
	}

}
