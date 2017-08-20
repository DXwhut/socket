package socket;

import java.io.*;
import java.net.Socket;

/**
 * Created by duxin on 2017/8/19.
 */
public class Client {

    static class ClientThread implements Runnable{
        // 该线程负责处理的Socket
        private Socket socket;
        // 该线程所处理的Socket对应的输入流
        BufferedReader br = null;

        public ClientThread(Socket socket) throws IOException{
            this.socket = socket;
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }

        @Override
        public void run() {
            try{
                String content = null;
                // 不断地读取Socket输入流中的内容，并将这些内容打印输出
                while((content = br.readLine()) != null){
                    System.out.println(content);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]) throws IOException{
        Socket socket = new Socket("127.0.0.1", 30000);
        // 客户端启动ClientThread线程不断地读取来自服务器地数据
        new Thread(new ClientThread(socket)).start();
        // 获取该Socket对应的输出流
        PrintStream ps = new PrintStream(socket.getOutputStream());
        String line = null;
        // 不断地读取键盘输入
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while ((line = br.readLine()) != null){
            // 将用户的键盘输入内容写入Socket对应的输出流
            ps.println(line);
        }
//        try{
//            Socket socket=new Socket("127.0.0.1",4700);
//            //向本机的4700端口发出客户请求
//            BufferedReader sin=new BufferedReader(new InputStreamReader(System.in));
//            //由系统标准输入设备构造BufferedReader对象
//            PrintWriter os=new PrintWriter(socket.getOutputStream());
//            //由Socket对象得到输出流，并构造PrintWriter对象
//            BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            //由Socket对象得到输入流，并构造相应的BufferedReader对象
//            String readline;
//            readline=sin.readLine(); //从系统标准输入读入一字符串
//            while(!readline.equals("bye")){
//                //若从标准输入读入的字符串为 "bye"则停止循环
//                os.println(readline);
//                //将从系统标准输入读入的字符串输出到Server
//                os.flush();
//                //刷新输出流，使Server马上收到该字符串
//                System.out.println("Client:"+readline);
//                //在系统标准输出上打印读入的字符串
//                System.out.println("Server:"+is.readLine());
//                //从Server读入一字符串，并打印到标准输出上
//                readline=sin.readLine(); //从系统标准输入读入一字符串
//            } //继续循环
//            os.close(); //关闭Socket输出流
//            is.close(); //关闭Socket输入流
//            socket.close(); //关闭Socket
//        }catch(Exception e) {
//            System.out.println("Error"+e); //出错，则打印出错信息
//        }
    }
}
