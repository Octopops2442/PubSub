// import java.io.{Serializable}
@SerialVersionUID(100L)
class SubscribeEvent(val message: String,val subscriber: Subscriber) extends Serializable with Event(message,true) {
    // override def speak() = println("Woof")
    // def comeToMaster() = println("Here I come!")

}