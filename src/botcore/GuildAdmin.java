/* Copyright 2018 Jonas Wischnewski

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package botcore;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import botcore.exceptions.DatabaseConnectionException;
import botcore.languages.Languages;

import java.sql.*;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.impl.GuildImpl;

public class GuildAdmin {
    
    private ArrayList<GuildConfig> loaded;
    private Connection databaseConn;

    public GuildAdmin() {
	try {
	    connect();
	    loaded = new ArrayList<GuildConfig>();
	//    saveConfig(new GuildConfig());
	} 
	catch (DatabaseConnectionException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} 
//	catch (SQLException e) {
//	    // TODO Auto-generated catch block
//	    e.printStackTrace();
//	}
    }
    // TODO Speicherstrategie definieren
    public void setPrefix(Guild g) {
	
    }
    public String getPrefix(Guild g) {
	// TODO Methode um das jeweilige gespeicherte Prefix auszulesen
	return "+";
    }
    public boolean isLoaded(Guild g) {
	for (GuildConfig current : loaded) {
	    if (current.getId() == g.getId()) {
		return true;
	    }
	}
	return false;
    }
    public GuildConfig getIfLoaded(Guild g) {
	for (int i = 0; i < loaded.size(); i += 1) {
	    if (loaded.get(i).getId().equals(g.getId())) {
		return loaded.get(i);
	    }
	}
	return null;
    }
    public void load(Guild g) throws SQLException {
	Statement stmt = databaseConn.createStatement();
	
    }
    public void loadAndGet(Guild g) throws SQLException {
	Statement stmt = databaseConn.createStatement();
    }
    public boolean isLogActive(Guild g) {
	// TODO Methode, die zur�ckgibt ob geloggt werden soll
	return true;
    }
    public TextChannel getLogChannel(Guild g) {
	// TODO Methode die den Channel zum loggen zur�ckgibt initalisieren
	return g.getTextChannelCache().asList().get(0);
    }
    public Languages getLanguage(Guild g) throws SQLException {
	if (isLoaded(g)) {
	    GuildConfig con = getIfLoaded(g);
	    return con.getLanguage();
	}     
	return Languages.GERMAN;
    }
    /* 	public void saveConfig(GuildConfig con) throws SQLException {
	ResultSet rs = databaseConn.createStatement().executeQuery("SELECT ID FROM General WHERE EXISTS (SELECT ID FROM General WHERE ID = '"+ con.getId() + "')");
	if (rs.first()) {
	    System.out.println(rs.getString(1));
	} else {
	    System.out.println("Keine Reihe");
	}
    }*/
    public void connect() throws DatabaseConnectionException {
	try {
	    Class.forName("org.hsqldb.jdbcDriver");
	    String url;
	    Path startPath = Paths.get(".", "start.config");
	    BufferedReader input = Files.newBufferedReader(startPath);
	    for (byte b = 0; b < 4; b += 1) {
		input.readLine();
	    }
	    url = input.readLine();
	    databaseConn = DriverManager.getConnection(url, "TBot", "");
	    if (!databaseConn.isValid(5)) {
		throw new DatabaseConnectionException();
	    }
	} 
	catch (ClassNotFoundException classEx) {
	    DatabaseConnectionException dce = new DatabaseConnectionException();
	    dce.initCause(classEx);
	    throw dce;
	} 
	catch (IOException ioEx) {
	    DatabaseConnectionException dce = new DatabaseConnectionException();
	    dce.initCause(ioEx);
	    throw dce;
	}
	catch (SQLException sqlEx) {
	    DatabaseConnectionException dce = new DatabaseConnectionException();
	    dce.initCause(sqlEx);
	    throw dce;
	}
    }
}
