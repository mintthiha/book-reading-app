# Book Reading App

## Developers :
- Thiha Min Thein
- Wan Mei Tao
- Ivana Zhekova

## Project Overview 
This Android application is a book reading app that downloads and displays 
HTML books from Project Gutenberg. The app handles downloading, parsing, 
and displaying books with text, images, and tables while providing a smooth reading experience.

## Prerequisits 
- Android Studio Hedgehog | 2023.1.1
- Java 17 Installed 
- Kotlin 1.8.0+
- Git

## Installation

Clone the repository:

```bash
git clone https://gitlab.com/dawson-csy3-24-25/511/section1/Mei-Ivana-Thiha/book-reading-app

```

Open Android Studio and select:

CopyFile -> Open -> [Navigate to cloned project directory]

Sync Gradle files:

CopyTools -> Android -> Sync Project with Gradle Files

## Running the App

Connect an Android device or start an emulator

Build and run the app:

Run -> Run 'app'

## Project Structure 


## Key Features

-  Downloads and parses HTML books from URLs
-  Stores book content in Room database
-  Displays books with images and tables
-  Search functionality within books
-  Adaptive navigation based on screen size
-  Multi-language support (English/French)


## Design Choices Documentation

### UI Layer
The app uses Jetpack Compose with 5 main screens:

1. **Home Screen (Home.kt)**
   - Welcome message
   - Navigation to library
   - Motivational quote
   - Uses `Column` layout for vertical content flow

2. **Library Screen (Library.kt)**
   - Displays downloaded books in `LazyRow`
   - Shows available books for download
   - Handles book downloading process
   - Uses async image loading

3. **Reading Screen (Reading.kt)**
   - Displays book content
   - Chapter navigation
   - Reading mode toggle
   - Uses `Box` layout with nested `Column` for content

4. **Search Screen (Search.kt)**
   - Search within current book
   - Results highlighting
   - Navigation to found content
   - Uses `Column` layout with scroll

5. **Table of Contents Screen (TableOfContent.kt)**
   - Lists chapters
   - Navigation to chapters
   - Reading mode toggle
   - Uses `LazyColumn` for chapter list


#### ViewModel Design

The way we designed our database is in a way that we stored each element of a chapter into its own categoric table such as a heading, a paragraph, a table, and an image.
While fetching all those elements to display a chapter, using one view model to handle this process is way easier than using one view model per entity. 
By having one view model, we can fetch the elements of a chapter in a way that they are in one single list rather than four different lists. 
Plus, the single list is in order of how the elements should be displayed.

This same view model is also used to keep track of the current book and the current chapter the user clicked on. 
When the user clicks on a chapter, the view model can call the repository to update the current chapter live data based 
on the current book variable it is holding. Similarly, it will also call the repository to update the chapter’s list of elements live
data based on the current chapter variable it is holding.
Finally, by having all the logic in one view model, everything is done internally in the view model. 
This avoids having to pass around values from one view model to another in the UI.
