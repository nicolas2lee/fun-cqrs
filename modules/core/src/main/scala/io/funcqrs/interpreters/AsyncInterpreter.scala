package io.funcqrs.interpreters
import io.funcqrs.AggregateLike
import io.funcqrs.behavior.{ Behavior, FutureCommandHandlerInvoker, IdCommandHandlerInvoker, TryCommandHandlerInvoker }

import scala.concurrent.Future
import scala.language.higherKinds

/**
 * An Interpreter with F[_] bounded to [[Future]].
 *
 * All command handling are interpreted to [[Future]] of Events.
 *
 * @param behavior - a Aggregate [[Behavior]]
 * @tparam A - an Aggregate type
 */
class AsyncInterpreter[A <: AggregateLike](val behavior: Behavior[A]) extends Interpreter[A, Future] {

  def handleCommand(optionalAggregate: Option[A], cmd: Command): Future[Events] = {

    behavior.handleCommand(optionalAggregate, cmd) match {
      case IdCommandHandlerInvoker(handler) => Future.successful(handler(cmd))
      case TryCommandHandlerInvoker(handler) => Future.fromTry(handler(cmd))
      case FutureCommandHandlerInvoker(handler) => handler(cmd)
    }
  }

}

object AsyncInterpreter {
  def apply[A <: AggregateLike](behavior: Behavior[A]) = new AsyncInterpreter(behavior)
}