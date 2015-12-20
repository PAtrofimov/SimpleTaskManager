/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import beans.User;
import controllers.UserController;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Anton
 */
public class AuthenticationHandler {

    private String[] getAuthenticationData(String data) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodedByteArray = decoder.decode(data);
        String[] authdata = new String(decodedByteArray).split(":");

        return authdata;

    }

    public boolean authenticate(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        UserController userController = new UserController();
        Entry<String, String> find_entry = null;
        for (Map.Entry<String, String> entry : request.headers()) {
            if (entry.getKey().equals("Authorization")) {
                find_entry = entry;
                break;
            }
        }

        if (find_entry != null) {

            String encodedString = find_entry.getValue().replace("Basic ", "");

            String[] authdata = getAuthenticationData(encodedString);
            String login = authdata[0];
            String password = authdata[1];
            User user = userController.getUserByLoginPassword(login, password);
            if (user != null) {
                handler.setUser(user);
                return true;
            }
        }
        return false;
    }

}
