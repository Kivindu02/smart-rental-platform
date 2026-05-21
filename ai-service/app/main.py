from services.ai_processor import AIProcessorService
from services.vector_db import VectorDBService
from services.kafka_consumer import KafkaConsumerService


def main():
    print("=== Starting AI Service ===")

    # 1. Load the embedding model
    ai_processor = AIProcessorService()

    # 2. Connect to Qdrant and ensure collection exists
    vector_db = VectorDBService()

    # 3. Wire both into the Kafka consumer and start listening
    consumer = KafkaConsumerService(
        ai_processor=ai_processor,
        vector_db=vector_db,
    )

    # Blocks here — processes Kafka events indefinitely
    consumer.start_listening()


if __name__ == "__main__":
    main()