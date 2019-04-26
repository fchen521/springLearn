package com.example.socket.service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ChatServer {

    private Selector  selector = null;
    private Charset charset = Charset.forName("UTF-8");
    public static final int PORT = 8888;
    private static String USER_CONTENT_SPILIT = "#";
    private static HashSet<String> users = new HashSet<String>();

    public void init() throws IOException {
        selector = Selector.open();
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(PORT));
        // 将其注册到 Selector 中，监听 OP_ACCEPT 事件
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            // 遍历
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                dealWithKey(server, key);

            }
        }

    }

    private void dealWithKey(ServerSocketChannel server, SelectionKey key) throws IOException {
        String content = null;
        if (key.isAcceptable()) {
            // 有已经接受的新的到服务端的连接
            SocketChannel socketChannel = server.accept();

            // 有新的连接并不代表这个通道就有数据，
            // 这里将这个新的 SocketChannel 注册到 Selector，监听 OP_READ 事件，等待数据
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_READ);
            //将此对应的channel设置为准备接受其他客户端请求
            key.interestOps(SelectionKey.OP_ACCEPT);
            System.out.println("Server is listening from client " + socketChannel.getRemoteAddress());
            socketChannel.write(charset.encode("Please input your name: "));

        } else if (key.isReadable()) {
            // 有数据可读
            // 上面一个 if 分支中注册了监听 OP_READ 事件的 SocketChannel
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int num = socketChannel.read(readBuffer);
            if (num > 0) {
                content = new String(readBuffer.array()).trim();
                // 处理进来的数据...
                System.out.println("Server is listening from client " +
                        socketChannel.getRemoteAddress() +
                        " data received is: " +
                        content);
/*                ByteBuffer buffer = ByteBuffer.wrap("返回给客户端的数据...".getBytes());
                socketChannel.write(buffer);*/
                //将此对应的channel设置为准备下一次接受数据
                key.interestOps(SelectionKey.OP_READ);

                String[] arrayContent = content.split(USER_CONTENT_SPILIT);
                //注册用户
                if(arrayContent != null && arrayContent.length ==1) {
                    String name = arrayContent[0];
                    if(users.contains(name)) {
                        socketChannel.write(charset.encode("system message: user exist, please change a name"));

                    } else {
                        users.add(name);
                        int number = OnlineNum(selector);
                        String message = "welcome " + name + " to chat room! Online numbers:" + number;
                        broadCast(selector, null, message);
                    }
                }
                //注册完了，发送消息
                else if(arrayContent != null && arrayContent.length >1){
                    String name = arrayContent[0];
                    String message = content.substring(name.length() + USER_CONTENT_SPILIT.length());
                    message = name + " say " + message;
                    if(users.contains(name)) {
                        //不回发给发送此内容的客户端
                        broadCast(selector, socketChannel, message);
                    }
                }
            } else if (num == -1) {
                // -1 代表连接已经关闭
                socketChannel.close();
            }
        }
    }

    private void broadCast(Selector selector, SocketChannel except, String content) throws IOException {
        //广播数据到所有的SocketChannel中
        for(SelectionKey key : selector.keys())
        {
            Channel targetchannel = key.channel();
            //如果except不为空，不回发给发送此内容的客户端
            if(targetchannel instanceof SocketChannel && targetchannel!=except)
            {
                SocketChannel dest = (SocketChannel)targetchannel;
                dest.write(charset.encode(content));
            }
        }
    }

    public static int OnlineNum(Selector selector) {
        int res = 0;
        for(SelectionKey key : selector.keys())
        {
            Channel targetchannel = key.channel();
            if(targetchannel instanceof SocketChannel)
                res++;
        }
        return res;
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().init();
    }

}
