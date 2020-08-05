import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http
import akka.http.scaladsl.settings.ConnectionPoolSettings
import io.circe.generic.auto._
import io.circe.syntax._
import io.circe.parser._

import scala.util.{Failure, Success}
import scala.concurrent.duration._

object Main extends App {

    implicit val system = ActorSystem()
    implicit val materializer = ActorMaterializer()
    import system.dispatcher


    final case class Rating(userid: Int, movieid: Int, rating: Double)


    val reqMovie = MovieWriter.chooseMovie
    val json = Rating(0,reqMovie,4.5).asJson

    val request = HttpRequest(
      method = HttpMethods.POST,
      uri = "http://recommender:9000/global",
      entity = HttpEntity(
        ContentTypes.`application/json`,
        json.noSpaces
      )
    )

    final case class Recommendations(userid: Int, recommendations: Seq[Int])
    val responseFuture = Http().singleRequest(request, settings = ConnectionPoolSettings(system).withIdleTimeout(Duration.Inf))
    responseFuture
      .onComplete {
        case Success(res) => res.entity.toStrict(300.millis).map(_.data.utf8String).map(x => parse(x) match {
          case Left(failure) => println(s"something went wrong while parsing: $failure")
          case Right(json) => {
            json.as[Recommendations].map(_.recommendations).map(Message.sendMessage(reqMovie, _))
            MovieWriter.writeMovieId(reqMovie)
          }
        })
        case Failure(_)   => println("something went wrong")
      }

}