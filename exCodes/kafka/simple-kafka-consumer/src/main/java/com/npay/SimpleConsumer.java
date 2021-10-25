package com.npay;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SimpleConsumer {
    private final static Logger logger = LoggerFactory.getLogger(SimpleConsumer.class);
    private final static String[] TOPIC_NAME = new String[]{"test"};
    private final static String BOOTSTRAP_SERVERS = "dev-internship01-npay-ncl:9092";
    private final static String GROUP_ID = "test-group";

    public static void main(String[] args) {
        Properties configs = new Properties();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);

        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);//for manual commit

        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(configs);
        consumer.subscribe(Arrays.asList(TOPIC_NAME));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            Map<TopicPartition, OffsetAndMetadata> currentOffset = new HashMap<>();//for commitSync param

            for (ConsumerRecord<String, String> record : records) {
                logger.info("{}", record);
//                currentOffset.put(
//                        new TopicPartition(record.topic(), record.partition()),
//                        new OffsetAndMetadata(record.offset() + 1, null)
//                );
//                consumer.commitSync(currentOffset);
            }
//            consumer.commitSync(); // 동기 커밋 - poll() 마지막 레코드 기준
//            consumer.commitAsync();//비동기 - poll() 마지막 레코드 기준, 응답 안 기다림
            consumer.commitAsync(new OffsetCommitCallback() {
                @Override
                public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
                    if (exception != null) {
                        System.err.println("Commit failed");
                        logger.error("Commit failed for offsets {}", offsets, exception);
                    }
                    else{
                        logger.info("Commit succeeded for offsets {}", offsets);
                    }

                }
            });
        }
    }
}
