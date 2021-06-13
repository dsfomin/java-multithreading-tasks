package Lab_2.Lab5.Server;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Server implements ExceptionListener {
    ActiveMQConnectionFactory connectionFactory;

    Connection connection;

    Session session;

    Destination destination1;
    Destination destination2;

    MessageProducer producer1;
    MessageConsumer consumer1;

    MessageProducer producer2;
    MessageConsumer consumer2;

    // Запуск сервера
    public void start(String QMName, String IP, int port,
                      String channel, String Q1_name, String Q2_name)
            throws JMSException {
        try {
            connectionFactory = new ActiveMQConnectionFactory("admin","admin","tcp://localhost:61611");

            // Create a Connection
            connection = connectionFactory.createConnection();
            connection.setExceptionListener(this);

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

            while (processQuery()) {}
            System.out.println("\\nSUCCESS\\n");

            producer1.close();
            consumer1.close();
            producer2.close();
            consumer2.close();
            session.close();
            connection.close();
        } catch (Exception jmsex) {
            System.out.println(jmsex.getMessage());
            System.out.println("\\nFAILURE\\n");
        }
    }


    // Обработка сообщения-запроса
    public boolean processQuery() {
        try {

            // Читаю сообщение из очереди запросов
            TextMessage receivedMessage = (TextMessage) consumer1.receive(1000);
            System.out.println("Received message:" + receivedMessage);

            // Обрабатываю сообщение
            if (receivedMessage != null) {
                int oper = receivedMessage.getIntProperty("operation"); // Операция
                int v1 = receivedMessage.getIntProperty("value1"); // Число1
                int v2 = receivedMessage.getIntProperty("value2");  // Число2

                // Считаю результат
                int result; // Результат операции
                result = (oper == 0) ? (v1 + v2) : (v1 - v2);

                // Формирую ответ
                Message response = session.createMessage();
                Message query = session.createMessage();
                query.setIntProperty("operation", oper); // Операция
                query.setIntProperty("value1", v1); // Число1
                query.setIntProperty("value2", v2); // Число2
                query.setIntProperty("result", result); // Число2

                // Отправляю ответ
                producer2.send(response);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static void main(String[] args)
            throws JMSException {
        (new Server()).start("QM1", "localhost", 1414,
                "SYSTEM.DEF.SVRCONN",
                "SRV.Q", "CL.Q");
    }

    @Override
    public void onException(JMSException exception) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
}
