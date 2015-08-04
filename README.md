# marathon-client

Very early work on a [Marathon](https://mesosphere.github.io/marathon/) API client using [spray.io](http://spray.io/).

###Dependencies

* spray.io
* akka-actor, (with `provided` scope, ie. you need to pull it yourself.)

###Examples

##### Get the list of running tasks.

```
implicit val timeout: Timeout = 5.seconds
implicit val system = ActorSystem("simple-example")
import system.dispatcher

val auth = new BasicHttpCredentials("username", "password")
val baseUri = Uri("www.domain.com") withPort 8080
val marathon = new Marathon(baseUri, auth)

try {
  println(s"${Await.result(marathon.tasks.list(), Duration.Inf).tasks.mkString("\n")}")
} catch {
  case e: Exception => e.printStackTrace()
} finally {
  system.shutdown()
}
```

###License

Released under Apache 2.0 License.
