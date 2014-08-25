package monocle.std

import monocle.TestUtil._
import monocle.function._
import monocle.std.set._
import monocle.{LensLaws, PrismLaws}
import org.specs2.scalaz.Spec


class SetSpec extends Spec {

  checkAll("at Set", LensLaws(at[Set[Int], Int, Unit](2)))

  checkAll("empty Set", PrismLaws(empty[Set[Int]]))

}
