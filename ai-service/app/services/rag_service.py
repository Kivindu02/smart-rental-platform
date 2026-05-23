from groq import Groq
from qdrant_client.models import Filter, FieldCondition, MatchValue
from config.settings import GROQ_API_KEY, GROQ_MODEL

class RAGService:
    def __init__(self, ai_processor, vector_db):
        self.ai = ai_processor
        self.db = vector_db
        self.llm = Groq(api_key=GROQ_API_KEY)

    def chat(self, question: str, history: list[dict]) -> str:
        """
         Full RAG pipeline with multi-turn conversation support:
           1. Embed the question
           2. Retrieve relevant properties + reviews from Qdrant
           3. Build context from results
           4. Send context + full conversation history to Groq
           5. Return the natural language answer

         Args:
             question: Latest message from the user.
             history:  Previous conversation turns from the frontend.
                       Each item is {"role": "user"|"assistant", "content": "..."}

         Returns:
             The assistant's response as a plain string.
         """

        # Step 1 - Embed the question and search Qdrant
        query_vector = self.ai.generate_embedding(question)
        results = self.db.search(query_vector=query_vector, top_k=5)

        # Step 2 - Build context from retrieved results
        context = self._build_context(results)

        # Step 3 - Build the message array for Groq
        messages = self._build_messages(question, history, context)

        # Step 4 - Call Groq
        response = self.llm.chat.completions.create(
            model=GROQ_MODEL,
            messages=messages,
            temperature=0.3,
        )

        return response.choices[0].message.content

    def _build_messages(self, question: str, history: list[dict], context: str) -> list[dict]:
        """
        Assembles the full message array to send to Groq:
        [system prompt] + [conversation history] + [current question with context]
        """
        system_prompt = {
            "role": "system",
            "content": (
                "You are a helpful assistant for a smart property rental platform. "
                "Answer the user's questions using the property and review context provided. "
                "If the context doesn't contain enough information, say so honestly. "
                "Keep your answers concise, helpful, and friendly. "
                "When listing properties, include their name, type, and description."
            )
        }

        # Current question injected with retrieved context
        current_message = {
            "role": "user",
            "content": (
                f"Context from our rental platform (properties and reviews):\n{context}\n\n"
                f"User question: {question}"
            )
        }

        # Full conversation: system + history + current question
        return [system_prompt] + history + [current_message]

    def _build_context(self, results) -> str:
        """
        Converts Qdrant search results into a readable context string
        for the LLM prompt.
        """
        if not results:
            return "No relevant properties or reviews found"

        context_parts = []

        for i, result in enumerate(results, start=1):
            payload = result.payload
            record_type = payload.get("type")

            if record_type == "property":
                details = payload.get("details", {})
                context_parts.append(
                    f"[Property {i}]\n"
                    f"Name: {details.get('name', 'N/A')}\n"
                    f"Type: {details.get('type', 'N/A')}\n"
                    f"Description: {details.get('description', 'N/A')}\n"
                )

            elif record_type == "review":
                context_parts.append(
                    f"[Review {i}]\n"
                    f"Property ID: {payload.get('propertyId', 'N/A')}\n"
                    f"Rating: {payload.get('rating', 'N/A')}/5\n"
                    f"Author: {payload.get('author', 'Anonymous')}\n"
                )

        return "\n".join(context_parts)




