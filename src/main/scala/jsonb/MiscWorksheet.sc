
val seq: Option[List[String]] = ".* (\\d+):(\\d+)(.*[–-\\,])(\\d+)".r.unapplySeq("aaa 1:23-45")

seq.nonEmpty


//".* (\\d+):(\\d+)(.*[–-,])(\\d+)".r.unapplySeq("aaa 1:23-45")







