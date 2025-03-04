import requests

server_url = "http://127.0.0.1:5000/send"

data = {"temperature": "22.5", "status": "NORMAL"}

response = requests.post(server_url, json=data)  # Invia una richiesta POST
print(response.json())  # Stampa la risposta del server