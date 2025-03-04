import tkinter as tk
from tkinter import ttk
import random
import threading
import time

class DashboardApp:
    def __init__(self):
        self.temperatures = []
        self.root = tk.Tk()
        self.root.title("Temperature Monitoring Dashboard")

        # Temperature Data
        self.temperatures = [random.uniform(18, 25) for _ in range(10)]
        self.state = "NORMAL"
        self.window_level = 50
        self.alarm = False

        # UI Components
        self.temp_label = ttk.Label(self.root, text="Temperature Readings:")
        self.temp_label.pack()

        self.temp_display = tk.Text(self.root, height=5, width=50)
        self.temp_display.pack()

        self.state_label = ttk.Label(self.root, text=f"State: {self.state}")
        self.state_label.pack()

        self.window_label = ttk.Label(self.root, text=f"Window Level: {self.window_level}%")
        self.window_label.pack()

        self.window_slider = ttk.Scale(self.root, from_=0, to=100, orient='horizontal', command=self.update_window_level)
        self.window_slider.set(self.window_level)
        self.window_slider.pack()

        self.manual_button = ttk.Button(self.root, text="Set Window Level", command=self.set_window_level)
        self.manual_button.pack()

        self.alarm_button = ttk.Button(self.root, text="Reset Alarm", command=self.reset_alarm)
        self.alarm_button.pack()

    def update_temperature(self, temperature):
        # Aggiunge la nuova temperatura e aggiorna la GUI
        self.new_temp = temperature
        self.temperatures.append(self.new_temp)
        if len(self.temperatures) > 10:
            self.temperatures.pop(0)
        self.check_state()
        self.root.after(0, self.refresh_display)  # Usa after per aggiornare la GUI nel thread principale

    def check_state(self):
        self.avg_temp = sum(self.temperatures) / len(self.temperatures)
        self.max_temp = max(self.temperatures)
        self.min_temp = min(self.temperatures)

        if self.new_temp > 28:
            self.state = "TOO-HOT"
            self.alarm = True
        elif self.avg_temp > 25:
            self.state = "HOT"
        else:
            self.state = "NORMAL"

    def alarmState(self):
        # Restituisce lo stato dell'allarme
        return self.alarm

    def refresh_display(self):
        # Aggiorna l'interfaccia grafica con la nuova temperatura
        self.temp_display.delete('1.0', tk.END)
        self.temp_display.insert(tk.END, ', '.join(f"{t:.2f}" for t in self.temperatures))
        self.state_label.config(text=f"State: {self.state}")
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def update_window_level(self, value):
        self.window_level = int(float(value))
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def set_window_level(self):
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def reset_alarm(self):
        self.alarm = False
        self.state = "NORMAL"
        self.state_label.config(text=f"State: {self.state}")