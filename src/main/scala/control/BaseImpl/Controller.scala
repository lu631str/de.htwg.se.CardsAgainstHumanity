package control.BaseImpl

import control._
import model.BaseImpl.GameManager
import utils.UndoManager

import scala.swing.Publisher

class Controller(var gameManager: GameManager) extends Publisher {

  var state: ControllerState = PreSetupState(this)
  val undoManager = new UndoManager

  def nextState(): Unit = state = state.nextState

  def changePage(page: Int): Unit = {
    page match{
      case 1 => publish(new StartPageEvent)
      case 2 => publish(new SecondPageEvent)
      case 3 => publish(new ThirdPageEvent)
    }
  }
  /*
  def updateTui(): Unit = {
    publish(new UpdateTuiEvent)
  }
  */

  def eval(input: String): Unit = {
    state.evaluate(input)
  }

  def stateAsString(): String = {
    state match {
      case _: PreSetupState => "PreSetupGame"
      case _: SetupState => "SetupGame"
      case _: AnswerState => "AnswerState"
      case _: FinishState => "FinishState"
    }
  }

  def getCurrentStateAsString(): String = state.getCurrentStateAsString


  def undo: Unit = {
    undoManager.undoStep
    publish(new UndoEvent)
  }

  def redo: Unit = {
    undoManager.redoStep
    publish(new UndoEvent)
  }
}

trait ControllerState {

  def evaluate(input: String): Unit

  def getCurrentStateAsString: String

  def nextState: ControllerState
}

case class PreSetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {
    if (input.toInt > 4 || input.toInt < 2) getCurrentStateAsString
    else {
      controller.gameManager = controller.gameManager.setPlayersAndRounds(input.toInt)
      controller.changePage(2)
      controller.publish(new UpdateGuiEvent)
      controller.publish(new UpdateTuiEvent)
      controller.nextState()
    }
  }

  override def getCurrentStateAsString: String = "Willkommen bei Cards Against Humanity \n"

  override def nextState: ControllerState = AddCardsQuest(controller)
}

case class AddCardsQuest(controller: Controller) extends ControllerState {
  controller.publish(new UpdateTuiEvent)
  override def evaluate(input: String): Unit = {
    if (input.equals("Weiter") || input.equals("weiter")) {
      controller.nextState()
      controller.publish(new ThirdPageEvent)
    } else {
      controller.undoManager.doStep(new AddCardsCommand(input, this.controller))
      controller.publish(new UpdateInfoBarEvent)
      controller.publish(new UpdateGuiEvent)
    }
  }

  override def getCurrentStateAsString: String = "AddCardState"

  override def nextState: ControllerState = SetupState(controller)
}

case class SetupState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = {

    if (input.isEmpty) return

    controller.undoManager.doStep(new AddPlayersCommand(input, controller))
    controller.publish(new UpdateGuiEvent)
    controller.publish(new UpdateTuiEvent)

    //controller.gameManager = controller.gameManager.addPlayer(input)
    if (controller.gameManager.player.length == controller.gameManager.numberOfPlayers) {
      controller.gameManager = controller.gameManager.createCardDeck()
      controller.gameManager = controller.gameManager.handOutCards()
      controller.nextState()
      controller.publish(new UpdateGuiEvent)
      controller.publish(new NextStateEvent)
    }
  }

  override def getCurrentStateAsString: String = "SetupState"

  override def nextState: ControllerState = AnswerState(controller)
}

case class AnswerState(controller: Controller) extends ControllerState {

  override def evaluate(input: String): Unit = {

    if(input== "" || controller.gameManager.roundAnswerCards.size == controller.gameManager.player.length) {
      controller.gameManager = controller.gameManager.clearRoundAnswers()
      controller.gameManager = controller.gameManager.placeQuestionCard()
      controller.publish(new UpdateInfoBarEvent)
      controller.publish(new UpdateGuiEvent)
      controller.publish(new UpdateTuiEvent)
    } else {
      if (controller.gameManager.roundAnswerCards.size == controller.gameManager.player.size) {
        controller.gameManager = controller.gameManager.drawCard()
        controller.publish(new UpdateGuiEvent)
        controller.publish(new UpdateTuiEvent)
        controller.nextState()
      }
      val activePlayer = controller.gameManager.getActivePlayer()
      if (input.toInt >= 0 && input.toInt < controller.gameManager.player(activePlayer).playerCards.length) {
        controller.gameManager = controller.gameManager.placeCard(activePlayer, controller.gameManager.player(activePlayer).playerCards(input.toInt))
        controller.gameManager = controller.gameManager.pickNextPlayer()
        controller.publish(new UpdateGuiEvent)
        controller.publish(new UpdateTuiEvent)
      }

    }

    if(controller.gameManager.numberOfRounds >= controller.gameManager.numberOfPlayableRounds)
      controller.nextState()
  }

  override def getCurrentStateAsString: String = controller.gameManager.roundQuestion

  override def nextState: ControllerState = {
    if(controller.gameManager.numberOfRounds > controller.gameManager.numberOfPlayableRounds) {
      FinishState(controller)
    } else this
  }
}

case class FinishState(controller: Controller) extends ControllerState {
  override def evaluate(input: String): Unit = ()

  override def getCurrentStateAsString: String = "Please write q to exit the game"

  override def nextState: ControllerState = this
}
