package bocsodf.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

/**
 *
 * ClassName: SocketReceive
 * Function: Socket接收处理
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
public class SocketReceive implements Runnable{
    private Socket socket;
    private Vector<Socket> allclients;

    public SocketReceive() {
    }

    public SocketReceive(Socket socket, Vector<Socket> clients) {
        this.socket = socket;
        allclients = clients;
    }

    @Override
    public void run() {
        while (true) {
            if (null == socket) {
                if (allclients.contains(socket)) {
                    allclients.remove(socket);
                }
                return;
            }
            boolean isClosed = socket.isClosed();
            if (isClosed) {
                if (allclients.contains(socket)) {
                    allclients.remove(socket);
                }
                return;
            }
            try {
                //获取服务端传递的消息
                DataInputStream in = new DataInputStream(socket.getInputStream());
                byte[] buffer = new byte[1024 * 4];
                if (in.read(buffer) == -1) {
                    if (allclients.contains(socket)) {
                        allclients.remove(socket);
                    }
                    return;
                }
                //解析报文，并处理返回
                DataAnalysis dataanalysis = new DataAnalysis();
                dataanalysis.Analysis(buffer, socket);
            } catch (IOException e) {
                System.out.println(e);
                if (allclients.contains(socket)) {
                    allclients.remove(socket);
                }
                try {
                    socket.close();
                    return;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
