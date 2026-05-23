import threading
from contextlib import asynccontextmanager
from fastapi import FastAPI

from services.ai_processor import AIProcessorService
from services.vector_db import VectorDBService
from services.kafka_consumer import KafkaConsumerService
from services.rag_service import RAGService
from routers.ai_router import router


@asynccontextmanager
async def lifespan(app: FastAPI):
    """
    Startup:
      1. Load embedding model
      2. Connect to Qdrant
      3. Wire RAG service and store in app.state
      4. Start Kafka consumer in background thread

    Shutdown:
      - Kafka thread stops automatically (daemon=True)
    """
    print("=== Starting AI Service ===")

    # 1. Load embedding model
    ai_processor = AIProcessorService()

    # 2. Connect to Qdrant
    vector_db = VectorDBService()

    # 3. Wire RAG service — accessible to all routes via app.state
    app.state.rag = RAGService(ai_processor, vector_db)

    # 4. Start Kafka consumer in background daemon thread
    consumer     = KafkaConsumerService(ai_processor, vector_db)
    kafka_thread = threading.Thread(target=consumer.start_listening, daemon=True)
    kafka_thread.start()
    print("Kafka consumer thread started.")

    yield

    print("=== Shutting down AI Service ===")


# FastAPI App

app = FastAPI(
    title="AI Service",
    description="RAG chatbot for the Smart Rental Platform",
    version="1.0.0",
    lifespan=lifespan,
)

app.include_router(router)