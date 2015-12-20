/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import beans.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import controllers.TaskController;
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
public class SelectTasksHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        boolean authOK = new AuthenticationHandler().authenticate(context, request, handler);
        if (!authOK) {
            return;
        }
        TaskController tasksController = new TaskController();
        List<Task> tasks = tasksController.GetTasksByUser(handler.getUser().getId(),handler.getUser().isAdmin());
        try {

            ObjectMapper mapper = new ObjectMapper();
            handler.sendJSonResponse(context, mapper.writeValueAsString(tasks), OK);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(SelectTasksHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
