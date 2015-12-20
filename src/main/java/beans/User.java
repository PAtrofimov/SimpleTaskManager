package beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.owlike.genson.annotation.JsonProperty;

public class User {
    private int id;
    private String login;
    private String password;
    private String firstname;
    private String lastname;
    private boolean admin;
    
    @JsonIgnore
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    
    @JsonProperty("firstname")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }
    @JsonProperty("lastname")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }
    
        
    @JsonProperty("id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @JsonProperty("login") 
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", login=" + login 
        + ", password=" + password + ", firstname=" + firstname
        + ", lastname=" + lastname+ '}';
    }
    
    
     
}
