package view.GUI.Pages

import java.awt.Color

import control.Controller
import javax.naming.spi.DirectoryManager
import view.GUI.InfoBar

import scala.swing.event.ButtonClicked
import scala.swing.{Button, Dimension, FlowPanel, TextField}

class SecondPage(controller: Controller, infobar: InfoBar) extends FlowPanel {

  val kartenNameTf = new TextField("Kartenname") {
    preferredSize = new Dimension(200, 50)
  }
  val addKarteBtn = new Button("Add") {
    preferredSize = new Dimension(200, 50)
  }

  val weiterBtn = new Button("Weiter") {
    preferredSize = new Dimension(100, 100)
  }

  this.contents += kartenNameTf
  this.contents += addKarteBtn
  this.contents += weiterBtn

  preferredSize = new Dimension(790, 500)

  listenTo(addKarteBtn)
  listenTo(weiterBtn)

  reactions += {
    case ButtonClicked(b) if b==addKarteBtn => {
      if(kartenNameTf.text.equals("") || kartenNameTf.text.equals("Kartenname"))
        infobar.text = "Bitte gib zuerst eine Karte ein."
      else {
        controller.eval(kartenNameTf.text)
        kartenNameTf.text = ""
      }
    }
    case ButtonClicked(b) if b==weiterBtn=> {
      controller.eval("weiter")
    }
  }
}
