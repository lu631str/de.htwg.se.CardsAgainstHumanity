
import com.google.inject.AbstractModule
import control.ControllerInterface
import model.BaseImpl.{CardStack, GameManager, KompositumCard, RoundStrategy}
import model.ModelInterface
import model.fileIoComponent.FileIOInterface
import model.fileIoComponent.fileIoJsonImpl.FileIO
import net.codingwell.scalaguice.ScalaModule

class CardsAgainstHumanityModule extends AbstractModule with ScalaModule {

  override def configure() = {
    bind[ModelInterface].toInstance(GameManager())
    bind[FileIOInterface].to[FileIO]
  }

}
