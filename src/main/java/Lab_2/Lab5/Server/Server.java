package Lab_2.Lab5.Server;

import com.ibm.mq.*;
import com.ibm.mq.MQQueue;
import com.ibm.mq.jms.*;
import com.ibm.msg.client.jms.JmsConnectionFactory;
import com.ibm.msg.client.jms.JmsFactoryFactory;
import com.ibm.msg.client.wmq.WMQConstants;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSC;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSMessage;
import com.ibm.msg.client.wmq.compat.jms.internal.JMSTextMessage;

import javax.jms.*;

public class Server {
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

    // Запуск сервера
    public void start(String QMName, String IP, int port,
                      String channel, String Q1_name, String Q2_name)
            throws MQException, JMSException {
        try {
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
            receiver1 = (MQQueueReceiver) session.createReceiver((Queue) queue1);
            sender1 = (MQQueueSender) session.createSender((Queue) queue1);

            queue2 = (MQQueue) session.createQueue("queue:///" + Q2_name);
            sender2 = (MQQueueSender) session.createSender((Queue) queue2);
            receiver2 = (MQQueueReceiver) session.createReceiver((Queue) queue2);

            long uniqueNumber = System.currentTimeMillis() % 1000;
            JMSTextMessage message = (JMSTextMessage) session.createTextMessage("SimplePTP " + uniqueNumber);

            // Start the connection
            connection.start();

            int i = 0;
            while (processQuery()) i++;

            System.out.println("\\nSUCCESS\\n");
        } catch (Exception jmsex) {
            System.out.println(jmsex.getMessage());
            System.out.println("\\nFAILURE\\n");
        } finally {
            sender1.close();
            sender2.close();
            receiver2.close();
            receiver1.close();
            session.close();
            connection.close();
        }
    }


    // Обработка сообщения-запроса
    public boolean processQuery() {
        try {
            // Настраиваю интервал ожидания 3 сек.
            MQGetMessageOptions gmo = new MQGetMessageOptions();
            gmo.waitInterval = 3000;

            // Читаю сообщение из очереди запросов
            MQMessage receivedMessage = (MQMessage) receiver2.receive(10000);
            System.out.println("\\nReceived message:\\n" + receivedMessage);

            // Обрабатываю сообщение
            int oper = receivedMessage.readInt(); // Операция
            int v1 = receivedMessage.readInt(); // Число1
            int v2 = receivedMessage.readInt(); // Число2

            // Считаю результат
            int result; // Результат операции
            result = (oper == 0) ? (v1 + v2) : (v1 - v2);

            // Формирую ответ
            MQMessage response = new MQMessage();
            response.writeInt(oper);
            response.writeInt(v1);
            response.writeInt(v2);
            response.writeInt(result);

            // Отправляю ответ
            sender1.send((Message) response);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args)
            throws MQException, JMSException {
        (new Server()).start("QM1", "localhost", 1414,
                "SYSTEM.DEF.SVRCONN",
                "SRV.Q", "CL.Q");
    }
}
