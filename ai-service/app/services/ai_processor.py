from sentence_transformers import SentenceTransformer
from config.settings import EMBEDDING_MODEL_NAME

class AIProcessorService:
    def __init__(self):
        """
       Loads the sentence-transformer model into memory once on startup.
       """
        print(f"Loading embedding model: '{EMBEDDING_MODEL_NAME}'")
        self.model = SentenceTransformer(EMBEDDING_MODEL_NAME)
        print("Embedding model loaded successfully.")

    def generate_embedding(self, text: str) -> list[float]:
        """
     Converts plain text into a fixed-size vector.

     Args:
         text: The raw text to embed (property description, review comment, etc.)

     Returns:
         A list of floats representing the semantic vector.
     """
        if not text or not text.strip():
            raise ValueError("Cannot generate an embedding for empty text.")

        embedding = self.model.encode(text, convert_to_numpy=True)
        return embedding.tolist()