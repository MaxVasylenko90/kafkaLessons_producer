package dev.mvasylenko.service.impl;

import dev.mvasylenko.entity.ProductModel;
import dev.mvasylenko.event.ProductCreatedEvent;
import dev.mvasylenko.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate;

    public ProductServiceImpl(KafkaTemplate<String, ProductCreatedEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public String createProduct(ProductModel product) throws Exception {
        String productId = UUID.randomUUID().toString();
        //saving product in db
        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
                productId, product.getTitle(),
                product.getPrice(),product.getQuantity());

        LOGGER.info("BEFORE!!!!!!!!!");
        //with .get() - syncronious approach, with .whenComplete() or without anything - asynchronious
//        SendResult<String, ProductCreatedEvent> result =
//                kafkaTemplate.send("topic6", productId, productCreatedEvent).get();
        kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);

//        CompletableFuture<SendResult<String, ProductCreatedEvent>> future =
//                kafkaTemplate.send("product-created-events-topic", productId, productCreatedEvent);
//        future.whenComplete((result, exception) -> {
//            if (exception != null) {
//                LOGGER.error("**************Failed to send message: {}", exception.getMessage());
//            } else {
//                LOGGER.info("***************Successfully sent message: {}", result.getRecordMetadata());
//            }
//        });
//
//        future.join(); //block the current thread before the future will complete and return some result
//
//        LOGGER.info("Partition: " + result.getRecordMetadata().partition());
//        LOGGER.info("Topic name: " + result.getRecordMetadata().topic());
//        LOGGER.info("Offset: " + result.getRecordMetadata().offset());
        LOGGER.info("**********************");
        return productId;
    }
}
