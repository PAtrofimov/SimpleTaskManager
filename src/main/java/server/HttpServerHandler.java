package server;

import beans.User;
import handlers.DeleteTaskHandler;
import handlers.DeleteUserHandler;
import handlers.HandlerInterface;
import handlers.InsertTaskHandler;
import handlers.InsertUserHandler;
import handlers.LoginHandler;
import handlers.SelectAllUsersHandler;
import handlers.SelectTasksHandler;
import handlers.SelectUserHandler;
import handlers.UpdateTaskHandler;
import handlers.UpdateUserHandler;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpChunkedInput;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;
    public static final String catalog_path = "src\\main\\webapp";
    private User user;
    

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void sendJSonResponse(ChannelHandlerContext ctx, String jsontext, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
        response.headers().set(CONTENT_TYPE, "text/json; charset=UTF-8");
        StringBuilder buf = new StringBuilder().append(jsontext);
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    public void sendUserResponseBad(ChannelHandlerContext ctx, String mes) {
        //not found user
        String jsontext = "{\"success\":\"false\", \"message\":\"" + mes + "\"}";
        sendJSonResponse(ctx, jsontext, FORBIDDEN);
    }

    public void sendUserResponseOk(ChannelHandlerContext ctx) {
        String jsontext = "{\"success\":\"true\"}";
        sendJSonResponse(ctx, jsontext, OK);
    }
    
    public void sendUserIsAdminResponseOk(ChannelHandlerContext ctx, boolean isAdmin) {
        
        String jsontext = "{\"success\":\"true\", \"admin\":\"" + String.valueOf(isAdmin) + "\"}";
        sendJSonResponse(ctx, jsontext, OK);
    }

    public void sendOK(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    
    

    private boolean executeRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        Map<String, HandlerInterface> handlers = HttpServer.getMapServerHandlers();
        QueryStringDecoder qsd = new QueryStringDecoder(request.getUri());
        System.out.println("===" + qsd.parameters() + "==="+ request.getUri() + "===");
        
        if (qsd.parameters().containsKey("event")) {
            String eventstr = qsd.parameters().get("event").get(0);
            if (handlers.containsKey(eventstr)) {
                handlers.get(eventstr).handlerRequest(ctx, request, this);
                return true;
            }
        }
        return false;
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        final String uri = request.getUri();

        if (!request.getDecoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

      
        if (executeRequest(ctx, request)) {
          return;
        }

        final String path = sanitizeUri(uri);
        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        System.out.println(path);

        File file = new File(path);

        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }

//        if (file.isDirectory()) {
//            if (uri.endsWith("/")) {
//                sendListing(ctx, file);
//            } else {
//                sendRedirect(ctx, uri + '/');
//            }
//            return;
//        }
        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        // System.out.println("____"+request.toString());
        // Cache Validation
//        String ifModifiedSince = request.headers().get(IF_MODIFIED_SINCE);
//        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
//            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
//            Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);
//
//            // Only compare up to the second because the datetime format we send to the client
//            // does not have milliseconds
//            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
//            long fileLastModifiedSeconds = file.lastModified() / 1000;
//            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
//                sendNotModified(ctx);
//                return;
//            }
//        }
        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException ignore) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpHeaders.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateAndCacheHeaders(response, file);
        if (HttpHeaders.isKeepAlive(request)) {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        }

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture sendFileFuture;
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            sendFileFuture
                    = ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
            sendFileFuture
                    = ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                            ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }

        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) { // total unknown
                    System.err.println(future.channel() + " Transfer progress: " + progress);
                } else {
                    System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) {
                System.err.println(future.channel() + " Transfer complete.");
            }
        });

        // Decide whether to close the connection or not.
        if (!HttpHeaders.isKeepAlive(request)) {
            // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause
    ) {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");

    private static String sanitizeUri(String uri) {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/') {
            return null;
        }

        // Convert file separators.
        uri = uri.replace('/', File.separatorChar);

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + '.')
                || uri.contains('.' + File.separator)
                || uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.'
                || INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        // Convert to absolute path.
        return SystemPropertyUtil.get("user.dir") + File.separator +catalog_path + uri;
    }

    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");

    private static void sendListing(ChannelHandlerContext ctx, File dir) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

        String dirPath = dir.getPath();
        StringBuilder buf = new StringBuilder()
                .append("<!DOCTYPE html>\r\n")
                .append("<html><head><title>")
                .append("Listing of: ")
                .append(dirPath)
                .append("</title></head><body>\r\n")
                .append("<h3>Listing of: ")
                .append(dirPath)
                .append("</h3>\r\n")
                .append("<ul>")
                .append("<li><a href=\"../\">..</a></li>\r\n");

        for (File f : dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }

            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }

            buf.append("<li><a href=\"")
                    .append(name)
                    .append("\">")
                    .append(name)
                    .append("</a></li>\r\n");
        }

        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(LOCATION, newUri);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(
                HTTP_1_1, status, Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * When file timestamp is the same as what the browser is sending up, send a
     * "304 Not Modified"
     *
     * @param ctx Context
     */
    private static void sendNotModified(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
        setDateHeader(response);

        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * Sets the Date header for the HTTP response
     *
     * @param response HTTP response
     */
    private static void setDateHeader(FullHttpResponse response) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        Calendar time = new GregorianCalendar();
        response.headers().set(DATE, dateFormatter.format(time.getTime()));
    }

    /**
     * Sets the Date and Cache headers for the HTTP Response
     *
     * @param response HTTP response
     * @param fileToCache file to extract content type
     */
    private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        // Date header
        Calendar time = new GregorianCalendar();
        response.headers().set(DATE, dateFormatter.format(time.getTime()));

        // Add cache headers
        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
        response.headers().set(EXPIRES, dateFormatter.format(time.getTime()));
        response.headers().set(CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.headers().set(
                LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
    }

    /**
     * Sets the content type header for the HTTP Response
     *
     * @param response HTTP response
     * @param file file to extract content type
     */
    private static void setContentTypeHeader(HttpResponse response, File file) {
//        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
//        response.headers().set(CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
        response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
    }
}
