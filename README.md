# AutoSearchComplete

An autocomplete search system built with Spring Boot and PostgreSQL provides real-time query suggestions as users type. Search terms and their frequencies are stored in PostgreSQL and loaded into an in-memory Trie for efficient prefix-based lookup. A Min-Heap is used to retrieve the top matching queries based on frequency. The system exposes REST APIs through Spring Boot to deliver fast and relevant suggestions.



##  Features

- Instant autocomplete suggestions as the user types
- Frequency-based ranking — more popular searches appear first
- E-commerce focused synthetic dataset (phones, laptops, accessories, etc.)
- RESTful API with clean separation of concerns (Controller → Service → Repository)
- Persistent storage with PostgreSQL — search data survives restarts
- Trie built in-memory at startup from the database for fast lookups


##  System Architecture

```
User Input (query string)
        │
        ▼
┌─────────────────────┐
│   AutoComplete      |
|      Controller     |      ← REST API layer
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│ AutoCompleteService │      ← Business logic: Trie + Heap
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│     PostgreSQL DB   │      ← Stores search phrases + frequencies
└─────────────────────┘
```


## How It Works — Trie + Heap

### Trie (Prefix Tree)
A Trie is a tree-like data structure where each node represents a character. Words that share a common prefix share the same path from the root.

```
root
 └── n
      └── i
           └── k
                └── e  ✓  ("nike shoes buy online")
                     └── ...
```

When a user types `"nik"`, the system traverses the Trie to the node at `k` and collects all complete words/phrases in that subtree.

### Min-Heap (Priority Queue)
After collecting all matching phrases from the Trie, a Min-Heap of fixed size k (e.g., top 10) is used to efficiently find the highest-frequency suggestions without sorting the entire list.

- Heap maintains only the top-k results at any time
- Time complexity for suggestions: **O(n log k)** where n = matches in subtree
- Much faster than sorting all matches when k is small

### Combined Flow
```
1. App starts → Load all phrases + frequencies from PostgreSQL
2. Build Trie in memory, storing frequency at each terminal node
3. User types "sam" → Traverse Trie to prefix node
4. Collect all phrases under that node
5. Use Min-Heap to extract top-k by frequency
6. Return ranked suggestions to client
```



## Dataset

A synthetic e-commerce search dataset was generated with realistic search phrases and associated frequencies simulating real user behavior.



##  API Endpoints

### GET `/api/search/autocomplete`
Returns top autocomplete suggestions for a given query prefix.

**Request:**
```
GET /api/search/autocomplete?query=sam
```

**Response:**
```json
[
  "samsung monitor discount for work",
  "samsung phone buy online",
  "samsung charger specs 2026"
]
```



### POST `/api/search`
Records a new search query (used to update frequency counts).

**Request:**
```json
{
  "query": "samsung monitor discount for work"
}
```

**Response:**
```
200 OK
```

## Setup & Running Locally

### Prerequisites
- Java 17+
- Maven
- PostgreSQL running on port 5433

### 1. Clone the repository
```bash
git clone https://github.com/Smruthi0204/AutoSearchComplete.git
cd AutoSearchComplete
```

### 2. Configure the database

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/AutoComplete_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

### 3. Create the database
```sql
CREATE DATABASE AutoComplete_db;
```
Import the csv file(autocomplete_10k_dataset) into the schema

### 4. Run the application
```bash
./mvnw spring-boot:run
```

The server starts on port 8081.

### 5. Test the API
```bash
curl "http://localhost:8081/api/search/autocomplete?query=sam"
```


