package bocsodf.service;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * ClassName: TcpSocket
 * Function: Tcp线程
 * date: 2019-9-18 上午10:34:39
 *
 * @author ZhangHao
 */
@Component
public class TcpSocket implements Runnable{
    private Integer Port;
    private ServerSocket Server;
    private ExecutorService ThreadPool;
    public Vector<Socket> AllClients;

    public TcpSocket() {
        try {
            AllClients = new Vector<>();
            AllClients.clear();
            Port = 9195;
            ThreadPool = Executors.newCachedThreadPool();
            Server = new ServerSocket(Port);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = Server.accept();
                if (socket != null) {
                    SocketReceive socketReceive = new SocketReceive(socket, AllClients);
                    ThreadPool.submit(socketReceive);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
