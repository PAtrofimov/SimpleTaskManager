/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import com.owlike.genson.annotation.JsonProperty;

/**
 *
 * @author trofimov
 */
public class UserValidation {
    
    private boolean success;
    private String message;
    private boolean admin;
    
    @JsonProperty("admin")
    public boolean isadmin() {
        return admin;
    }
    
    public void setadmin(boolean isAdmin) {
        this.admin = isAdmin;
    }
    
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @JsonProperty("success")
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "{\"success\":\"" + success + "\", \"message\":\"" + message + "\", \"admin\":\"" + admin + "\"}";
    }

}
