from fastapi import APIRouter, Request, HTTPException
from pydantic import BaseModel

router = APIRouter(prefix="/ai", tags=["AI"])

class ChatMessage(BaseModel):
    role: str     # "user" or "assistant"
    content: str

class ChatRequest(BaseModel):
    question: str
    history: list[ChatMessage] = []

@router.post("/chat")
def chat(body: ChatRequest, request: Request):
    """
    Multi-turn RAG chatbot endpoint.

    The frontend sends the current question + full conversation history.
    The backend retrieves relevant context from Qdrant and generates
    a natural language answer using Groq.

    Request body:
    {
        "question": "which properties have good reviews?",
        "history": [
            {"role": "user",      "content": "find me a beachfront villa"},
            {"role": "assistant", "content": "I found Sunset Beach Villa..."}
        ]
    }
    """
    try:
        rag = request.app.state.rag

        # Convert pydantic models to plain dicts for Groq
        history = [{"role": m.role, "content": m.content} for m in body.history]

        answer  = rag.chat(
            question=body.question,
            history=history,
        )

        return {"answer": answer}

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/health")
def health():
    """Simple health check."""
    return {"status": "ok", "service": "ai-service"}



