import java.net._

class Server(var port: Int = 8080) {

  var serverSocket: ServerSocket = _
  var clientSocket: Socket = _
  var listener: Thread = _

  def connect(): Unit = {
    println("Trying to create server...")
    try {
      // Create new server
      serverSocket = new ServerSocket(port)
      println("Server running on port " + port)
    } catch {
      case _: UnknownHostException => println("Unknown host")
      case e: Exception => println("Some error creating the socket: " + e.getMessage)
    }
  }

  def listen(callback: (Postman) => Unit): Unit = {
    println("Awaiting connection...")
    // Listens in the background
    val listener = new Thread {
      override def run(): Unit = {
        if (!Thread.currentThread().isInterrupted) {
          clientSocket = serverSocket.accept() // This blocks until a client connects
          println("Client connected")

          callback(new Postman(clientSocket))
        }
      }
    }
    listener.start()
  }
}
