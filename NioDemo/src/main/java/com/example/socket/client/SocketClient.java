package com.example.socket.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Scanner;

public class SocketClient {
    // 服务端地址
    private InetSocketAddress SERVER;

    // 用于接收数据的缓冲区
    private ByteBuffer rBuffer = ByteBuffer.allocate(1024);

    // 用于发送数据的缓冲区
    private ByteBuffer sBuffer = ByteBuffer.allocate(1024);

    // 用于监听通道事件
    private static Selector selector;

    // 用于编/解码 buffer
    private Charset charset = Charset.forName("UTF-8");

    public SocketClient(int port) {
        SERVER = new InetSocketAddress("localhost",port);
        try {
            init();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void init() throws IOException {
        // 获得一个Socket 通道
        SocketChannel channel = SocketChannel.open();

        //设置为非阻塞模式
        channel.configureBlocking(false);

        // 获得一个通道管理器
        selector = Selector.open();

        // 将该通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件
        channel.register(selector, SelectionKey.OP_CONNECT);

        //客户端连接服务器，其实方法执行并没有实现连接，需要在listen（）方法中调
        //用channel.finishConnect();才能完成连接
        channel.connect(SERVER);

        /**
         * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理
         */
        // 轮询访问selector
        while (true){
            selector.select();
            // 获得selector中选中的项的迭代器
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                // 删除已选的key,以防重复处理
                iterator.remove();
                handle(key);
            }
        }
    }

    private void handle(SelectionKey key){
        try {
            if (key.isConnectable()){
                SocketChannel client = (SocketChannel) key.channel();
                if (client.isConnectionPending()){
                    client.finishConnect();
                    System.out.println("连接成功......");
                    new Thread(()->{
                        while (true){
                            try {
                                sBuffer.clear();
                                Scanner scanner = new Scanner(System.in);
                                String sendText = scanner.nextLine();
                                System.out.println(sendText);
                                sBuffer.put(charset.encode("utf-8"));
                                sBuffer.flip();
                                client.write(sBuffer);
                            }catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。
                client.register(selector, SelectionKey.OP_READ);
            }else if(key.isReadable()){
                SocketChannel client =  (SocketChannel)key.channel();
                rBuffer.clear();
                int count = client.read(rBuffer);
                if(count > 0){
                    String receiveText = new String(rBuffer.array(),0,count);
                    System.out.println(receiveText);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws IOException {
        new SocketClient(8888);
    }
}
