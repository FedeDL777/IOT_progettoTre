from HttpServer import HttpServer
from Dashboard import DashboardApp
import threading

def start_flask_server():
    # Crea il server Flask e passa l'istanza di DashboardApp
    server = HttpServer(app)
    server.run()

if __name__ == '__main__':
    # Crea l'istanza di DashboardApp
    app = DashboardApp()

    # Avvia il server Flask in un thread separato
    flask_thread = threading.Thread(target=start_flask_server)
    flask_thread.daemon = True
    flask_thread.start()

    # Avvia la GUI Tkinter
    app.mainloop()