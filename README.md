#Synopsis
The application is a SWING based Java standalone desktop application which is used for the following functionalities
1. Upload files to user's drop box account.
2. Download files from user's drop box account.
3. Delete files from user's drop box account.
4. Sync folders in PC with the drop box account.

#Code Example
App.java 
Initiates the application, 
First time users are redirected to user's dropbox online account to provide Access Permission to the Application.
Performs OAuth handshake with drop box account using dropbox api

CreateGUI.java
Creates the gui frame and other components

com.balaji.app.listeners.*
Event listeners for button events of upload, download, delete and sync files and folders.

FileListener.java
Folder syncer class to listen for file change events.

#Motivation
Started this project to get my hands dirty on Java Swing and Drop Box API. Felt the need for synchronizing more than one folder in computer
In Existing drop box account only one folder can be synchronized with the drop box account. 

#Installation
Import the entire files into eclipse

#API Reference
Drop Box API
Apache FileAlteration Monitor

#Tests
Not completed yet

#Contributors
self, stackoverflow.com members

#License
Open source, feel free to download and enhance/provide feedback.
