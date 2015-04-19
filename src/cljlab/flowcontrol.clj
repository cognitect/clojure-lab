;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Flow Control
;;; 
;;; 
;; **

;; **
;;; ## Statements vs. Expressions
;;; In Java, expressions return values, whereas statements do not.
;;; 
;;; ```java
;;; // "if" is a statement because it doesn't return a value:
;;; String s;
;;; if (x > 10) {
;;;     s = "greater";
;;; } else {
;;;     s = "greater or equal";
;;; }
;;; obj.someMethod(s);
;;; 
;;; // Ternary operator is an expression; it returns a value:
;;; obj.someMethod(x > 10 ? "greater" : "greater or equal");
;;; ```
;;; 
;;; In Clojure, however, everything is an expression! _Everything_ returns a value, and a block of multiple expressions returns the last value. Expressions that exclusively perform side-effects return `nil`.
;;; 
;;; ## Flow Control Expressions
;;; Accordingly, flow control operators are expressions, too!
;;; 
;;; Flow control operators are composable, so we can use them anywhere. This leads to less duplicate code, as well as fewer intermediate variables.
;;; 
;;; Flow control operators are also extensible via macros, which allow the compiler to be extended by user code. We won't be discussing macros today, but you can read more about them at [Clojure.org](http://clojure.org/macros), [Clojure from the Ground Up](https://aphyr.com/posts/305-clojure-from-the-ground-up-macros), or [Clojure for the Brave and True](http://www.braveclojure.com/writing-macros/), among many other places.
;;; 
;;; ## `if`
;;; 
;;; `if` is the most important conditional expression - it consists of a condition, a "then", and an "else". `if` will only evaluate the branch selected by the conditional.
;; **

;; @@
(str "2 is " (if (even? 2) "even" "odd"))
;; @@

;; @@
;; else expression is optional
(if (true? false) "impossible!")
;; @@

;; **
;;; ## Truthiness
;;; 
;;; In Clojure, all values are "truthy" or not. The only "false" values are `false` and `nil` - all other values are "truthy".
;; **

;; @@
(if true :truthy :falsey)
;; @@

;; @@
(if (Object.) :truthy :falsey) ; objects are true
;; @@

;; @@
(if [] :truthy :falsey) ; empty collections are true
;; @@

;; @@
(if 0 :truthy :falsey) ; zero is true
;; @@

;; @@
(if false :truthy :falsey)
;; @@

;; @@
(if nil :truthy :falsey) ; nil is false
;; @@

;; @@
(if (seq []) :truthy :falsey) ; seq on empty coll is nil
;; @@

;; **
;;; ## `if` and `do`
;;; 
;;; The `if` only takes a single expression for the "then" and "else". Use do to create larger blocks that are a single expression. 
;;; 
;;; Note that the only reason to do this is if your bodies have side effects! (Why?)
;; **

;; @@
(if (even? 5)
  (do (println "even")
    true)
  (do (println "odd")
    false))
;; @@

;; **
;;; ## `when`
;;; 
;;; `when` is a one-armed `if`. It checks a condition and then evaluates any number of expressions as a body (so no `do` is required). The value of the last expression is returned. If the condition is false, nil is returned.
;;; 
;;; `when` communicates to a reader that there is no "else" branch.	
;; **

;; @@
(when (neg? x)
  (throw (RuntimeException. (str "x must be positive: " x))))
;; @@

;; **
;;; ## `cond`
;;; 
;;; `cond` is a series of tests and expressions. Each test is evaluated in order and the expression is evaluated and returned for the first true test. 
;; **

;; @@
(let [x 5]
  (cond
    (< x 2) "x is less than 2"
    (< x 10) "x is less than 10"))
;; @@

;; **
;;; ## `cond` and `else`
;;; 
;;; If no test is satisfied, nil is returned. A common idiom is to use a final test of `:else`. Keywords (like `:else`) always evaluate to true so this will always be selected as a default.
;; **

;; @@
(let [x 11]
  (cond
    (< x 2)  "x is less than 2"
    (< x 10) "x is less than 10"
    :else  "x is greater than or equal to 10"))
;; @@

;; **
;;; ## `case`
;;; 
;;; `case` compares an argument to a series of values to find a match. This is done in constant (not linear) time! However, each value must be a compile-time literal (numbers, strings, keywords, etc).
;; **

;; @@
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"))
;; @@

;; @@
(foo 10)
;; @@

;; **
;;; Unlike `cond`, `case` will throw an exception if no value matches:
;; **

;; @@
(foo 11)
;; @@

;; **
;;; ## `case` with else-expression
;;; 
;;; `case` can have a final trailing expression that will be evaluated if no test matches.
;; **

;; @@
(defn foo [x]
  (case x
    5 "x is 5"
    10 "x is 10"
    "x isn't 5 or 10"))
;; @@

;; @@
(foo 10)
;; @@

;; @@
(foo 11)  ;; falls into default instead of erroring
;; @@

;; **
;;; ## Iteration for Side Effects
;;; ### `dotimes`
;;; * Evaluate expresson _n_ times
;;; * Returns `nil`
;; **

;; @@
(dotimes [i 3]
  (println i))
;; @@

;; **
;;; ### doseq
;;; * Iterates over a sequence
;;;   * Similar to Java's for-each loop
;;; * If a lazy sequence, forces evaluation
;;; * Returns `nil`
;; **

;; @@
(doseq [n (range 3)]
  (println n))
;; @@

;; **
;;; ### `doseq` with multiple bindings
;;; * Similar to nested `foreach` loops
;;; * Processes all permutations of sequence content
;;; * Returns `nil`
;; **

;; @@
(doseq [letter [:a :b]
      number (range 3)] ; list of 0, 1, 2
  (pr [letter number]))
;; @@

;; **
;;; ## Clojure's `for`
;;; * List comprehension, *not* a for-loop
;;; * Generator function for sequence permutation
;;; * Bindings behave like `doseq`
;; **

;; @@
(for [letter [:a :b]
      number (range 3)] ; list of 0, 1, 2
  [letter number])
;; @@

;; **
;;; ## Recursion
;;; 
;;; ### Recursion and Iteration
;;; * Clojure provides recur and the sequence abstraction
;;; * `recur` is "classic" recursion
;;;   * Closed to consumers, lower-level
;;; * Sequences represent iteration as values
;;;   * Consumers can partially iterate
;;; * Reducers represent iteration as function composition
;;;   * Added in Clojure 1.5, not convered here
;;; 
;;; ### `loop` and `recur`
;;; * Functional looping construct
;;;   * `loop` defines bindings
;;;     * `recur` re-executes `loop` with new bindings
;;; * Prefer higher-order library functions
;; **

;; @@
(loop [i 0]
  (if (< i 10)
    (recur (inc i))
    i))
;; @@

;; **
;;; ### `defn` and `recur`
;;; * Function arguments are implicit `loop` bindings
;; **

;; @@
(defn increase [i]
  (if (< i 10)
    (recur (inc i))
    i))
;; @@

;; @@
(increase 1)
;; @@

;; **
;;; ### `recur` for recursion
;;; * `recur` must be in "tail position"
;;;   * The last expression in a branch
;;; * `recur` must provide values for all bound symbols by position
;;;   * Loop bindings
;;;     * defn/fn arguments
;;; * Recursion via `recur` does not consume stack
;;; 
;;; ## Exceptions
;;; 
;;; ### Exception handling
;;; * `try`/`catch`/`finally` as in Java
;; **

;; @@
(try
  (/ 2 1)
  (catch ArithmeticException e
    "divide by zero")
  (finally
    (println "cleanup")))
;; @@

;; **
;;; ### Throwing exceptions
;; **

;; @@
(try
  (throw (Exception. "something went wrong"))
  (catch Exception e (.getMessage e)))
;; @@

;; **
;;; ### Exceptions with Clojure data
;;; * `ex-info` takes a message and a map
;;; * `ex-data` gets the map back out
;;;   * Or `nil` if not created with `ex-info`
;; **

;; @@
(try
  (throw (ex-info "There was a problem" {:detail 42}))
  (catch Exception e
    (prn (:detail (ex-data e)))))
;; @@

;; **
;;; ### `with-open`
;; **

;; @@
(let [f (clojure.java.io/writer "/tmp/new")]
  (try
    (.write f "some text")
    (finally
      (.close f))))
;; @@

;; @@
;; Can be written:
(with-open [f (clojure.java.io/writer "/tmp/new")]
  (.write f "some text"))
;; @@

;; **
;;; # LAB: Flow Control
;;; 
;;; ## I am thinking of a number
;;; Let's play a number-guessing game. Define a function `check-guess` that takes two arguments:
;;; * `secret`, the number the player is trying to guess, and
;;; * `guess`, the player's most recent guess.
;;; 
;;; The function should return a string:
;;; * "You win!" if the numbers are equal,
;;; * "Too low" if the guess is less than the secret, or
;;; * "Too high" if the guess is greater than the secret.
;;; 
;;; Only use `if`, not `cond`.
;; **

;; @@
;; SOLUTION
(defn check-guess [secret guess]
  (if (= guess secret)
    "You win!"
    (if (< guess secret)
      "Too low"
      "Too high")))
;; @@

;; **
;;; ## I am thinking of another number
;;; Repeat the previous exercise using `cond` instead of `if`.
;; **

;; **
;;; 
;; **

;; @@
;; SOLUTION
(defn check-guess [secret guess]
  (cond (= guess secret)
          "You win!"
        (< guess secret)
          "Too low"
        :else
          "Too high"))
;; @@

;; **
;;; ## Triplicate redux
;;; Define a function `triplicate` that takes a single argument--a function `f`--and calls that function three times. Use `dotimes`.
;;; 
;;; Test it with an anonymous function that prints `:hi`.
;; **

;; @@
;; SOLUTION
(defn triplicate [f]
  (dotimes [i 3]
    (f)))
;; @@

;; @@
(triplicate #(prn :hi))
;; @@

;; **
;;; ## Printing numbers
;;; Define a function `numbers` that takes a single argument `n` and prints all the numbers from zero to _n-1_ (inclusive), one per line.
;; **

;; @@
(defn numbers [n]
  (dotimes [i n]
    (println i)))
;; @@

;; **
;;; ## Counting numbers
;;; Define a function `counting` that takes a single argument `n` and prints all the numbers from one to `n` (inclusive), one per line.
;;; 
;;; _Hint:_ Use `doseq` and `range`.
;; **

;; @@
(defn counting [n]
  (doseq [i (range 1 (inc n))]
    (println i)))
;; @@

;; **
;;; ## Garage band
;;; Let's start a band! Define a function `print-bands` that takes three arguments:
;;; * `guitars`, a vector of guitarists' names,
;;; * `basses`, a vector of bass players' names, and
;;; * `drums`, a vector of drummers' names.
;;; 
;;; The function should print all the possible three-piece combinations you can make with those players, like this:
;;; ```
;;; (print-bands ["Gary" "Gus"]
;;;        ["Bill" "Bob" "Buster"]
;;;              ["Darrell"])
;;;              
;;; ;; Gary Bill Darrell
;;; ;; Gary Bob Darrell
;;; ;; Gary Buster Darrell
;;; ;; Gus Bill Darrell
;;; ;; Gus Bob Darrell
;;; ;; Gus Buster Darrell
;;; ```
;; **

;; @@
;; SOLUTION
(defn print-bands [guitars basses drums]
  (doseq [g guitars
          b basses
          d drums]
    (println g b d)))
;; @@

;; **
;;; ## Return of the garage band
;;; Define a function `all-bands` that takes the same arguments as `print-bands`, but instead of printing all the combinations, it returns them in a sequence. Each item in the sequence should be a vector like `[guitarist bass drummer]`.
;;; 
;;; _Hint:_ Use `for`.
;; **

;; @@
(defn all-bands [guitars basses drums]
  (for [g guitars
        b basses
        d drums]
    [g b d]))
;; @@

;; **
;;; ## Fizzbuzz
;;; It's everybody's favorite programming problem! Define a function `fizzbuzz` that prints the numbers from `1` to `100` (inclusive), subject to the following rules:
;;; * If the number is a multiple of three, print "Fizz";
;;; * If the number is a multiple of five, print "Buzz";
;;; * If the number is a multiple of _both_ three and five, print "FizzBuzz"; and
;;; * If the number is _not_ a multiple of either three or five, print the number.
;;; 
;;; _Hint:_ The `rem` function (short for "remainder") computes the remainder of dividing two numbers, like Java's `%` operator. Clojure also has a `zero?` function to test if a number is equal to zero.
;; **

;; @@
;; SOLUTION
(defn fizzbuzz []
  (doseq [i (range 1 101)]
    (println (cond (and (zero? (rem i 3))
                        (zero? (rem i 5)))
                   (zero? (rem i 3))
                    "Fizz"
                   (zero? (rem i 5))
                    "Buzz"
                   :else i))))
;; @@

;; @@
;; SOLUTION
;; If you want to avoid repeating the `rem` operation.
(defn fizzbuzz []
  (doseq [i (range 1 101)]
    (let [fizz (if (zero? (rem i 3)) "Fizz")
          buzz (if (zero? (rem i 5)) "Buzz")
          number (if (not (or fizz buzz)) i)]
      (println (str fizz buzz number)))))
;; @@

;; **
;;; ## Euclid's algorithm
;;; [Euclid's algorithm](http://en.wikipedia.org/wiki/Euclidean_algorithm) finds the greatest common divisor of two integers using only substraction. In imperative pseudo-code, it looks like this:
;;; 
;;; ```
;;; function gcd(A, B):
;;;   do loop:
;;;       if A == 0
;;;           return B
;;;         if B == 0
;;;           return B
;;;         if A > B
;;;           A := A - B
;;;         else
;;;           B := B - A
;;; ```
;;; 
;;; Define a function `gcd` that implements Euclid's algorithm. Use `recur`.
;; **

;; @@
;; SOLUTION
(defn gcd [a b]
  (cond (zero? a) b
        (zero? b) a
        (> a b)   (recur (- a b) b)
        :else   (recur a (- b a))))
;; @@

;; **
;;; Some tests:
;; **

;; @@
;; TESTS
(assert (= 1 (gcd 3 4)))
(assert (= 3 (gcd 3 6)))
(assert (= 3 (gcd 6 3)))
(assert (= 25 (gcd 100 25)))
(assert (= 4 (gcd 100 8)))
(assert (= 8 (gcd 16 24)))
;; @@

;; **
;;; ## Bonus: I am thinking of a number
;;; Define a function that returns another function to play the number-guessing game.
;;; 
;;; That is, define a function `guessing-game` that picks a random number and returns a function that takes a single argument, the player's guess. The returned function should return the same strings as in the first exercise.
;;; 
;;; _Hint:_ Clojure's `rand-int` function returns a random integer.
;; **

;; @@
;; SOLUTION
(defn guessing-game []
  (let [secret (rand-int 100)]
    (fn [guess]
      (cond (= guess secret)
              "You win!"
            (< guess secret)
              "Too low"
            :else
              "Too high"))))
;; @@

;; **
;;; ## Bonus: Binary search
;;; Use `loop` and `recur` to implement a binary search. Define a function `binary-search` that takes two arguments:
;;; * `n`, a number, and
;;; * `nums`, a sorted vector of numbers.
;;; 
;;; The function should return `true` if `nums` contains `n` and `false` if it does not.
;; **

;; @@
;; SOLUTION
(defn binary-search [n nums]
  (loop [start 0
         end (count nums)]
    (if (= start end)
      false
      (let [index (+ start (int (/ (- end start) 2)))
            x (nth nums index)]
        (cond (= n x) true
              (< n x) (recur start index)
              :else   (recur (inc index) end))))))
;; @@

;; **
;;; And some tests:
;; **

;; @@
;; SOLUTION
(assert (false? (binary-search 4 [1 3 5 7 9])))
(assert (false? (binary-search 11 [1 3 5 7 9])))
(assert (true? (binary-search 7 [1 3 5 7 9])))
(assert (true? (binary-search 1 [1 3 5 7 9])))
(assert (true? (binary-search 9 [1 3 5 7 9])))

(assert (true? (binary-search 54 (range 0 100 2))))
(assert (true? (binary-search 98 (range 0 100 2))))
;; @@

;; **
;;; 
;;; ## Navigation
;;; 
;;; * [Up (Home)](/worksheet.html?filename=src/cljlab/start.clj)
;;; * [Previous (Collections)](/worksheet.html?filename=src/cljlab/collections.clj)
;;; * [Next (Namespaces)](/worksheet.html?filename=src/cljlab/namespaces.clj)
;; **
