# YouTube-Download

A Java desktop application for downloading YouTube videos with customizable options.

## Features

- Download videos from YouTube URLs
- Support for video quality selection (720p)
- Optional Arabic subtitle download
- Playlist support
- Video thumbnail preview
- Custom save location
- Table view of download queue
- Tree view navigation

## Prerequisites

- Java 8 or higher
- yt-dlp installed on system
- JavaFX

## Installation

1. Clone the repository
2. Install yt-dlp on your system
3. Build using Maven
4. Run the application

## Usage

1. Click "Add URL" button to add YouTube video link
2. Select desired options (e.g., subtitles)
3. Choose save location in settings
4. Click "Start" to begin download
5. Monitor progress in the table view

## Technical Details

- Built with JavaFX
- Uses yt-dlp as backend downloader
- Lombok for reduced boilerplate
- JSON for configuration storage
- Log4j2 for logging
- Supports custom save paths
- Cross-platform compatibility

