# -*- coding: utf-8 -*-
import RPi.GPIO as GPIO
from socket import *
import csv
import os
import time

# Configuration des broches GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(21, GPIO.OUT) # Assurez-vous que la broche GPIO 21 est configurée en tant que sortie

ctrCmd = ['Open', 'Close', 'lire']

HOST = ''
PORT = 21567
BUFSIZE = 2048
ADDR = (HOST, PORT)

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

def send_csv_data(client_socket):
    script_dir = os.path.dirname(os.path.abspath(__file__))
    os.chdir(script_dir)
    csv_file_path = os.path.join(script_dir, 'recognition_results2.csv')

    try:  
        # Ouvrir le fichier CSV et lire les données
        with open(csv_file_path, newline='', encoding='utf-8') as csvfile:
            csv_reader = csv.reader(csvfile)

            # Envoyer chaque ligne du fichier CSV au client
            for row in csv_reader:
                data_to_send = ','.join(row)  # Convertir la liste en chaîne avec des virgules
                client_socket.send(data_to_send.encode("utf-8"))

        # Envoyer un signal de fin de transmission
        client_socket.send("FIN".encode("utf-8"))
        csvfile.flush()
        os.fsync(csvfile.fileno())

    except Exception as e:  # <- ???????????????
        print(f"Error sending CSV data: {e}")

try:
    while True:
        print('-attente connection')
        tcpCliSock, addr = tcpSerSock.accept()
        print('...connected from:', addr)
        print('valeur tableau Cmd:')
        print(ctrCmd)

        try:
            data = ''
            while 'Open' not in data and 'Close' not in data and 'lire' not in data:
                received_data = tcpCliSock.recv(BUFSIZE).decode("utf-8").strip()
                if not received_data:
                    print('no data')
                    break
                data += received_data
                print('Received data:', data)

            if 'Open' in data:
                print('Ouvert')
                GPIO.output(21, GPIO.HIGH)
                time.sleep(5)
                GPIO.output(21, GPIO.LOW)
            elif 'Close' in data:
                print('Ferme')
                GPIO.output(21, GPIO.LOW)  # Désactivez la gâchette de la serrure (adaptation nécessaire selon le fonctionnement de votre gâchette)
            elif 'lire' in data:
                print('Received lire command')
                send_csv_data(tcpCliSock)  # Appel de la fonction pour envoyer les données CSV

        except KeyboardInterrupt:
            print('Except')
            GPIO.cleanup()

        tcpCliSock.close()

finally:
    tcpSerSock.close()

"""
# -*- coding: utf-8 -*-
import RPi.GPIO as GPIO
from socket import *
import csv
import os
import time

# Configuration des broches GPIO
GPIO.setmode(GPIO.BCM)
GPIO.setup(21, GPIO.OUT) # Assurez-vous que la broche GPIO 21 est configurée en tant que sortie

ctrCmd = ['Open', 'Close', 'lire']

HOST = ''
PORT = 21567
BUFSIZE = 2048
ADDR = (HOST, PORT)

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

def send_csv_data(client_socket):
  script_dir = os.path.dirname(os.path.abspath(__file__))
  os.chdir(script_dir)
  csv_file_path = os.path.join(script_dir, 'recognition_results2.csv')


  with open(csv_file_path, newline='', encoding='utf-8') as csvfile:
    csv_reader = csv.reader(csvfile)

    # Envoyer chaque ligne du fichier CSV au client
    for row in csv_reader:
      data_to_send = ','.join(row) # Convertir la liste en chaîne avec des virgules
      client_socket.send(data_to_send.encode("utf-8"))

  # Envoyer un signal de fin de transmission
  client_socket.send("FIN".encode("utf-8"))

try:
  while True:
    print('-attente connection')
    tcpCliSock, addr = tcpSerSock.accept()
    print('...connected from:', addr)
    print('valeur tableau Cmd:')
    print(ctrCmd)

    try:
      data = ''
      while 'Open' not in data and 'Close' not in data and 'lire' not in data:
        received_data = tcpCliSock.recv(BUFSIZE).decode("utf-8").strip()
        if not received_data:
          print('no data')
          break
        data += received_data
        print('Received data:', data)

      if 'Open' in data:
        print('Ouvert')
        GPIO.output(21, GPIO.HIGH)
        time.sleep(5)
        GPIO.output(21, GPIO.LOW)
      elif 'Close' in data:
        print('Ferme')
        GPIO.output(21, GPIO.LOW) # Désactivez la gâchette de la serrure (adaptation nécessaire selon le fonctionnement de votre gâchette)
      elif 'lire' in data:
        print('Received lire command')
        send_csv_data(tcpCliSock) # Appel de la fonction pour envoyer les données CSV

    except KeyboardInterrupt:
      print('Except')
      GPIO.cleanup()

    tcpCliSock.close()

finally:
  tcpSerSock.close()
"""
