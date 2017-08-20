package socket;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by duxin on 2017/8/19.
 */
public class Server {


    // 定义保存所有Socket的ArrayList，并将其包装为线程安全的
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<Socket>());

    static class ServerThread implements Runnable{

        // 定义当前线程所处理的Socket
        Socket socket = null;
        // 该线程所处理的Socket对应的输入流
        BufferedReader br = null;
        public ServerThread(Socket socket) throws IOException{
            this.socket = socket;
            // 初始化该Socket对应的输入流
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try{
                String content = null;
                // 采用循环不断地从Socket中读取客户端发送过来的数据
                while ((content = readFromClient()) != null){
                    // 遍历socketList中的每个Socket
                    // 将读到的内容向每个Socket发送一次
                    for(Socket socket : Server.socketList){
                        PrintStream ps = new PrintStream(socket.getOutputStream());
                        ps.println(content);
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        // 定义读取客户端数据的方法
        private String readFromClient(){
            try {
                return br.readLine();
            }catch (IOException e){
                // 如果捕获到异常，则表明该Socket对应的客户端已经关闭
                // 删除该Socket
                Server.socketList.remove(socket);
            }
            return null;
        }
    }

    public static void main(String args[]) throws IOException{
        ServerSocket server = new ServerSocket(30000);
        while (true){
            // 此行代码会阻塞，将一直等待别人的连接
            Socket socket = server.accept();
            socketList.add(socket);
            // 每当客户端连接后就启动一个ServerThread线程为该客户端服务
            new Thread(new ServerThread(socket)).start();
        }
//        try{
//            ServerSocket server=null;
//            try{
//                server=new ServerSocket(4700);//创建一个ServerSocket在端口4700监听客户请求
//            }catch(Exception e) {
//                System.out.println("can not listen to:"+e);//出错，打印出错信息
//            }
//            Socket socket=null;
//            try{
//                socket=server.accept();
//                //使用accept()阻塞等待客户请求，有客户请求到来则产生一个Socket对象，并继续执行
//            }catch(Exception e) {
//                System.out.println("Error."+e);//出错，打印出错信息
//            }
//            String line;
//            BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));//由Socket对象得到输入流，并构造相应的BufferedReader对象
//            PrintWriter os=new PrintWriter(socket.getOutputStream());//由Socket对象得到输出流，并构造PrintWriter对象
//            BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));//由系统标准输入设备构造BufferedReader对象
//            System.out.println("Client:"+is.readLine());//在标准输出上打印从客户端读入的字符串
//            line=sin.readLine();//从标准输入读入一字符串
//            while(!line.equals("bye")){
//                //如果该字符串为 "bye"，则停止循环
//                os.println(line);
//                //向客户端输出该字符串
//                os.flush();
//                //刷新输出流，使Client马上收到该字符串
//                System.out.println("Server:"+line);
//                //在系统标准输出上打印读入的字符串
//                System.out.println("Client:"+is.readLine());
//                //从Client读入一字符串，并打印到标准输出上
//                line=sin.readLine();
//                //从系统标准输入读入一字符串
//            } //继续循环
//            os.close(); //关闭Socket输出流
//            is.close(); //关闭Socket输入流
//            socket.close(); //关闭Socket
//            server.close(); //关闭ServerSocket
//        }catch(Exception e){
//            System.out.println("Error:"+e);
//            //出错，打印出错信息
//        }
    }
}
