/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import beans.User;
import com.google.gson.Gson;
import controllers.UserController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;


/**
 *
 * @author Anton
 */
public class InsertUserHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        
        ByteBuf content = request.content();
        if (content.isReadable()) {
            System.out.println("dsd" + content.toString(CharsetUtil.UTF_8));

            User newuser = new Gson().fromJson(content.toString(CharsetUtil.UTF_8), User.class);
            System.out.println(newuser.toString());
            String mes = new UserController().createUser(newuser);

            if ("".equals(mes)) {
                handler.sendUserResponseOk(context);
            } else {
                handler.sendUserResponseBad(context, mes );
            }

        }

    }

}
