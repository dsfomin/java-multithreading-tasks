package Lab_2.Lab5.Client;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class Client {
    ActiveMQConnectionFactory connectionFactory;

    Connection connection;

    Session session;

    Destination destination1;
    Destination destination2;

    MessageProducer producer1;
    MessageConsumer consumer1;

    MessageProducer producer2;
    MessageConsumer consumer2;

    public Client(String QMName, String IP,
                  int port, String channel,
                  String Q1_name, String Q2_name) throws IOException, JMSException {
        connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61611");

        // Create a Connection
        connection = connectionFactory.createConnection();

        // Create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Create the destination (Topic or Queue)
        destination1 = session.createQueue("CL.SRV");

        // Create a MessageProducer from the Session to the Topic or Queue
        producer1 = session.createProducer(destination1);
        producer1.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        consumer1 = session.createConsumer(destination1);

        // Create the destination (Topic or Queue)
        destination2 = session.createQueue("SRV.CL");

        // Create a MessageProducer from the Session to the Topic or Queue
        producer2 = session.createProducer(destination2);
        producer2.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        consumer2 = session.createConsumer(destination2);

        connection.start();
    }

    // отправить запрос серверу
    private void sendQuery(int operation, int value1, int value2)
            throws IOException, JMSException {
        // Создаю сообщение-запрос
        String text = "Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode();
        TextMessage message = session.createTextMessage(text);

        // Записываю в очередь запросов
        System.out.println("Send query " + message.toString());
        producer1.send(message);
    }

    // посчитать сумму чисел
    public void sum(int value1, int value2)
            throws IOException, JMSException {
        sendQuery(0, value1, value2);
    }

    // посчитать разность чисел
    public void sub(int value1, int value2)
            throws IOException, JMSException {
        sendQuery(1, value1, value2);
    }

    // Получить ответ от сервера
    public boolean printResult() {
        try {
            // Читаю сообщение из очереди ответов
            Message message = consumer2.receive(1000);
            if (message != null) {
                int oper = message.getIntProperty("operation"); // Операция
                int v1 = message.getIntProperty("value1"); // Число1
                int v2 = message.getIntProperty("value2"); // Число2
                int res = message.getIntProperty("result"); // Результат
                String s = (oper == 0) ? "+" : "-";
                System.out.println(v1 + s + v2 + " = " + res);
            }
            return true;
        } catch (Exception e) {
            System.out.println("WTF" + e.getMessage());
            return false;
        }
    }

    // отсоединиться
    public void disconnect()
            throws JMSException {
        producer1.close();
        consumer1.close();
        producer2.close();
        consumer2.close();
        session.close();
        connection.close();
    }

    public static void main(String[] args) throws IOException, JMSException {
        try {
            Client client = new Client("QM1", "localhost",
                    1414, "SYSTEM.DEF.SVRCONN",
                    "SRV.Q", "CL.Q");
            client.sum(15, 20);
            client.sub(30, 38);
            client.sum(100, 200);
            client.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
