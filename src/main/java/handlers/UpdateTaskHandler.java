/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package handlers;

import utils.JsonDeserializerImpl;
import beans.Task;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import controllers.TaskController;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import server.HttpServerHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import io.netty.util.CharsetUtil;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anton
 */
public class UpdateTaskHandler implements HandlerInterface {

    public void handlerRequest(ChannelHandlerContext context, FullHttpRequest request, HttpServerHandler handler) {
        boolean authOK = new AuthenticationHandler().authenticate(context, request, handler);
        if (!authOK) {
            return;
        }

        ByteBuf content = request.content();
        if (content.isReadable()) {
            System.out.println("dsd" + content.toString(CharsetUtil.UTF_8));

            JsonDeserializer<Task> deser = new JsonDeserializerImpl();

            Gson gson = new GsonBuilder()
                    //        .registerTypeAdapter(Date.class, deser2).create();
                    .registerTypeAdapter(Task.class, deser).create();

            Task upTask = gson.fromJson(content.toString(CharsetUtil.UTF_8), Task.class);

            upTask.setUserId(handler.getUser().getId());
            System.out.println(upTask.toString());
            new TaskController().updateTask(upTask);

            try {

                ObjectMapper mapper = new ObjectMapper();
                handler.sendJSonResponse(context, mapper.writeValueAsString(upTask), OK);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(SelectTasksHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//        QueryStringDecoder qsd = new QueryStringDecoder(request.getUri());
//        TasksController tasksController = new TasksController();
//        Integer userId = Integer.parseInt(qsd.parameters().get("id").get(0));
//        String name = qsd.parameters().get("name").get(0);
//        boolean done = "true".equals(qsd.parameters().get("done").get(0));
        //  tasksController.updateTask(userId, name, done);
        handler.sendOK(context);
    }

}
