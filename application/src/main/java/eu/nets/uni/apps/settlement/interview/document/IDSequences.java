package eu.nets.uni.apps.settlement.interview.document;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "pk_id_sequences")
public class IDSequences {
    private String id;
    private Long sequenceNumber;
}