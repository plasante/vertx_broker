# Vegeta Load Testing

# In Windows PowerShell

> cd C:\Users\pierr\Downloads\vertx-share-broker

> echo "GET http://localhost:8888/assets" | vegeta attack -duration=10s -rate=10 -output=results.bin
