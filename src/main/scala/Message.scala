import com.danielasfregola.twitter4s.TwitterRestClient

object Message {
  def sendMessage(movieid: Int, recomendations: Seq[Int]) = {
    val movieNames = new DB().getMovies()

    val cleanRecommendations = recomendations
      .filter( _ != movieid)
      .take(3).map(x => movieNames(x))
      .map(x => s"${x.title} http://www.imdb.com/title/tt${x.link}")
      .mkString("\n")

    val movieRequest = movieNames(movieid)

    val client = TwitterRestClient()
    client.createTweet(status = s"\n\n$cleanRecommendations \n\nIf you liked \nhttp://www.imdb.com/title/tt${movieRequest.link}")
  }

}
