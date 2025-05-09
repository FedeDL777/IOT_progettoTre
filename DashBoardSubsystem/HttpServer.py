from flask import Flask, request, jsonify
import threading

class HttpServer:
    def __init__(self, dashboardApp):
        # Inizializza Flask nell'istanza
        self.app = Flask(__name__)
        self.dashboardApp = dashboardApp
        self.app.add_url_rule('/send', 'receive_message', self.receive_message, methods=['POST'])
        self.app.add_url_rule('/ping', 'ping', self.ping, methods=['GET'])

    def receive_message(self):
        # Riceve il messaggio e lo converte in un tipo float
        status = request.json["status"]
        temperature = float(request.json["temperature"])
        level = int(request.json["level"])
        print(f"Received message: {status} - {temperature} - {level}")
        # Chiamata alla funzione della dashboard per aggiornare la temperatura
        self.dashboardApp.update_level(level)
        self.dashboardApp.update_state(status)
        self.dashboardApp.update_temperature(temperature)
        # Restituisce lo stato dell'allarme
        return jsonify({"status": self.dashboardApp.getState(), "window_level": self.dashboardApp.get_window_level()})

    def ping(self):
        return jsonify({"message": "Server attivo!"})

    def run(self):
        # Avvia il server Flask
        self.app.run(debug=True, port=5000, use_reloader=False)