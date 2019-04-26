package com.example.socket.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SocketService {
    private int port = 8888;

    // 用于编/解码 buffer
    private Charset charset = Charset.forName("UTF-8");

    // 用于接收数据的缓冲区
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    // 用于发送数据的缓冲区
    private ByteBuffer sBuffer = ByteBuffer.allocate(1024);

    // 用于存放客户端集合
    private Map<String, SocketChannel> clientMap = new HashMap();

    private static int i = 0;

    // 用于监听通道事件
    private static Selector selector;

    public SocketService(int port) {
        this.port = port;
        try {
            init();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void init() throws IOException{
        /**
         * 获得一个ServerSocketChannel通道
         */
        ServerSocketChannel serverSocketChannels = ServerSocketChannel.open();
        /**
         * 设置通道为非阻塞模式
         */
        serverSocketChannels.configureBlocking(false);

        /**
         * 将通道对应的serviceSocket绑定到port上
         */
        serverSocketChannels.socket().bind(new InetSocketAddress(8888));

        /**
         * 获得一个通道管理器
         */
        selector = Selector.open();

        /**
         * 将通道管理器和该通道绑定，并未该通道注册selectionKey.OP_ACCEPT 时间，注册该事件后，
         * 当该事件到达时，selector.select()会返回如果该事件没达到selector.select()会一直阻塞。
         */
        serverSocketChannels.register(selector, SelectionKey.OP_ACCEPT);

        /**
         * 采用轮询的方式监听selector上是否有需要处理的事件，如何有，则进行处理
         */
        System.out.println("服务器启动成功......");
    }

    /**
     * 服务器端轮询监听，select 方法会一直阻塞直到有相关事件发生或超时
     */
    public void listen(){
        while (true){
            try {
                // 返回值为本次触发的事件数
                selector.select();
                // 获得selector中选中的迭代器，选中的项为注册的事件
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    // 删除已选的key，以防重复处理
                    iterator.remove();
                    handle(key);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(SelectionKey key){
        try {
            //有客户端要连接
            if (key.isAcceptable()){
                ServerSocketChannel server = (ServerSocketChannel)key.channel();
                // 获得和客户端连接的通道
                SocketChannel channel = server.accept();
                // 设置为非阻塞模式
                channel.configureBlocking(false);
                //在和客户端连接成功后，为了可以接收客户端的信息，需要给通道设置读的权限
                channel.register(selector,SelectionKey.OP_READ);
                clientMap.put(getClientName(channel),channel);
                //获取可读事件
            }else if (key.isReadable()){
                /**
                 * 处理客户端发送过来的消息事件
                 */
                // 服务器可读取消息，得到事件发生的Socket通道
                SocketChannel channel = (SocketChannel) key.channel();
                rBuffer.clear();
                int read = channel.read(rBuffer);
                if (read > 0){
                    rBuffer.flip();
                    String receiveText = String.valueOf(charset.decode(rBuffer));
                    System.out.println(channel.toString() + ":" + receiveText);
                    dispatch(channel, receiveText);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转发消息给各个客户端
     */
    private void dispatch(SocketChannel client, String info) throws IOException {
        if (!clientMap.isEmpty()) {
            for (Map.Entry<String, SocketChannel> entry : clientMap.entrySet()) {
                SocketChannel temp = entry.getValue();
                if (!client.equals(temp)) {
                    sBuffer.clear();
                    sBuffer.put(charset.encode(getClientName(client) + ":" + info));
                    sBuffer.flip();
                    temp.write(sBuffer);
                }
            }
        }
    }

    private String getClientName(SocketChannel client){
        Socket socket = client.socket();
        return "[" + socket.getInetAddress().toString().substring(1) + ":" + Integer.toHexString(client.hashCode())+"]";
    }

    public static void main(String[] args) throws IOException {
        SocketService service = new SocketService(8888);
        service.listen();
    }
}
