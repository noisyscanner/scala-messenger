import java.net.{Socket, UnknownHostException}

class Client(var host: String = "localhost", var port: Int = 8080) {

  var socket: Socket = _
  var connected = false

  def connect(callback: (Postman) => Unit): Boolean = {
    try {
      socket = new Socket(host, port)
      connected = true
      println("Connected to socket on " + host + ":" + port)

      val postman = new Postman(socket)
      callback(postman)
      connected
    } catch {
      case _: UnknownHostException => println("Unknown host"); false
      case e: Exception => println("Some error connecting to the socket: " + e.getMessage); false
    }
  }

  def disconnect(): Unit = {
    socket.close()
    println("Socket closed.")
  }

}
