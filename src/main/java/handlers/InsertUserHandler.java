/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import beans.User;
import beans.UserValidation;
import com.google.gson.Gson;
import controllers.UserController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;


/**
 *
 * @author Anton
 */
public class InsertUserHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        
        ByteBuf content = request.content();
        if (content.isReadable()) {
            System.out.println("content " + content.toString(CharsetUtil.UTF_8));

            User newuser = new Gson().fromJson(content.toString(CharsetUtil.UTF_8), User.class);
            System.out.println(newuser.toString());
            UserValidation message = new UserController().createUser(newuser);

            if (message.isSuccess()) {
             //   handler.sendUserResponseOk(context);
             handler.sendJSonResponse(context, message.toString(), HttpResponseStatus.OK);
            } else {
              //  handler.sendUserResponseBad(context, mes );
              handler.sendJSonResponse(context, message.toString(), HttpResponseStatus.FORBIDDEN);
            }
            
        }

    }

}
