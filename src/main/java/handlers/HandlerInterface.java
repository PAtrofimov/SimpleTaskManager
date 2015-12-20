/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 *
 * @author Anton
 */
public interface HandlerInterface {
    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler);
}
