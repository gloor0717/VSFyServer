# VSFyServer User Manual

## How to Launch the Project

1. **Prerequisites:**
   - Ensure Java is installed on your system. 
   - Maven should be installed for building the project.

2. **Building the Project:**
   - Navigate to the project directory in the terminal.
   - Run `mvn clean package` to build the project. This will create a JAR file in the `target` directory.

3. **Running the Server:**
   - After building, navigate to the `target` directory.
   - Run the server using the command `java -jar VSFyServer-1.0-SNAPSHOT.jar`.

## How to Use the Project

The VSFyServer listens for client connections on port 45000. Once the server is running, clients can connect to it and interact using the defined protocols for registering, requesting songs, listing music, etc.

## How the Project Works

- The `VSFyServer` class initializes a server socket and waits for client connections.
- Each client connection is handled by the `ClientHandler` class in a separate thread.
- `ClientHandler` processes commands from clients, such as registering a client, listing music, and handling song requests.
- The `ClientInfo` class stores information about each connected client.

## How to Generate a JAR if Not Generated

If the JAR file is not generated using the Maven build, ensure that you are in the project's root directory and Maven is correctly installed. Run `mvn clean package` again and check for any error messages in the output.

## Access the JavaDoc for More Information

JavaDoc provides detailed documentation on the project's classes and methods.

1. **Generating JavaDoc:**
   - Run `mvn javadoc:javadoc` from the project's root directory.
   - This generates documentation in the `target/site/javadoc` directory.

2. **Viewing JavaDoc:**
   - Open the `index.html` file in the generated JavaDoc directory in a web browser to view the documentation.

## How to Generate the JavaDoc

To generate the JavaDoc:

1. Navigate to the project's root directory.
2. Run the command `mvn javadoc:javadoc`.
3. The JavaDoc will be generated in the `target/site/javadoc` directory.

