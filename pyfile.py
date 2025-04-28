import socket
import time
import random
import argparse

# Assuming your team provides this function to get camera data
# This function will return a tuple: (crossing_number, lane_number, vehicle_count)
def get_camera_data(crossing):
    # Simulating data for the camera (you should replace this with your actual camera function)
    lane = random.randint(1, 2)      # Lane number (let's assume two lanes per crossing)
    count = random.randint(0, 10)    # Simulated vehicle count
    return crossing, lane, count

# Set up the socket connection
def send_camera_data_to_java(crossing):
    server_address = ('localhost', 7777)  # Java server is listening on port 7777
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    while True:
        try:
            print(f"Connecting to {server_address[0]} on port {server_address[1]} (Crossing: {crossing})")
            sock.connect(server_address)

            # Infinite loop to simulate continuous camera data updates
            while True:
                # Get camera data for the provided crossing number
                crossing_data = get_camera_data(crossing)
                
                # Prepare the message format that Java expects: "crossing:1,lane:2,count:5"
                message = f"crossing:{crossing_data[0]},lane:{crossing_data[1]},count:{crossing_data[2]}\n"
                print(f"Sending: {message.strip()} (Crossing: {crossing})")
                
                # Send the message to Java server
                sock.sendall(message.encode('utf-8'))
                
                # Wait for a brief period before sending new data (simulating continuous stream of data)
                time.sleep(5)  # Send data every 5 seconds (adjust as necessary)

        except (ConnectionRefusedError, ConnectionResetError) as e:
            print(f"Connection failed (Crossing: {crossing}). Retrying in 5 seconds...")
            time.sleep(5)  # Wait before retrying
            sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)  # Recreate the socket object

        except Exception as e:
            print(f"An error occurred (Crossing: {crossing}): {e}")
            break  # Exit if an unexpected error occurs

        finally:
            sock.close()
            print(f"Connection closed (Crossing: {crossing}).")

# Command-line argument parsing
def parse_arguments():
    parser = argparse.ArgumentParser(description="Simulate a camera client that sends data to the Java server.")
    parser.add_argument('crossing', type=int, help="The crossing number (e.g., 1, 2, 3, 4) for this camera client.")
    return parser.parse_args()

if __name__ == "__main__":
    # Parse the command-line arguments
    args = parse_arguments()
    
    # Start sending camera data to the Java server for the given crossing
    send_camera_data_to_java(args.crossing)
