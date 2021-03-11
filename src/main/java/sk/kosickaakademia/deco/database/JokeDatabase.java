package sk.kosickaakademia.deco.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JokeDatabase {
    private String url;
    private String user;
    private String password;

    public JokeDatabase(){
        loadConfig("src/main/resources/configLocal.properties");
    }

    private void loadConfig(String filepath){
        try {
            InputStream inputStream = new FileInputStream(filepath);

            Properties properties=new Properties();

            properties.load(inputStream);

            url=properties.getProperty("url");
            user=properties.getProperty("user");
            password=properties.getProperty("password");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url,user,password);
    }

    private List<String> executeSelect(PreparedStatement ps) {
        List<String> list = new ArrayList<>();
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                String text = rs.getString("text");
                list.add(text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public String getJoke(int id){
        if (id < 1) return "";

        try {
            Connection connection = getConnection();
            String query = "Select text from joke where id = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1,id);

            List<String> jokeList = executeSelect(ps);
            if (!jokeList.isEmpty()){
                return jokeList.get(0);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean insertJoke(String joke){
        if (joke == null || joke.equals(""))
            return false;
        try {
            Connection connection = getConnection();
            String query = "insert into joke(text) values(?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, joke);

            int result = ps.executeUpdate();
            return result != 0;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //returns 3 random jokes
    /*public List<String> getJokes(){
        try {
            Connection connection = getConnection();
            String query = "select text from joke where id=? or id=? or id=?";


            PreparedStatement ps = connection.prepareStatement(query);
            // todo unfinished

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return null;
    }*/

    public String getRandomJoke(){
        try {
            Connection connection = getConnection();
            String query = "select text from joke order by rand() limit 1";
            PreparedStatement ps = connection.prepareStatement(query);

            List<String> jokeList = executeSelect(ps);
            return jokeList.get(0);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
