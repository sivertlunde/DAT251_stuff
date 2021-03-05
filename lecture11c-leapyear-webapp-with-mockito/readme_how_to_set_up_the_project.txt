How I did it (2017) !
It is possible that this issue has been fixed since then.
---------------------------------------------------------

1 - I created a Maven Project from within Eclipse using the maven-archetype-webapp. (The Eclipse
	files are generated from "old" settings in the maven-plugin, and will be replaced later.)

2 - To conform with the Servlet 3.0 spec., I replaced the content of the web.xml file with this:
	<?xml version="1.0" encoding="UTF-8"?>
    <web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd" version="3.0">
      <display-name>Archetype Created Web Application</display-name>
    </web-app>
    
3 - To use "new" versions of stuff, I updated pom.xml with the following properties and dependencies:

	<properties>
		<maven.compiler.source>1.8</maven.compiler.source>
		<maven.compiler.target>1.8</maven.compiler.target>
	</properties>
	
	<dependencies>
		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-api</artifactId>
			<version>5.3.1</version>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter-engine</artifactId>
			<version>5.3.1</version>
			<scope>test</scope>
		</dependency>
		...
		<dependency>
			<groupId>javax.servlet</groupId>
			<artifactId>javax.servlet-api</artifactId>
			<version>3.0.1</version>
			<scope>provided</scope>
		</dependency>
	</dependencies>
	...
	
4 - Now it is time to generate the Eclipse files one more time, based on the new settings.
	4a - I closed the project and deleted it (without deleting the files)
	4b - From Windows Explorer, I deleted the Eclipse files from the project
	4c - I imported the Maven-project in Eclipse once more

5 - From Navigator, I added folders src/main/java and src/test/java
	(they automatically become source folders)
	
8 - To verify the setup, I created a servlet and run the application on a server.

OK !!! :)
	