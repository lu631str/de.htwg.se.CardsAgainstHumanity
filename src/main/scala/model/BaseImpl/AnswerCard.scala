package model.BaseImpl

import model.BaseImpl.Card

case class AnswerCard(antwort:String) extends Card {

  override def printCard = {println("Meine Antwort ist: " + antwort)}
  override def toString: String = antwort
  override def addNewCard(card: Card): KompositumCard = {null}
  override def removeCard(card: Card): KompositumCard = {null}
}