package scalafix.benchmarks

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import scala.meta._

@BenchmarkMode(scala.Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
class ChildrenBenchmark {

  @Param(scala.Array("scala.meta.Term.Name", "scala.meta.Term.Return", "scala.meta.Term.Match", "scala.meta.Term.If", "scala.meta.Term.Try", "scala.meta.Defn.Def"))
  var treeType: String = _
  var tree: Tree = _

  // scala.collection.immutable.List()
  val termName: Term.Name = q"a"

  // scala.collection.immutable.List(this.expr)
  val termReturn: Term.Return = q"return"

  // scala.collection.immutable.List(this.expr).$plus$plus(this.cases).$plus$plus(scala.collection.immutable.List())
  val termMatch: Term.Match =
    q"""
       foo match {
         case 0 =>
         case 1 =>
         case 2 =>
         case 3 =>
         case 4 =>
         case 5 =>
         case 6 =>
         case 7 =>
         case 8 =>
         case 9 =>
         case _ =>
       }
     """

  // scala.collection.immutable.List(this.cond, this.thenp, this.elsep)
  val termIf: Term.If = q"if (true) 1 else 2"

  // scala.collection.immutable.List(this.expr).$plus$plus(this.catchp).$plus$plus(scala.collection.immutable.List()).$plus$plus(this.finallyp.toList).$plus$plus(scala.collection.immutable.List())
  val termTry: Term.Try =
    q"""
         try {
            1 / 0
         } catch {
            case _: Exception => 0
         } finally {
            // whatever
         }
       """

  // scala.collection.immutable.List().$plus$plus(this.mods).$plus$plus(scala.collection.immutable.List(this.name)).$plus$plus(this.tparams).$plus$plus(scala.collection.immutable.List()).$plus$plus(this.paramss.flatten).$plus$plus(scala.collection.immutable.List()).$plus$plus(this.decltpe.toList).$plus$plus(scala.collection.immutable.List(this.body))
  val defnDef: Defn.Def =
    q"""
       protected final def foo[A <: Int, B <: Int, C <: Int, D <: Int, E <: Int, F <: Int, G <: Int, H <: Int, I <: Int, J <: Int]
                              (a: A, b: B, c: C, d: D, e: E, f: F, g: G, h: H, i: I, j: J): String = { "foo" }
     """

  val type2Tree = Map(
    "scala.meta.Term.Name" -> termName,
    "scala.meta.Term.Return" -> termReturn,
    "scala.meta.Term.Match" -> termMatch,
    "scala.meta.Term.If" -> termIf,
    "scala.meta.Term.Try" -> termTry,
    "scala.meta.Defn.Def" -> defnDef
  )

  @Setup(Level.Trial)
  def setup(): Unit = {
    tree = type2Tree(treeType)
  }

  @Benchmark
  def children_current(bh: Blackhole): Unit = {
    bh.consume(tree.children)
  }

  @Benchmark
  def children_new(bh: Blackhole): Unit = {
    bh.consume(tree.children_new)
  }
}
