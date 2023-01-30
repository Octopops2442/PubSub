// import java.io.Serializable
@SerialVersionUID(100L)
class MessageEvent(val message: String, val publisherName: String) extends Serializable with Event(message,false){
    // override def speak() = println("Woof")
    // def comeToMaster() = println("Here I come!")

}