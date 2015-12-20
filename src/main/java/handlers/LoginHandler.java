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
public class LoginHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        
        
//        QueryStringDecoder qsd = new QueryStringDecoder(request.getUri());
       
//        String login = qsd.parameters().get("login").get(0);
//        String password = qsd.parameters().get("password").get(0);
        
        ByteBuf content = request.content();
        if (content.isReadable()) {
            System.out.println("dsd" + content.toString(CharsetUtil.UTF_8));
            User CurUser = new Gson().fromJson(content.toString(CharsetUtil.UTF_8), User.class);
            System.out.println(CurUser.toString());
            User user = new UserController().getUserByLoginPassword(CurUser.getLogin(), CurUser.getPassword());
            handler.setUser(user);
        }

        if (handler.getUser() == null) {
            handler.sendUserResponseBad(context,"not found user");
        } else {
            handler.sendUserIsAdminResponseOk(context,handler.getUser().isAdmin());
        }
    }

}
