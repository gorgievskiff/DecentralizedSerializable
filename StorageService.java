package DecentralizedVSerializable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeoutException;

public class StorageService {
    private static final String EXCHANGE_NAME = "finki";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("51.83.68.66");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        var queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName,EXCHANGE_NAME,"storage");
        channel.basicQos(1);

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            byte[] byteArray = delivery.getBody();
            try {
                RequestMessage obj = (RequestMessage) deserialize(byteArray);
                System.out.println(" [X] Received");
                System.out.println(" \tRAM: " + obj.getRam() + "\tCORES: " + obj.getCores() + "\tSECONDS: " + obj.getSeconds());
                Storage(obj.getSeconds());
                System.out.println(" [x] Saved");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        };

        channel.basicConsume(queueName,false,deliverCallback,consumerTag -> {});
    }

    public static Object deserialize(byte[] byteArray) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    private static void Storage(int seconds) {
        try {
            Thread.sleep(seconds*1000);
        } catch (InterruptedException _ignored) {
            Thread.currentThread().interrupt();
        }
    }
}
