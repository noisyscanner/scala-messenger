import java.io.{BufferedReader, InputStreamReader}

object ServerMain {
  def main(args: Array[String]): Unit = {

    // Create server object
    val server = new Server()

    // Get standard input
    val stdIn = new BufferedReader(new InputStreamReader(System.in))

    // Read server name
    print(s"Enter port to listen on [${server.port}]: ")
    val port = stdIn.readLine()
    if (port != "") {
      server.port = port.toInt
    }

    server.connect()
    server.listen((postman) => {
      // This runs an infinite loop waiting to receive a message,
      // and calls the callback for each line
      postman.readLines((line) => println("Them: " + line))

      // Listens for input from the console and passes it to the socket
      postman.listenForInput(stdIn)

      // When the program is quit, disconnect the client
//      Runtime.getRuntime.addShutdownHook(new Thread {
//        override def run(): Unit = postman.socket.close()
//      })
    })

    // When the program is quit, stop the server
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = server.serverSocket.close()
    })

  }
}
