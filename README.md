# CMSC137Project
KnockOut Game

## Prerequisite(s)
- **Java**

## Command(s)

### Set up the repository
1. **Clone the repository**
    ```
	  $ git clone https://github.com/pvtan/CMSC137Project.git
	```
### Compile and run the application
1. **Compile**
    ```
    $ makefile
    ```
2. **Run**
- ***For server:***
    ```
	  $ java -jar Knock-Out-v.2.0.jar 0 tcp_port upd_port number_of_clients
    ```
- ***For client:***
    ```
	  $ java -jar Knock-Out-v.2.0.jar 1 server_ip_address tcp_port udp_port
    ```
