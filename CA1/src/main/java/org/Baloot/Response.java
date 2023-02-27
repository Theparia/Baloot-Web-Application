package org.Baloot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Response {
    private boolean success;
    private ObjectNode data;

    public Response(boolean success, ObjectNode data) {
        this.success = success;
        this.data = data;
    }

    public void print() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode node = objectMapper.convertValue(this, ObjectNode.class);
        System.out.println(node);
    }
}
