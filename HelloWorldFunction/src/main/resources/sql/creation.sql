# Create database
CREATE DATABASE lyricsDB;

# Use database
USE lyricsDB;

# Create table for lyrics
CREATE TABLE Lyrics
(
    id      INT AUTO_INCREMENT PRIMARY KEY, # Primary key
    title   VARCHAR(255) NOT NULL,          # Title of song
    artist  VARCHAR(255) NOT NULL,          # Artist of song
    content TEXT         NOT NULL           # Lyrics of song
);
