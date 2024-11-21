package model;

import com.mongodb.client.model.ValidationOptions;
import org.bson.Document;

public class ClientValidation {
    public static ValidationOptions options = new ValidationOptions().validator(
        Document.parse("""
                {
                  "$jsonSchema": {
                     "bsonType": "object",
                     "required": ["personal_id", "address"],
                     "properties": {
                        "personal_id": {
                           "bsonType": "string",
                           "minLength": 11,
                           "description": "Personal ID must be exactly 11 digits"
                        },
                        "address": {
                           "bsonType": "string",
                           "minLength": 1,
                           "description": "Address cannot be empty"
                        }
                     }
                  }
                }
            """)
    );
}
