# Smart Pantry Manager

## Project Overview
Smart Pantry Manager is an Android application developed in Java for the MAD700 assignment at Richfield.

The application allows users to manage pantry ingredients and discover recipes based on the ingredients they have available.

## Features
- Add, edit and delete pantry ingredients.
- Store ingredient quantities, units and expiry dates.
- Display expiry warnings.
- Suggest recipes based on available ingredients.
- View recipe ingredients and cooking instructions.
- Save data locally using SQLite.
- Enable or disable expiry warnings in Settings.

## Technologies Used
- Java
- Android Studio
- XML
- SQLite
- SharedPreferences

## How to Run
1. Download or clone the repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync.
4. Start an Android emulator or connect an Android device.
5. Run the application.

## Database
The application uses SQLite to store pantry ingredients and recipe information.

The database contains three main tables:
- `ingredients`
- `recipes`
- `recipe_ingredients`

The application includes 16 predefined recipes.

## Developer
Developed for MAD700 — Mobile Application Development.

## GitHub Repository
https://github.com/RiyashxSkyRonin/Smart_Pantry_Manager
