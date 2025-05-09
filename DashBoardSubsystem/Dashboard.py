import tkinter as tk
from tkinter import ttk
import random
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

class DashboardApp:

    def __init__(self):
        self.N = 10  # Numero di temperature da visualizzare
        self.temperatures = [random.uniform(18, 25) for _ in range(self.N)]
        self.state = "NORMAL"
        self.window_level = 50
        self.alarm = False
        self.manual = False
        self.dashState = "NORMAL"
        self.avg_temp = sum(self.temperatures) / len(self.temperatures)
        self.max_temp = max(self.temperatures)
        self.min_temp = min(self.temperatures)

        # Creazione finestra principale
        self.root = tk.Tk()
        self.root.title("Temperature Monitoring Dashboard")

        # FRAME: Info e Controlli
        control_frame = ttk.Frame(self.root)
        control_frame.pack(side=tk.TOP, fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.temp_label = ttk.Label(control_frame, text="Temperature Readings:")
        self.temp_label.grid(row=0, column=0, columnspan=2)

        self.temp_display = tk.Text(control_frame, height=2, width=50)
        self.temp_display.grid(row=1, column=0, columnspan=2)
        self.temp_display.insert(tk.END, ', '.join(f"{t:.2f}" for t in self.temperatures))

        self.stats_frame = ttk.LabelFrame(control_frame, text="Statistics")
        self.stats_frame.grid(row=2, column=0, columnspan=2, sticky="ew", pady=5)
        
        self.avg_label = ttk.Label(self.stats_frame, text=f"Average Temp: {self.avg_temp:.2f}°C")
        self.avg_label.grid(row=0, column=0, padx=5, pady=2, sticky="w")
        
        self.max_label = ttk.Label(self.stats_frame, text=f"Max Temp: {self.max_temp:.2f}°C")
        self.max_label.grid(row=0, column=1, padx=5, pady=2, sticky="w")
        
        self.min_label = ttk.Label(self.stats_frame, text=f"Min Temp: {self.min_temp:.2f}°C")
        self.min_label.grid(row=1, column=0, padx=5, pady=2, sticky="w")

        self.state_label = ttk.Label(control_frame, text=f"State: {self.state}")
        self.state_label.grid(row=2, column=0, sticky="w")

        self.window_label = ttk.Label(control_frame, text=f"Window Level: {self.window_level}%")
        self.window_label.grid(row=3, column=0, sticky="w")

        self.window_slider = ttk.Scale(control_frame, from_=0, to=90, orient='horizontal', command=self.update_window_level)
        self.window_slider.set(self.window_level)
        self.window_slider.config(state='disabled')
        self.window_slider.grid(row=3, column=1, sticky="e")

        self.manual_button = ttk.Button(control_frame, text="Manual Mode", command=self.manual_mode)
        self.manual_button.grid(row=4, column=0, sticky="w")

        self.alarm_button = ttk.Button(control_frame, text="Reset Alarm", command=self.reset_alarm)
        self.alarm_button.grid(row=4, column=1, sticky="e")
        self.alarm_button.config(state='disabled')

        self.graph_frame = ttk.Frame(self.root)
        self.graph_frame.pack(side=tk.BOTTOM, fill=tk.BOTH, expand=True, padx=10, pady=10)

        self.fig, self.ax = plt.subplots(figsize=(5, 3))
        self.ax.set_title("Temperature Trend")
        self.ax.set_xlabel("Time")
        self.ax.set_ylabel("Temperature (°C)")
        self.line, = self.ax.plot(self.temperatures, marker="o", linestyle="-", color="b")

        self.canvas = FigureCanvasTkAgg(self.fig, master=self.graph_frame)
        self.canvas.get_tk_widget().pack(fill=tk.BOTH, expand=True)

    def calculate_temperature_stats(self):
        """Calcola le statistiche di temperatura (media, massima, minima)."""
        self.avg_temp = sum(self.temperatures) / len(self.temperatures)
        self.max_temp = max(self.temperatures)
        self.min_temp = min(self.temperatures)
        
        self.avg_label.config(text=f"Average Temp: {self.avg_temp:.2f}°C")
        self.max_label.config(text=f"Max Temp: {self.max_temp:.2f}°C")
        self.min_label.config(text=f"Min Temp: {self.min_temp:.2f}°C")

    def update_temperature(self, temperature):
        """Aggiunge una nuova temperatura e aggiorna GUI e grafico."""
        self.temperatures.append(temperature)
        if len(self.temperatures) > self.N:
            self.temperatures.pop(0)
        self.calculate_temperature_stats()
        self.root.after(0, self.refresh_display)
        self.root.after(0, self.update_plot)

    def update_plot(self):
        """Aggiorna il grafico delle temperature."""
        self.ax.clear()
        self.ax.set_title("Temperature Trend")
        self.ax.set_xlabel("Time")
        self.ax.set_ylabel("Temperature (°C)")
        self.ax.plot(self.temperatures, marker="o", linestyle="-", color="b")
        self.canvas.draw()

    def update_window_level(self, value):
        self.window_level = int(float(value))
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def refresh_display(self):
        """Aggiorna il testo della GUI."""
        self.temp_display.delete('1.0', tk.END)
        self.temp_display.insert(tk.END, ', '.join(f"{t:.2f}" for t in self.temperatures))
        self.state_label.config(text=f"State: {self.state}")
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def update_state(self, status):  # Chiamato dal server
        if not self.alarm:
            self.state = status
            if status == "ALARM":
                self.alarm_button.config(state='normal')
                self.alarm = True

    def update_level(self, level):  # Chiamato dal server
        if not self.manual:
            self.window_level = level

    def update_data(self):  # Chiamato dal server
        self.avg_temp = sum(self.temperatures) / len(self.temperatures)
        self.max_temp = max(self.temperatures)
        self.min_temp = min(self.temperatures)

    def getState(self):
        return self.dashState

    def get_window_level(self):
        return self.window_level

    def manual_mode(self):
        self.manual = not self.manual
        if self.manual:
            self.window_slider.config(state='normal')
            self.manual_button.config(text="Automatic Mode")
            self.dashState = "MANUAL"
        else:
            self.window_slider.config(state='disabled')
            self.manual_button.config(text="Manual Mode")
            self.dashState = "NORMAL"
        self.window_label.config(text=f"Window Level: {self.window_level}%")

    def reset_alarm(self):
        self.alarm = False
        self.dashState = "UNDEFINED"
        self.state_label.config(text=f"State: NORMAL")
        self.alarm_button.config(state='disabled')

    def mainloop(self):
        self.root.mainloop()