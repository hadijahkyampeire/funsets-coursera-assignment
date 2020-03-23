package funsets

import org.junit._

/**
 * This class is a test suite for the methods in object FunSets.
 *
 * To run this test suite, start "sbt" then run the "test" command.
 */
class FunSetSuite {

  import FunSets._

  @Test def `contains is implemented`: Unit = {
    assert(contains(x => true, 100))
  }

  @Test def `it should not contain 100`: Unit = {
    assert(!contains({x: Int => x >0 }, -100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(1)
    val setPositiveNumbers = union(singletonSet(1), singletonSet(300))
    val setNegativeNumbers = union(singletonSet(-10), singletonSet(-99))
    val setPositiveAndNegativeNumbers = union(setPositiveNumbers, setNegativeNumbers)
    val setEvenNumbers = union(singletonSet(4), singletonSet(6))
    val setOddNumbers = union(singletonSet(3), singletonSet(9))
    val setEvenAndOddNumbers = union(setEvenNumbers, setOddNumbers)
  }

  /**
   * This test is currently disabled (by using @Ignore) because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", remove the
   * @Ignore annotation.
   */
  @Test def `singleton set one contains one`: Unit = {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton contains 1")
    }
  }

  @Test def `singleton set one should not contain 3`: Unit = {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(!contains(s1, 3), "Singleton doesnot contain 3")
    }
  }

  @Test def `union contains all elements of each set`: Unit = {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  @Test def `intersect contains elements in both sets`: Unit =  {
    new TestSets {
      val intersection1 = intersect(s1, s2)
      assert(!contains(intersection1, 1), "Intersect 1 between singletonSet(1) and singletonSet(2)")
      val intersection2 = intersect(s1, s4)
      assert(contains(intersection2, 1), "Intersect 1 between singletonSet(1) and singletonSet(4)")
      assert(!contains(intersection2, 2), "Intersect 2 between singletonSet(1) and singletonSet(4)")


    }
  }

  @Test def `diff returns the difference between two sets`: Unit =  {
    new TestSets {
      val difference1 = diff(s1, s2)
      assert(contains(difference1, 1), "Diff 1 between singletonSet(1) and singletonSet(2)")
      val difference2 = diff(s1, s4)
      assert(!contains(difference2, 1), "Does not containe Diff 1 between singletonSet(1) and singletonSet(2)")


    }
  }


  @Test def `filter returns the subset of one set for which a parameter function holds`: Unit = {
    new TestSets {
      val filterSet1 = filter(s1, {elem:Int => elem < 2})
      assert(contains(filterSet1, 1))


      val filterSet2 = filter(s3, {elem:Int => elem > 5})
      assert(!contains(filterSet2, 3))


    }
  }

  @Test def `forall function`: Unit = {
    new TestSets {
      assert(forall(setPositiveNumbers, { elem:Int => elem > 0 }))

      assert(forall(setNegativeNumbers, { elem:Int => elem < 0 }))

      assert(!forall(setPositiveAndNegativeNumbers, { elem: Int => elem > 0 }))

      assert(forall(setEvenNumbers, { elem:Int => (elem % 2) == 0 }))

      assert(forall(setOddNumbers, { elem:Int => (elem % 2) != 0 }))

      assert(!forall(setEvenAndOddNumbers, { elem:Int => (elem % 2) == 0 }))
    }
  }

  @Test def `exists function`: Unit = {
    new TestSets {

      assert(exists(setPositiveAndNegativeNumbers, {elem:Int => elem > 0}))

      assert(exists(setEvenAndOddNumbers, {elem:Int => (elem % 2) ==  0}))
    }
  }

  @Test def `map function`: Unit = {
    new TestSets {
      val mapEvenSetToOdd = map(setEvenNumbers, { elem:Int => elem + 1})
 
      assert(contains(mapEvenSetToOdd,5) && contains(mapEvenSetToOdd,7))

      val mapOddSetToEven = map(setOddNumbers, { elem:Int => elem * 2})

      assert(forall(mapOddSetToEven, {elem:Int => (elem % 2) == 0}))


    }
  }

  @Rule def individualTestTimeout = new org.junit.rules.Timeout(10 * 1000)
}
