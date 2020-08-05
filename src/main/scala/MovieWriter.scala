import java.io.{BufferedWriter, File, FileWriter}
import scala.util.Random
import scala.io.Source

object MovieWriter {

  def chooseMovie() = {
    val moviesIds = new DB().getMoviesIds
    val filename = s"./resources/movieRegistry.txt"
    val alreadyReq = Source.fromFile(filename).getLines.mkString(" ").split(' ').map(_.stripMargin.toInt)
    Random.shuffle(moviesIds.filter(!alreadyReq.contains(_))).head
  }

  def writeMovieId(movieid:Int) = {
    val file = new File(s"./resources/movieRegistry.txt")
    val bw = new BufferedWriter(new FileWriter(file, true))
    bw.write(s" $movieid ")
    bw.close()
  }

}
