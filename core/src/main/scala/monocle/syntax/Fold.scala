package monocle.syntax

import monocle._
import scalaz.Monoid

object fold extends FoldSyntax

private[syntax] trait FoldSyntax {
  implicit def toApplyFoldOps[S](value: S): ApplyFoldOps[S] = new ApplyFoldOps(value)
}

final case class ApplyFoldOps[S](s: S) {
  def applyFold[A](fold: Fold[S, A]): ApplyFold[S, A] = new ApplyFold[S, A](s, fold)
}

case class ApplyFold[S, A](s: S, _fold: Fold[S, A]) {
  def foldMap[B: Monoid](f: A => B): B = _fold.foldMap(s)(f)

  def fold(implicit ev: Monoid[A]): A = _fold.fold(s)

  def getAll: List[A] = _fold.getAll(s)

  def headOption: Option[A] = _fold.headOption(s)

  def exist(p: A => Boolean): Boolean = _fold.exist(s)(p)

  def all(p: A => Boolean): Boolean = _fold.all(s)(p)

  def composeFold[B](other: Fold[A, B]): ApplyFold[S, B] = ApplyFold(s, _fold composeFold other)
  def composeGetter[B](other: Getter[A, B]): ApplyFold[S, B] = ApplyFold(s, _fold composeGetter other)
  def composeTraversal[B, C, D](other: Traversal[A, B, C, D]): ApplyFold[S, C] = ApplyFold(s, _fold composeTraversal other)
  def composeOptional[B, C, D](other: Optional[A, B, C, D]): ApplyFold[S, C] = ApplyFold(s, _fold composeOptional other)
  def composePrism[B, C, D](other: Prism[A, B, C, D]): ApplyFold[S, C] = ApplyFold(s, _fold composePrism other)
  def composeLens[B, C, D](other: Lens[A, B, C, D]): ApplyFold[S, C] = ApplyFold(s, _fold composeLens other)
  def composeIso[B, C, D](other: Iso[A, B, C, D]): ApplyFold[S, C] = ApplyFold(s, _fold composeIso other)
}