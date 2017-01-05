import jsonb.Books


//"E" :: Nil :: "D" :: List("A", "B", "C")

Seq("A", "B", null, "C")
  .filter(_ != null)







val s = List(1, 2, 3)

4 :: s ::: List(5)



List(7, 8) ::: List(9, 10)



Seq(1, 2, 3)
.flatMap(i => Seq("A" + i, "B" + i))
