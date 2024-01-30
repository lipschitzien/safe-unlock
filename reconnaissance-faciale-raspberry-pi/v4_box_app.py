import os
import cv2
import numpy as np
import csv
from sklearn.decomposition import PCA
from sklearn.model_selection import train_test_split
from sklearn.pipeline import make_pipeline
from sklearn.svm import SVC
from sklearn.preprocessing import StandardScaler
from sklearn.svm import OneClassSVM
from sklearn import model_selection
from sklearn import metrics
from sklearn import svm
from sklearn.model_selection import GridSearchCV, ShuffleSplit
import subprocess
import datetime
from datetime import datetime
import tkinter as tk
from tkinter import ttk
from PIL import Image, ImageTk

import RPi.GPIO as GPIO
import time
#from socket import *


#3 classes

class DetectionApp:
	def __init__(self, master):
		self.master = master
		# algo pour s entrainer le modele
		self.initialiser_reconnaissance_faciale()
		self.master.title("Reconnaissance faciale")
		
		self.cap = None
		self.freeze_frame = False
		#self.id = 0 
		self.csv_file_path = 'recognition_results2.csv'
		"""
		try:
			with open(self.csv_file_path, "r") as file:
				reader = csv.reader(file)
				for row in reader:
					if row:
						return int(row[0])
				last_id = 0
		except FileNotFoundError:
			last_id = 0
		"""
		#last_id = get_last_id(self.csv_file_path)
		self.id = 0
		
		# Set the style for ttk buttons to have a white background
		style = ttk.Style()
		style.configure("TButton", background="white", borderwidth=1)
		
		# Set the background color of the root window to white
		self.master.configure(bg="white")
		
		# Create a frame to hold the buttons with a white background
		self.button_frame = tk.Frame(self.master, bg="white")
		self.button_frame.pack()
		
		self.create_widgets()
		# Initialize recognition_result with a default value
		self.recognition_result = "No faces detected"
		# Open CSV file in write mode
		#self.csv_file_path = 'recognition_results2.csv'
		with open(self.csv_file_path, mode='a', newline='') as file:
			writer = csv.writer(file)
			# If the file is empty, write the header
			if file.tell() == 0:
				writer.writerow(['id', 'personne', 'porte_status', 'current_time'])
				file.write(";\n")

	
	def initialiser_reconnaissance_faciale(self):
		X_loaded = np.loadtxt('new_images_flat.txt', dtype=np.uint8)
		y_loaded = np.loadtxt('new_labels_3classes.txt', dtype=np.uint8)
		
		scaler = StandardScaler()
		X_scaled = scaler.fit_transform(X_loaded)
		
		pca = PCA(n_components=73)
		X_pca = pca.fit_transform(X_scaled)
		
		#best_svm = SVC(C=5, kernel='rbf', gamma=1e-05)
		#best_svm = SVC(C=0.1, kernel='linear')
		best_svm = SVC(C=100, kernel='rbf', gamma=1e-05)
		self.pipeline = make_pipeline(scaler, pca, best_svm)
		
		self.pipeline.fit(X_loaded,y_loaded)
		
		# Configuration des broches GPIO
		GPIO.setmode(GPIO.BCM)
		GPIO.setup(21, GPIO.OUT)
		
	def create_widgets(self):
		self.btn_open_camera = ttk.Button(self.button_frame, text="Ouvrir la caméra", command=self.open_camera)
		self.btn_open_camera.pack(pady=10)
		
		self.btn_capture = ttk.Button(self.button_frame, text="Lancer la reconnaissance", command=self.capture_image)
		self.btn_capture.pack(pady=10)
		
		self.btn_exit = ttk.Button(self.button_frame, text="Quitter", command=self.exit_app)
		self.btn_exit.pack(pady=10)
		
		# Set the background color of the Canvas to white
		self.canvas = tk.Canvas(self.master, width=640, height=480, bg="white")
		self.canvas.pack()
	
	def open_camera(self):
		self.cap = cv2.VideoCapture(0)
		self.show_camera()
	
	def show_camera(self):
		if self.cap is not None:
			ret, frame = self.cap.read()
			
			#detecter visage
			faces = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml').detectMultiScale(
#			faces = cv2.CascadeClassifier("haarcascade_frontalface_default.xml").detectMultiScale(
				frame, scaleFactor=1.1, minNeighbors=5, minSize=(30, 30)
			)
			self.faces = faces
			
			if len(faces) > 0:
				for (x, y, w, h) in faces:
					cv2.rectangle(frame, (x, y), (x + w, y + h), (0, 255, 0), 2)
			
			frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
			photo = ImageTk.PhotoImage(image=Image.fromarray(frame))
			
			self.canvas.create_image(0, 0, anchor=tk.NW, image=photo)
			self.canvas.photo = photo
			self.master.after(10, self.show_camera)
			
			
			"""
			
			if self.freeze_frame:
				frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
				photo = ImageTk.PhotoImage(image=Image.fromarray(frame))
			else:
				frame = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)
				photo = ImageTk.PhotoImage(image=Image.fromarray(frame))
			
		self.canvas.create_image(0, 0, anchor=tk.NW, image=photo)
		self.canvas.photo = photo
		self.master.after(10, self.show_camera)
		"""
		

	def capture_image(self):
		if self.cap is not None:
			ret, frame = self.cap.read()
		if self.cap is not None:
			ret, frame = self.cap.read()
			
			if ret:
				base_name = "temp"
				extension = ".jpg"
				count = 1

				while True:
					nom_fichier = f"{base_name}{count}{extension}"
					
					if not os.path.exists(nom_fichier):
						break
					
					count += 1
				
				
				cv2.imwrite(nom_fichier, frame)
				#print(f"Photo : {nom_fichier}")
				#id = 0
				porte_status = "fermer"
				
				# After capturing the image and running face recognition
				if self.run_face_recognition(nom_fichier):# Append the image name and recognition result to the CSV file
					with open(self.csv_file_path, mode='a', newline='') as file:
						writer = csv.writer(file)
						current_time = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
						personne = self.recognition_result
						if personne == "class_2":
							porte_status = "Open"
						elif personne == "class_1":
							porte_status = "open"
						else:
							porte_status = "NotOpen"
						
						# Increment the ID counter
						self.id += 1
						
						writer.writerow([self.id, personne, porte_status, current_time])
						file.write(";\n")
					#file.close()
					
					#with open(self.csv_file_path, mode='a', newline=''):
						#pass

            
	def run_face_recognition(self, nom_fichier):
		nouvelle_image = cv2.imread(nom_fichier)
		#print("1")
		nouvelle_image_gris = cv2.cvtColor(nouvelle_image, cv2.COLOR_BGR2GRAY)
		face_cascade = cv2.CascadeClassifier(cv2.data.haarcascades + 'haarcascade_frontalface_default.xml')
		
		#print("2")
		face_resized = None
		
		try:
			faces = face_cascade.detectMultiScale(nouvelle_image_gris, scaleFactor=1.1, minNeighbors=3, minSize=(30, 30))
			
			if len(faces) > 0:
				largest_face = max(faces, key=lambda face: face[2] * face[3])
				x, y, w, h = largest_face
				face_resized = cv2.resize(nouvelle_image_gris[y:y+h, x:x+w], (92, 112))
				#print(self.recognition_result)
				#return True
			else:
				raise Exception("No faces detected! Not saving image or updating CSV.")
				
		
		except Exception as e:
			if type(e) is not NameError:  # Check if the exception is not a NameError
				print(f"Exception: {e}")
				print("Relancer la caméra")
				
			else:
				self.recognition_result = None
				return False
			
			# If an exception occurs, set face_resized to None to avoid AttributeError
			face_resized = None
		
		if face_resized is not None:
			#print("3")
			visage_aplati = face_resized.flatten()
			visage_aplati_2d = visage_aplati.reshape(1, -1)
			
			result_pipeline = self.pipeline.predict(visage_aplati_2d)
			
			if result_pipeline == 0:
				#self.canvas.create_text(50, 50, text="Meiling", fill="green", font=("Helvetica", 16))
				#print("inconnu_0")
				self.recognition_result = "inconnu"
				#print("XXXXXX-- serrure verouillée --XXXXXX")
			elif result_pipeline == 2:
				#self.canvas.create_text(50, 50, text="M'hamed", fill="green", font=("Helvetica", 16))
				#print("class_2")
				self.recognition_result = "class_2"
				#print("------serrure ouverte------")
				GPIO.output(21, GPIO.HIGH)
				time.sleep(5)
				GPIO.output(21, GPIO.LOW)
				
			elif result_pipeline == 1:			
				#self.canvas.create_text(50, 50, text="Inconnu_SVM", fill="red", font=("Helvetica", 16))
				#print("inconnu_1")
				self.recognition_result = "class_1"
				#print("XXXXXX-- serrure verouillée --XXXXXX")
				GPIO.output(21, GPIO.HIGH)
				time.sleep(5)
				GPIO.output(21, GPIO.LOW)
			else:
				self.recognition_result = "Unknown"
			
			print(self.recognition_result)
			return True

	def exit_app(self):
		if self.cap is not None:
			self.cap.release()
			self.master.destroy()
			
			
def main():
	root = tk.Tk()
	app = DetectionApp(root)
	root.mainloop()

if __name__ == "__main__":
	main()
	

	
"""
			elif result_pipeline == 1:
				self.canvas.create_text(50, 50, text="Meiling", fill="green", font=("Helvetica", 16))
				print("Meiling")
				print("------serrure ouverte------")
"""
