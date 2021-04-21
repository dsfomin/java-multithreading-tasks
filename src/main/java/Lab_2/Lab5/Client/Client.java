package Lab_2.Lab5.Client;

import com.ibm.mq.*;
import com.ibm.mq.MQQueue;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSC;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.IOException;
public class Client
{
    private MQQueueConnectionFactory cf;
    private MQQueueSession session;
    private MQQueueConnection connection;

    private MQQueueManager QM = null; // Менеджер очередей

    private MQQueue queue1 = null; // Очередь запросов
    private MQQueueSender sender1 = null;
    private MQQueueReceiver receiver1 = null;

    private MQQueue queue2 = null; // Очередь запросов
    private MQQueueSender sender2 = null;
    private MQQueueReceiver receiver2 = null;
    // конструктор
    public Client(String QMName, String IP,
                  int port, String channel,
                  String Q1_name, String Q2_name)
            throws IOException, MQException, JMSException {
        cf = new MQQueueConnectionFactory();

        // Config
        cf.setHostName(IP);
        cf.setPort(port);
        cf.setTransportType(JMSC.MQJMS_TP_CLIENT_MQ_TCPIP);
        cf.setQueueManager(QMName);
        cf.setChannel(channel);

        connection = (MQQueueConnection) cf.createQueueConnection();
        session = (MQQueueSession) connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

        queue1 = (MQQueue) session.createQueue("queue:///" + Q1_name);
        sender1 = (MQQueueSender) session.createSender((Queue) queue1);
        receiver1 = (MQQueueReceiver) session.createReceiver((Queue) queue1);

        queue2 = (MQQueue) session.createQueue("queue:///" + Q2_name);
        sender2 = (MQQueueSender) session.createSender((Queue) queue2);
        receiver2 = (MQQueueReceiver) session.createReceiver((Queue) queue2);

        long uniqueNumber = System.currentTimeMillis() % 1000;
        JMSTextMessage message = (JMSTextMessage) session.createTextMessage("SimplePTP " + uniqueNumber);

        // Start the connection
        connection.start();
    }
    // отправить запрос серверу
    private void sendQuery(int operation, int value1, int value2)
            throws IOException, MQException, JMSException {
        // Создаю сообщение-запрос
        MQMessage query = new MQMessage();
        query.writeInt(operation); // Операция
        query.writeInt(value1); // Число1
        query.writeInt(value2); // Число2

        // Записываю в очередь запросов
        sender2.send((Message) query);
    }
    // посчитать сумму чисел
    public void sum(int value1, int value2)
            throws IOException, MQException, JMSException {
        sendQuery(0, value1, value2);
    }

    // посчитать разность чисел
    public void sub(int value1, int value2)
            throws IOException, MQException, JMSException {
        sendQuery(1, value1, value2);
    }

    // Получить ответ от сервера
    public boolean printResult()
    {
        try
        {
            // Читаю сообщение из очереди ответов
            MQMessage response = new MQMessage();
            receiver1.receive();
            int oper = response.readInt(); // Операция
            int v1 = response.readInt(); // Число1
            int v2 = response.readInt(); // Число2
            int res = response.readInt(); // Результат
            String s = (oper==0)? "+" : "-";
            System.out.println(v1 + s + v2 + " = " + res);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }
    // отсоединиться
    public void disconnect()
            throws IOException, MQException, JMSException {
        sender1.close();
        sender2.close();
        receiver2.close();
        receiver1.close();
        session.close();
        connection.close();
    }
    // ПЕРВЫЙ ЗАПУСК КЛИЕНТА
    public static void main1()
    {
        try
        {
            Client client = new Client("QM1","localhost",
                    1414, "SYSTEM.DEF.SVRCONN",
                    "SRV.Q","CL.Q");
            client.sum(15,20);
            client.sub(30,38);
            client.sum(100,200);
            client.disconnect();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    // ВТОРОЙ ЗАПУСК КЛИЕНТА
    public static void main2()
            throws IOException, MQException, JMSException {
        Client client = new Client("QM1","localhost",
                1414, "SYSTEM.DEF.SVRCONN",
                "SRV.Q","CL.Q");
        while (client.printResult());
    }
    public static void main(String[] args)
            throws IOException, MQException
    {
        main1(); // или main2()
    }
}
