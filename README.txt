README for NET302 Assignment by Dan Jeffries, James Mellor, Sam Bewick and Stephanie Finnigan

This directory contains the following important folders:
"Java Library" - This contains all JAR files required by the different java applications. When loading into Netbeans from a fresh GIT pull, netbeans will ask for the location of these JAR files; note that the 2 different gson versions both use the same gson JAR 
	"NET302 - Java Class Test" - This contains a library built to test the NET302JavaLibrary" and isn't important or used in the system
	
	"NET302JavaLibrary" - This is the main java library used in the REST API and in the website. 
		"src\Encrypter\*" - This contains the encryption classes used in the Java components of the system
		
		"src\NET302JavaLibrary\*" - This contains all other data classes used in the Java components of the system 

"Mobile Application" - This contains the code for the mobile application 
	"DWSS\Data\*" - This contains C# 'copies' of the Java library to allow JSON -> GSON serialization and deserialization 
	
	"DWSS\Development\*" - This contains pre-release dummy data and can be ignored

	"DWSS\Encryption\*" - This contains all classes used in the encryption and decryption processes

	"DWSS\Middleware\*" - This contains static classes used when interacting with the REST API

	"DWSS\Pages\*" - Contains UI code (.xaml files) and C# code for the pages (.xaml.cs). Pages refer to whole pages used by the application such as Login page or the Options page. 
		Note: .xaml just contains xml-like structure whereas the .cs contain all logic for the UI

	"DWSS\UserControls\*" - Contains UI code and C# code for the user controls used within the application. User controls are used to define re-usable structure such as the Order UI object or the Product UI object 

	"DWSS\MasterPage.xaml & .xaml.cs" - This is the master page used to display child pages in the application, with the exception of the login screen 

	"DWSS\StaticData.cs" - This contains all 'Session'-like data within the mobile application

"NET302_ClientConnector" - This is the Java library used to interact with the REST API 
	"src\Connector\Connector.java" - This is the only class in the library and contains all REST API connection logic 


"NET302_REST" - This is the REST API 
	"src\java\NET302_Handlers" - This is the class used to connect to the database. Contains all database logic

	"web\WEB-INF" - This is an empty folder required to exist by Netbeans. If this isn't here then the build will fail with a non-related error message. This folder isn't created on a fresh GIT pull. 

	"web\*" - This contains the JSP pages of which the rest of the system POST/GET their data from. These JSP pages provide the REST functionality. All information passed to or from these pages is encrypted. 

"Net302JavaWebApp" - This is the website 
	"src\java\net302\ManagementBean.java" - This is used to interact with the REST API from the UI side of the web pages. Removes interaction logic from individual pages. Also provides session-state data

	"src\java\net302\SessionHandler.java" - This is used to contain a copy of ManagementBean.java per client session 

	"web\*" - Contains .xhtml pages with mainly UI-based information, these all use the ManagementBean class to interact with the session/server data 