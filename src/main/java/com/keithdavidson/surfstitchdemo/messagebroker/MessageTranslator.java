package com.keithdavidson.surfstitchdemo.messagebroker;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keithdavidson.surfstitchdemo.db.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Two way translation between messages in JSON format, and Product objects.
 */
public class MessageTranslator {
    private static final Logger log = LoggerFactory.getLogger(MessageTranslator.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    public MessageTranslator() {
    }

    public Product getProduct(String jsonMessage) {
        try {
            return objectMapper.readValue(jsonMessage, Product.class);
        }catch(JsonParseException e) {
            log.error("Message failed to parse: " + e.getMessage() + ". Message body: " + jsonMessage);
            return null;
        }catch(JsonMappingException e) {
            log.error("Message failed to map: " + e.getMessage() + ". Message body: " + jsonMessage);
            return null;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public String getJsonMessage(Product product) {
        try {
            return objectMapper.writeValueAsString(product);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }
}
