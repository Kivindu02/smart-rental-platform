import json
from kafka import KafkaConsumer
from config.settings import KAFKA_BOOTSTRAP_SERVERS, KAFKA_GROUP_ID


class KafkaConsumerService:
    def __init__(self, ai_processor, vector_db):
        """
        Dependency Injection: We pass the initialized AI processor and
        Vector Database service from main.py so this service can use them.
        """
        self.ai = ai_processor
        self.db = vector_db

        self.consumer = KafkaConsumer(
            'property-events', 'review-events',
            bootstrap_servers=KAFKA_BOOTSTRAP_SERVERS,
            group_id=KAFKA_GROUP_ID,
            value_deserializer=lambda m: json.loads(m.decode('utf-8')) if m else None
        )

    def start_listening(self):
        """
        Starts the infinite message polling loop. This method will block the
        thread and wait actively for inbound events from Kafka.
        """
        print("AI Consumer started and listening for events...")
        for message in self.consumer:
            try:

                if message.value is None:
                    print(f"Received empty message from topic [{message.topic}], skipping.")
                    continue

                event = message.value
                topic = message.topic

                action = event.get('action')
                data = event.get('data')

                if not action or not data:
                    print(f"Malformed event received on topic [{topic}], skipping: {event}")
                    continue

                print(f"Received event [{action}] from topic [{topic}]")

                if topic == 'property-events':
                    self._handle_property(data, action)
                elif topic == 'review-events':
                    self._handle_review(data, action)

            except Exception as e:
                print(f"Error processing Kafka message on topic [{message.topic}]: {str(e)}")

    def _handle_property(self, data, action):
        qdrant_id = data['id']

        if action == "DELETE":
            print(f"Removing Property vector ID: {qdrant_id} completely")

            # 1. Delete the property itself
            self.db.delete_point(qdrant_id)

            # 2. Delete all matching reviews linked to this property
            self.db.delete_reviews_by_property(qdrant_id)
            print(f"Successfully wiped property {qdrant_id} and all its reviews from Qdrant.")
            return

        if action in ("CREATE", "UPDATE"):
            text_content = f"{data['name']} is a {data['type']}. {data['description']}"
            vector = self.ai.generate_embedding(text_content)
            payload = {
                "type": "property",
                "details": data
            }
            self.db.upsert_point(qdrant_id, vector, payload)
            print(f"Successfully upserted property vector ID: {qdrant_id}")
        else:
            print(f"Unknown action '{action}' for property event, skipping.")

    def _handle_review(self, data, action):
        qdrant_id = data['id']

        if action == "DELETE":
            print(f"Removing Review vector ID: {qdrant_id} completely")
            self.db.delete_point(qdrant_id)
            return

        if action in ("CREATE", "UPDATE"):
            text_content = f"Review for property {data['propertyId']}: {data['comment']}"
            vector = self.ai.generate_embedding(text_content)
            payload = {
                "type": "review",
                "propertyId": data['propertyId'],
                "rating": data['rating'],
                "author": data['reviewer']['firstName'] if 'reviewer' in data and data['reviewer'] else "Anonymous"
            }
            self.db.upsert_point(qdrant_id, vector, payload)
            print(f"Successfully upserted review vector ID: {qdrant_id} linked to property: {data['propertyId']}")
        else:
            print(f"Unknown action '{action}' for review event, skipping.")