package scalafix.benchmarks

import java.io.File
import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import org.openjdk.jmh.infra.Blackhole

import scala.meta.inputs.Input
import scala.meta._

@BenchmarkMode(scala.Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
@Warmup(iterations = 10)
@Measurement(iterations = 10)
@State(Scope.Benchmark)
class ChildrenTraversalBenchmark {

  @Param(scala.Array("XS", "S", "M", "L", "XL"))
  var size: String = _
  val Dir = "src/main/resources"
  var tree: Tree = _


  @Setup(Level.Trial)
  def setup(): Unit = {
    tree = Input.fileToInput.apply(new File(s"$Dir/$size.scala")).parse[Source].get
  }

  private object CurrentChildrenTraverser {
    def apply(tree: Tree): Unit = tree.children.foreach(apply)
  }

  private object NewChildrenTraverser {
    def apply(tree: Tree): Unit = tree.children_new.foreach(apply)
  }

  @Benchmark
  def children_current(bh: Blackhole): Unit = {
    bh.consume(CurrentChildrenTraverser.apply(tree))
  }

  @Benchmark
  def children_new(bh: Blackhole): Unit = {
    bh.consume(NewChildrenTraverser.apply(tree))
  }
}
