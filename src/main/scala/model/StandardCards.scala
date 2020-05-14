package model

final case class StandardCards() {
  final private val standardQuestions = List("Ich finde _ , toll", "Der Held meiner Oma ist _")
  final private val standardAnswers = List("Hugh Hefner", "Donald Trump")

  def getStandardAnswer: List[String] ={ standardAnswers }
  def getQuestionCards: List[String] = { standardQuestions }
}
