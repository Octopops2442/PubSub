import java.io.Serializable

trait Event(message:String,  val isSubscriber: Boolean)  {    
    // def setMessage(m:String) = {
    //     message = m
    // }


    def getMessage():String = {
        return message
    }

}