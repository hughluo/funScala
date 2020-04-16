//all examples, ignore boundary checking to make it cleaner

// 0. What and why
// 0.1 What is functional programming?
// * a paradigm under declarative programming
// * programs consists entirely of functions
// * function here means function in math, not in programming languages
//   relation between sets. f(x) = x * 2

// 0.2 Why functional programming?
// * no share stats => avoid race condition => enable parallel
// * referential transparency => no side effects (so there is no for-loop in strict-fp)
// * modularity => easy to write/read/debug/reuse

// 0.3 Why using Scala here?
// * It provides both paradigm: imperative way and functional way.
// * Spark, Kafka
// * Scala is a pure object-oriented language in the sense that everything is an object
// * High sweetness: bunch of syntactic sugar
// * A language invented by a German! -- Martin Odersky.

// 1. Warm up - how to write a factorial function?
// iterative vs recursive

def factorialIterative(n: Int): Int = {
  var i = 1
  var res = 1
  while (i <= n) {
    res *= i
    i += 1
  }
  return res
}

// notice here no var involved
// instead, we use call stack to store state
def factorialRecursive(n: Int): Int = {
  if(n == 1) 1
  else {
    n * factorialRecursive(n - 1)
  }
}

factorialIterative(5)
factorialRecursive(5)

// 2. Introduction
// 2.1 introduce higher order function (in math)
// a higher order function does at least one of the following:
// takes one or more functions as arguments,
// returns a function as its result.

// use int as example, of course we can use generics

// 2.2 introduce map

def mapImperative(xs: List[Int], f: Int => Int): List[Int] = {
  val xsArr = xs.toArray
  val resArr = new Array[Int](xsArr.length)
  var i = 0
  while (i < xsArr.length) {
    val curr = xsArr(i)
    resArr(i) = f(curr)
    i += 1
  }
  return resArr.toList
}

// 2.3 introduce anonymous function

// (or lambda function, which comes from λ-calculus
// which is then a formal system in mathematical logic for expressing computation based on
// function abstraction and application using variable binding and substitution)
def multiply2(a: Int): Int = a * 2

// the following three are equivalent, at least behave the same
mapImperative(List(1, 2, 3, 4 ,5), multiply2)
mapImperative(List(1, 2, 3, 4, 5), x => x * 2)
mapImperative(List(1, 2, 3, 4, 5), _ * 2)

def mapFunctional(xs: List[Int], f: Int => Int): List[Int] = xs match {
  case List(x) => List(f(x))
  case x :: rest => f(x) :: mapFunctional(rest, f)
}

mapFunctional(List(1, 2, 3, 4, 5), x => x * 2)
// Cool, isn't it? Both Neat and easy to understand

// 2.4 introduce reduce

// reduce takes all the elements in a collection (Array, List, etc) and
// combines them using a binary operation
// to produce a single value.

// here is how a reduce behave (code in imperative way)
// WARNING: To be able to be parallelled, such binary operation should be a "commutative monoid",
// i.e. an operation that is both commutative and associative
// add, multiply but not minus or divide

def reduceImperative(xs: List[Int], biOp: (Int, Int) => Int): Int = {
  val xsArr = xs.toArray
  var acc = xsArr(0)
  var i = 1
  while (i < xsArr.length) {
    var curr = xsArr(i)
    acc = biOp(acc, curr)
    i += 1
  }
  return acc
}

reduceImperative(List(1, 2, 3, 4, 5), _*_)


// here is how a reduce in functional way, using "pattern matching"
def reduceFunctional(xs: List[Int], biOp: (Int, Int) => Int): Int = xs match {
  case List(x) => x
  case x :: rest => biOp(x, reduceFunctional(rest, biOp))
}

reduceFunctional(List(1, 2, 3, 4, 5), _*_)

// so far, you understand how the famous map/reduce works! YEAH!



// 2.5 functional way to think, back to factorial example at the very beginning

def factorialFunctional(n: Int): Int = {
  val xs = 1 to n
  xs.reduce(_*_)
}


def higherOder(funcIn: (Int, Int) => Int): Int => Int = {
  def funcOut(end: Int): Int = {
    val xs = 1 to end
    xs.reduce(funcIn)
  }
  funcOut
}

def factorialHigherOrder = higherOder(_*_)

factorialHigherOrder(5)

// further reading
// * why functional programming matters
// * scala course in coursera

