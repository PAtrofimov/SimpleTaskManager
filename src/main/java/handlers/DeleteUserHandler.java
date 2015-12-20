/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import controllers.UserController;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

/**
 *
 * @author Anton
 */
public class DeleteUserHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        boolean authOK = new AuthenticationHandler().authenticate(context, request, handler);
        if(!authOK){
            return;
        }
        QueryStringDecoder qsd = new QueryStringDecoder(request.getUri());
        
        String id = qsd.parameters().get("id").get(0);
        Integer uid = Integer.parseInt(id);
        new UserController().deleteUser(uid);
        handler.sendOK(context);
    }

}
