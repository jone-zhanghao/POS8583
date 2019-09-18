package bocsodf.service;

public class TestPOSService {

    public static void main(String[] main){

        System.out.println("---------- 启动Socket线程池       ---------- ");

        TcpSocket tcpSocket = new TcpSocket();
        Thread serverthread = new Thread(tcpSocket);
        //启动线程
        serverthread.start();
    }

}
