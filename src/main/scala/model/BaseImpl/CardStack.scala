package model.BaseImpl

object CardStack {
  val initialize: KompositumCard = {
    val standardQ = List[String]("Meine Oma mag _", "Ich bin schockiert wenn ich _ sehe","Ich nutze Seife nur um _","Die Demokratie ist _"
      ,"Studium bringt mir _","Russland macht alles _","Ich bin so weil _","Dieses Spiel ist _","Mario Barth kann _","Angela Merkel will _")
    val standardA = List[String]("Kartoffeln", "Schwarze Einhörner","Hitler töten","Menschen zu töten","Baum","unheimlich sinnlos","garnichts"
      ,"super","Bombenanschläge","Wasser zu verschwenden","hugo","fawe","adsads"
      ,"adfs","gfdagads","fasfsa","fsafsa","fasfasfas","afsfsaafs", "bla", "hugo", "xyxfsd", "asdasd", "asdasasdd", "asda4534534534sd", "as234234324dasd",
      "asdsvgfgjhhkfgfdfasd", "asdas234534673476265d", "a2352356sdasd", "aasfdhgsdfassdasd", "asda23267353265sd", "asdas236426742362d", "asdHUUUUGOasd", "asda23234232sd")

    val userCards = List[Card](AnswerCard("Ich bin der aller beste"))
    var kompositumCard = KompositumCard(userCards)
    for(x <- standardQ)
      kompositumCard = kompositumCard.addNewCard(QuestionCard(x))
    for(x <- standardA)
      kompositumCard = kompositumCard.addNewCard(AnswerCard(x))
    kompositumCard = kompositumCard.addNewCard(AnswerCard("i bin so geil"))
    kompositumCard
  }

}