package DecentralizedVSerializable;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class RequestService {
    private static final String EXCHANGE_NAME = "finki";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("51.83.68.66");
        Scanner in = new Scanner(System.in);
        System.out.println("Изберете 1 за STORAGE, 2 за COMPUTE");
        var storageCompute = in.nextLine();
        System.out.println("Внесете колку сакате да алоцирате рам меморија");
        var ram = in.nextLine();
        System.out.println("Внесете број на јадра");
        var cores = in.nextLine();
        System.out.println("Внесете број на секунди за извршување на процесот");
        var seconds = in.nextLine();

        try(Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME,"topic");
            var routingKey = storageCompute.equals("1") ? "storage" : "compute";

            RequestMessage message = new RequestMessage();
            message.setStorageCompute(routingKey);
            message.setRam(Integer.parseInt(ram));
            message.setCores(Integer.parseInt(cores));
            message.setSeconds(Integer.parseInt(seconds));

            var serialized = getByteArray(message);

            channel.basicPublish(EXCHANGE_NAME,routingKey,null,serialized);

        } catch (IOException e) {
            System.out.println("Problem so konekcija");
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static byte[] getByteArray(RequestMessage msg) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(msg);
        return out.toByteArray();
    }
}
