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
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;


/**
 *
 * @author Anton
 */
public class UpdateUserHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        
        ByteBuf content = request.content();
        if (content.isReadable()) {
            System.out.println("dsd" + content.toString(CharsetUtil.UTF_8));

            User user = new Gson().fromJson(content.toString(CharsetUtil.UTF_8), User.class);
            System.out.println(user.toString());
            String mes = new UserController().updateUser(user);

            if ("".equals(mes)) {
               handler.sendUserResponseOk(context);
            } else {
                handler.sendUserResponseBad(context, mes );
            }

        }

    }

}

