import java.net._
import java.io._
import scala.collection.mutable._

class Middleware extends Runnable {

    val eventQueue = new Queue[Event]()
    var serverSocket = new ServerSocket(6969)
    var publisherMap: Map[String, ArrayBuffer[Int]] =  Map()
    // val eventMap: Map[Int, Array[String]] = Map.empty[Int, Array[String]]
    // int is the subscriber socket number

    override def run() = {
        // create socket to listen to Messages
        try {
            while(true){
                // wait for notify
                getMessage()
                // var event = eventQueue.dequeue
                // switch cases based on what event it recieves in notify 
                // if(event isInstanceof[PublishEvent] ){}
                // else if(event isInstanceof[MessageEvent] ){}
                // else if(event isInstanceof[SubscribeEvent] ){}
                // call that message in this class
            }
        } catch {
            case e: Exception => println(e)
        }
    }

    def getMessage() = {
        try {
            // listen to the port
            println("Waiting for Publisher/Subscriber...")
            
            var socket = serverSocket.accept()
            println("socket accepting")
            // add to the queue
            var is = socket.getInputStream()
            var ois = new ObjectInputStream(is)
            var obj1=ois.readObject().asInstanceOf[Event]
            
            println("Values received from Client are:-");
            println(obj1.getMessage());
            if(obj1.isSubscriber){
                println("got subscribeEvent ")
                var obj2 = obj1.asInstanceOf[SubscribeEvent]
                println("It is subscriber : " + obj2)
                println("socket:"+ obj2.subscriber.portNumber )
                var publishers = obj2.message.split(" ")
                var subscriberPortNumber = obj2.subscriber.portNumber
                for( publisher <- publishers){
                    println("PUBLISHER NAME :"+publisher+":")
                    if(publisherMap.contains(publisher)){
                        println("publisher is there in map")
                        println("publisher map array size before"+ publisherMap(publisher).size)
                        publisherMap(publisher) += subscriberPortNumber
                        println("publisher map array size after"+ publisherMap(publisher).size)
                    }
                    else {
                        println("publisher is not there in map")
                        publisherMap(publisher) = new ArrayBuffer[Int]()
                        println("publisher map array size before"+ publisherMap(publisher).size)
                        publisherMap(publisher) += subscriberPortNumber
                        println("publisher map array size after"+ publisherMap(publisher).size)
                    }
                    println("size of publisher map"+ publisherMap(publisher).size)
                }
            }
            else {
                println(" object recieved is instance of PublishEvent")
                var obj2 = obj1.asInstanceOf[PublishEvent]
                var message = obj2.message 
                var publisherName = obj2.publisher.name
                if(message == "register"){
                    if(!publisherMap.contains(publisherName)){
                        publisherMap += (publisherName ->  new ArrayBuffer[Int]())
                        println(" publisher added? "+ publisherMap.contains(publisherName))
                        println(" publisher map array size "+ publisherMap(publisherName).size)
                    }
                }
                else {
                    println("array Buffer size = "+ publisherMap(publisherName).size)
                    println("array Buffer = "+ publisherMap(publisherName))
                    println("publisher name :"+ publisherName +":")
                    println("map keys"+ publisherMap.keys)
                    println("map values "+ publisherMap.values)
                    for( port <- publisherMap(publisherName)){
                        println("port "+ port)
                        var clientSocket = new Socket("localhost",port)
                        println("came here1")
                        var messageEvent:Event = new MessageEvent(message,publisherName)
                        println("came here2")
                        var os = clientSocket.getOutputStream()
                        println("came here3")
                        var oos = new ObjectOutputStream(os)
                        println("came here4")

                        println("Sending values to the Subscriber socket..")
                        println("Sending : "+ message)

                        oos.writeObject(messageEvent)

                        clientSocket.close()
                    }
                }
            }
            

            println("Closing sockets.");
            socket.close();

            // eventQueue += message
        } catch {
            case e: Exception => println(e)
        }
    }

    // def registerSubscriber(subscriber: Subscriber) = {

    // }

    // def registerPublisher(publisher: Publisher) = {

    // }
}

object MainMiddleware {
    def main(args:Array[String]) = {
        // val middleName:String = scala.io.StdIn.readLine("Enter name: ")
        var executable = new Middleware()
        var middlewareThread = new Thread(executable)

        middlewareThread.start()
    }
}