package payroll;

import com.amazonaws.services.sqs.model.Message;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.util.List;
import java.util.Map;


@SpringBootApplication
public class PayrollApplication {

    private static final String QUEUE_NAME = "PaulQueue";

    public static void main(String... args) {
        SpringApplication.run(PayrollApplication.class, args);

        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String queue_url = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        ReceiveMessageRequest receive_request = new ReceiveMessageRequest()
                .withQueueUrl(queue_url)
                .withWaitTimeSeconds(20);
        final List<Message> messages = sqs.receiveMessage(receive_request).getMessages();
        for (final Message message : messages) {
            System.out.println("Message");
            System.out.println("  MessageId:     " + message.getMessageId());
            System.out.println("  ReceiptHandle: " + message.getReceiptHandle());
            System.out.println("  MD5OfBody:     " + message.getMD5OfBody());
            System.out.println("  Body:          " + message.getBody());
            for (final Map.Entry<String, String> entry : message.getAttributes().entrySet()) {
                System.out.println("Attribute");
                System.out.println("  Name:  " + entry.getKey());
                System.out.println("  Value: " + entry.getValue());
            }
        }
        System.out.println();
    }
}