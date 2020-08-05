import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats.effect._

object DB {

  def connection() = {

    implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

    val xa = Transactor.fromDriverManager[IO](
      "org.postgresql.Driver",
      "jdbc:postgresql://postgres:5432/movielens",
      "postgres",
      "password",
      Blocker.liftExecutionContext(ExecutionContexts.synchronous)
    )
    xa
  }

}

class DB {

  def getMovies(): Map[Int, Movie] = {
    sql"select movies.movieid, title, imdbid from movies join links on movies.movieid=links.movieid"
      .query[(Int, (String, String))]
      .to[List]
      .transact(DB.connection)
      .unsafeRunSync
      .map(x => (x._1, Movie(x._2._1, x._2._2)))
      .toMap
  }

  def getMoviesIds(): Seq[Int] = {
    sql"select movieid from movies"
      .query[Int]
      .to[List]
      .transact(DB.connection)
      .unsafeRunSync
  }

}
