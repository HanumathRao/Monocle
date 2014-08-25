package monocle

import scalaz.Id._
import scalaz._

/**
 * Optional can be seen as a partial Lens - Lens toward an Option - or
 * a 0-1 Traversal. The latter constraint is not enforce at compile time
 * but by OptionalLaws
 */
abstract class Optional[S, T, A, B] { self =>

  def _optional[F[_]: Applicative](s: S, f: A => F[B]): F[T]

  final def getOption(s: S): Option[A] = asFold.headOption(s)

  final def modifyF(f: A => B): S => T = _optional[Id](_, a => id.point(f(a)))
  final def modify(s: S, f: A => B): T = modifyF(f)(s)
  final def modifyOption(s: S, f: A => B): Option[T] = getOption(s).map(a => set(s, f(a)))
  final def modifyOptionF(f: A => B): S => Option[T] = modifyOption(_, f)

  final def set(s: S, newValue: B): T = setF(newValue)(s)
  final def setF(newValue: B): S => T = modifyF(_ => newValue)
  final def setOption(s: S, newValue: B): Option[T] = modifyOption(s, _ => newValue)
  final def setOptionF(newValue: B): S => Option[T] = setOption(_, newValue)

  // Compose
  final def composeFold[C](other: Fold[A, C]): Fold[S, C] = asFold composeFold other
  final def composeSetter[C, D](other: Setter[A, B, C, D]): Setter[S, T, C, D] = asSetter composeSetter other
  final def composeTraversal[C, D](other: Traversal[A, B, C, D]): Traversal[S, T, C, D] = asTraversal composeTraversal other
  final def composeOptional[C, D](other: Optional[A, B, C, D]): Optional[S, T, C, D] = new Optional[S, T, C, D] {
    def _optional[F[_] : Applicative](s: S, f: C => F[D]): F[T] = self._optional(s, other._optional(_, f))
  }
  final def composePrism[C, D](other: Prism[A, B, C, D]): Optional[S, T, C, D] = composeOptional(other.asOptional)
  final def composeLens[C, D](other: Lens[A, B, C, D]): Optional[S, T, C, D] = composeOptional(other.asOptional)
  final def composeIso[C, D](other: Iso[A, B, C, D]): Optional[S, T, C, D] = composeOptional(other.asOptional)

  // Optic transformation
  final def asTraversal: Traversal[S, T, A, B] = new Traversal[S, T, A, B] {
    def _traversal[F[_] : Applicative](s: S, f: A => F[B]): F[T] = _optional(s, f)
  }

  final def asSetter: Setter[S, T, A, B] = Setter[S, T, A, B](modifyF)

  final def asFold: Fold[S, A] = new Fold[S, A]{
    def foldMap[M: Monoid](s: S)(f: A => M): M =
      _optional[({ type l[a] = Const[M, a] })#l](s, a => Const[M, B](f(a))).getConst
  }

}

object Optional {

  def apply[S, T, A, B](seta: S => T \/ A, _set: (S, B) => T): Optional[S, T, A, B] = new Optional[S, T, A, B] {
    def _optional[F[_] : Applicative](s: S, f: A => F[B]): F[T] =
      seta(s)                                   // T    \/ A
        .map(f)                                 // T    \/ F[B]
        .map(Applicative[F].map(_)(_set(s, _))) // T    \/ F[T]
        .leftMap(Applicative[F].point(_))       // F[T] \/ F[T]
        .fold(identity, identity)               // F[T]
  }

}
