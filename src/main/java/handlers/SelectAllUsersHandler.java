/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import beans.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.UserController;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton
 */
public class SelectAllUsersHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        boolean authOK = new AuthenticationHandler().authenticate(context, request, handler);
        if (!authOK) {
            return;
        }
        
        List<User> users = new UserController().getAllUsers();
        try {

            ObjectMapper mapper = new ObjectMapper();
            handler.sendJSonResponse(context, mapper.writeValueAsString(users), OK);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SelectTasksHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}

