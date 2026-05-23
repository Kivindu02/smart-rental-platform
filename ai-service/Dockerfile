FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .

# Install CPU-only torch FIRST to prevent pip pulling GPU version
RUN pip install --no-cache-dir torch --extra-index-url https://download.pytorch.org/whl/cpu

# Then install the rest
RUN pip install --no-cache-dir -r requirements.txt

ARG EMBEDDING_MODEL_NAME=all-MiniLM-L6-v2
RUN python -c "from sentence_transformers import SentenceTransformer; SentenceTransformer('${EMBEDDING_MODEL_NAME}')"

COPY . .

WORKDIR /app/app

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "4003"]