;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Sequences
;;; 
;;; 
;; **

;; **
;;; ## Sequences overview
;;; ### Not really a data structure
;;; But it **looks** like a list when you print it.
;; **

;; @@
(range 5)
;; @@

;; **
;;; A list **is** a sequence, but a sequence is not necessarily a list.
;; **

;; @@
(seq? (list :a :b :c))
;; @@

;; **
;;; ### A sequence is a source of values
;;; * In some order
;;; 	* Vector or list: same as collection order
;;;     * Map or set: arbitrary, but consistent
;;;     * Sorted map or set: in sort order
;;; * Sort-of like Java [Iterator](http://docs.oracle.com/javase/7/docs/api/java/util/Iterator.html) or Ruby [Enumerator](http://www.ruby-doc.org/core-2.1.1/Enumerator.html)
;;; * May be **lazy**
;; **

;; **
;;; ### Partial computation
;;; * Most sequence functions return another sequence
;;; * Each sequence represents one step in the "job"
;;; * Compose a "stack" of lazy sequence operations
;;; * Consume the resume of non-lazy
;; **

;; **
;;; ## The Sequence library: Generating
;;; 
;;; ### Evaluating infinite sequences
;;; 
;;; _WARNING:_ Some of the examples below will result in infinite sequences. To prevent the worksheet from stalling, evaluate the following expression:
;; **

;; @@
(set! *print-length* 25)
;; @@

;; **
;;; This will set the printer to only print the first 20 items of each sequence. If the collection contains more items, the printer will print the first 25 items, followed by "..." to indicate the remaining items.
;;; 
;;; Note that if you do not evaluate the above expression, but you evaluate an infinite sequence, the affected segment will turn green, and you will need to restart the Gorilla REPL server before reloading this page.
;; **

;; **
;;; ### Generating a sequence of numbers
;; **

;; @@
(range 10)
;; @@

;; @@
(range 40 45)
;; @@

;; @@
(range)
;; @@

;; **
;;; ### Sequence from data structure
;; **

;; @@
(seq [1 2 3])
;; @@

;; **
;;; * `seq` gets a sequence from any data structure
;;; * Also strings, Java arrays, and Iterable
;;; * Every sequence function calls `seq for you
;;; * You rarely need to call `seq` except to check for empty collection
;; **

;; **
;;; ### `seq` to check for empty
;;; * `seq` on empty collection returns `nil`
;;; * `nil` is logical false
;; **

;; @@
(if (seq "")
  :not-empty
  :empty)
;; @@

;; **
;;; ### Sequence from string
;; **

;; @@
(seq "Hello, World!")
;; @@

;; @@
(remove #{\a \e \i \o \u} "Hello, World!")
;; @@

;; **
;;; ### More sequences
;;; <table>
;;; <tr>
;;; <td>file-seq</td>
;;; <td>Files in directory</td>
;;; </tr>
;;; <tr>
;;; <td>line-seq</td>
;;; <td>Lines from java.io.BufferedReader</td>
;;; </tr>
;;; <tr>
;;; <td>re-seq</td>
;;; <td>Regex matches</td>
;;; </tr>
;;; <tr>
;;; <td>tree-seq</td>
;;; <td>Generic tree walker</td>
;;; </tr>
;;; <tr>
;;; <td>resultset-seq</td>
;;; <td>SQL query results</td>
;;; </tr>
;;; <tr>
;;; <td>xml-seq</td>
;;; <td>XML document nodes</td>
;;; </tr>
;;; <tr>
;;; <td>enumeration-seq</td>
;;; <td>Java Enumeration</td>
;;; </tr>
;;; <tr>
;;; <td>iterator-seq</td>
;;; <td>Java Iterator</td>
;;; </tr>
;;; </table>
;;; 
;;; ## The Sequence library: Operations
;;; ### Iterating over a sequence
;; **

;; @@
(map (fn [n] (long (Math/pow 2 n)))
     (range 9))
;; @@

;; **
;;; ### Iterating over many sequences
;; **

;; @@
(map +
     [100 200 300 400 500]
     (range 1 999)
     [10 20 30])
;; @@

;; **
;;; _Note:_ Stops at end of **shortest** sequence.
;;; 
;;; ### Iterating with `for`
;; **

;; @@
(for [n (range 9)]
  (long (Math/pow 2 n)))
;; @@

;; **
;;; _Note:_ Not an imperative for-loop; lazy like `map`.
;;; 
;;; ### Nested iteration with `for`
;; **

;; @@
(for [k [:a :b]
      n (range 1 4)]
  [k n])
;; @@

;; **
;;; _Note:_ Different from `map` with multiple sequences
;;; 
;;; ### Shortening sequences
;; **

;; @@
(take 3 (range))
;; @@

;; @@
(drop 3 (range))
;; @@

;; @@
(take-while #(< % 5) (range))
;; @@

;; @@
(drop-while #(< % 5) (range))
;; @@

;; **
;;; ### Taking out items
;; **

;; @@
(filter even? (range 10))
;; @@

;; @@
(remove even? (range 10))
;; @@

;; **
;;; ### Grouping elements of a sequence
;; **

;; @@
(partition 3 (range 10))
;; @@

;; @@
(partition-all (range 10))
;; @@

;; @@
(group-by (fn [n] (if (even? n) :even :odd))
          (range 10))
;; @@

;; **
;;; ### Reordering a sequence
;; **

;; @@
(sort '(10 37 3 10 44 6))
;; @@

;; **
;;; _Note:_ `sort` is not lazy.
;; **

;; **
;;; #### `sort` with comparator
;; **

;; @@
(sort (fn [x y] (- (compare x y))) '(10 37 3 10 44 6))
;; @@

;; **
;;; #### `sort-by`
;; **

;; @@
;; Note: Not a comparator
(sort-by :name [{:name "Bob" :age 33}
                {:name "Ali" :age 39}
                {:name "Mel" :age 22}
                {:name "Jim" :age 58}])
;; @@

;; @@
;; Note: Not a comparator
(sort-by :age [{:name "Bob" :age 33}
               {:name "Ali" :age 39}
               {:name "Mel" :age 22}
               {:name "Jim" :age 58}])
;; @@

;; **
;;; #### `shuffle`
;; **

;; @@
(shuffle (range 10))
;; @@

;; **
;;; _Note:_ `shuffle` returns a vector, is not lazy.
;;; 
;;; #### `reverse`
;; **

;; @@
(reverse (range 10))
;; @@

;; **
;;; _Note:_ `reverse` is not lazy.
;; **

;; **
;;; ### Flattening sequences
;; **

;; @@
(mapcat (fn [i] (range i)) (range 7))
;; @@

;; @@
(flatten '(1 1 (2 3) (5 8 (13 21))))
;; @@

;; **
;;; _Note:_ `flatten` is usually a sign of poorly-constructed sequences. Prefer `mapcat` where possible.
;;; 
;;; ### Combining sequences
;; **

;; @@
(concat [:a :b :c] (range 5))
;; @@

;; **
;;; _Note:_ Beware lazy "stacks."
;; **

;; **
;;; ### Utility sequences
;;; Typically used in combination with `map` or `reduce`.
;; **

;; @@
(repeat :b)
;; @@

;; @@
(repeatedly #(rand-int 100))
;; @@

;; @@
(cycle [:a :b :c])
;; @@

;; @@
(interpose \, "abc")
;; @@

;; @@
(apply str *1)
;; @@

;; @@
(interleave [1 2 3] [:a :b :c])
;; @@

;; @@
(iterate #(* 2 %) 2)
;; @@

;; **
;;; ## The Sequence library: Results
;;; ### Collecting result of a sequence
;; **

;; @@
(vec (filter even? (range 10)))
;; @@

;; @@
(set (map inc (range 10)))
;; @@

;; @@
(apply hash-map (range 10))
;; @@

;; @@
(apply str (interpose \, (range 4)))
;; @@

;; **
;;; ### Seqs into collections
;;; * `(into _coll_ _seq_)`
;;; * Adds elements of _seq_ to _coll_ using `conj`
;;; * "Pours" _seq_ into _coll_
;; **

;; @@
(into #{} "hello")
;; @@

;; @@
(into {} [[:x 1] [:y 2]])
;; @@

;; @@
(into () [:a :b :c])
;; @@

;; **
;;; ### Consuming a sequence for side effects
;; **

;; @@
(doseq [i (range 5)]
  (prn i))
;; @@

;; **
;;; _Note:_ `doseq` always returns `nil`
;;; 
;;; ### `doseq` over many sequences
;;; Creates nested iteration like `for`.
;; **

;; **
;;; 
;; **

;; @@
(doseq [c [:a :b]
        i (range 3)]
  (prn c i))
;; @@

;; **
;;; ### `reduce`
;;; * `(reduce _function_ _init_ _seq_)`
;;; * _function_ takes two arguments
;;; 	* `reduce` calls `(_function_ _init_ (first _seq_))`
;;;     * Return value becomes _init_ for the last step
;;; * Repeat util the end of the seq, return last init
;; **

;; @@
(reduce (fn [total item] (+ total (* 10 item)))
        0	; init
        [1 2 3 4])
;; @@

;; **
;;; With no _init_, uses first element of seq
;; **

;; @@
(reduce + [1 1 2 3 5])
;; @@

;; **
;;; ### `some`
;;; * `(some _function_ _seq_)`
;;; * Maps _function_ over the seq
;;; * Returns _first_ logical true value of function
;;; * Or `nil` if nothing true
;; **

;; @@
(some #(zero? (rem % 5)) [9 22 35 76])
;; @@

;; @@
(some #(= 4 %) [1 3 5])
;; @@

;; **
;;; ### `some` with a set
;;; * Sets are functions
;;; * Can be used as linear search
;; **

;; @@
(some #{:b} [:a :b :c])
;; @@

;; @@
(some #{:foo} [:a :b :c])
;; @@

;; **
;;; ## Laziness
;;; * Sequences can by **lazy**
;;; * Compute results as needed
;;; * Only compute value once, then cached
;;; * Can be infinite!
;;; * Most Clojure functions which return sequences are lazy
;;; 
;;; ### Don't mix side effects and laziness
;;; **Chunked sequences** lead to confusing results.
;; **

;; @@
(take 4 (map println (range 100)))
;; @@

;; **
;;; ### Use side effects at the end
;;; * Usually `doseq`
;;; * Sometimes `reduce`
;;; * Rarely `doall` or `dorun`
;;; 	* "Force" complete evaluation of lazy seq
;;;     * Sequence must be finite!
;;;     * `doall` returns the entire sequence (must fit in memory!)
;;;     * `dorun` returns `nil`
;;;     
;;; ### Beware lazy stacks
;; **

;; @@
(defn build-sequence [n]
  (reduce (fn [sequence i]
            (concat sequence (range i)))
          nil
          (range 1 (inc n))))
;; @@

;; @@
(build-sequence 6)
;; @@

;; @@
(first (build-sequence 4000))
;; @@

;; **
;;; #### Non-lazy alternative
;; **

;; @@
(defn build-vector [n]
  (reduce (fn [v i]
            (into v (range i)))
          []
          (range 1 (inc n))))
;; @@

;; @@
(take 10 (build-vector 4000))
;; @@

;; **
;;; ### Non-lazy alternatives
;;; Many lazy sequence operations have non-lazy equivalents that return vectors.
;;; 
;;; ```clojure
;;; ;; Return Lazy Seq		;; Return Vector
;;; map						mapv
;;; filter					filterv
;;; concat					into []
;;; 
;;; ;; O(n) on a seq		;; O(1) on a vector
;;; last					peek
;;; butlast					pop
;;; ```
;;; 
;;; ## Sequence theory
;;; ### Sequence API
;;; * `(seq coll)`
;;; 	* If collection is not empty, return seq object on it
;;;     * If collection is empty, return nil
;;; * `(first coll)` returns the first element
;;; * `(rest coll)` returns a sequence of the rest of the elements
;;; 	* Might by empty, but not necessarily `nil`
;;; * `(next coll)` is the same as `(seq (rest coll))`
;;; * `(cons x coll)` returns a new sequence: first is x, rest is coll
;;; 
;;; ### Sequences over structures
;;; * Can treat any Clojure data structure as a seq
;;; * Lists actually _are_ seqs
;;; * Associative structures treated as sequence of pairs
;;; 
;;; ### List as a Seq
;; **

;; @@
(def x '(1 2 3))	; x is a list
;; @@

;; **
;;; ![Collections list as a seq](project-files/images/collections-seq-list-initial.svg)
;; **

;; @@
(def a (first x))	; a is 1
;; @@

;; **
;;; ![Collections seq list first](project-files/images/collections-seq-list-first.svg)
;; **

;; @@
(def s (rest x))	; s is a seq
;; @@

;; **
;;; ![Collections list as a seq](project-files/images/collections-seq-list-rest.svg)
;; **

;; @@
(def b (first (rest x)))	; b is 2
(def b (second x))			; same thing
;; @@

;; **
;;; ![Collections list as a seq](project-files/images/collections-seq-list-second.svg)
;; **

;; **
;;; ### Seq over vector
;; **

;; @@
(def v [1 2 3])	; v is a vector
;; @@

;; **
;;; ![Collections list as a seq](project-files/images/collections-seq-vector-initial.svg)
;; **

;; @@
(def s1 (seq v))	; s1 is a seq
;; @@

;; **
;;; ![Collections seq vector seq](project-files/images/collections-seq-vector-seq.svg)
;; **

;; @@
(def a (first v))	; a is 1
;; @@

;; **
;;; ![Collections seq vector first](project-files/images/collections-seq-vector-first.svg)
;; **

;; @@
(def s2 (rest v))	; s2 is a seq
;; @@

;; **
;;; ![Collections seq vector rest](project-files/images/collections-seq-vector-rest.svg)
;; **

;; @@
(def b (first (rest v)))	; b is 2
(def b (second v))			; same thing
;; @@

;; **
;;; ![Collections seq vector second](project-files/images/collections-seq-vector-second.svg)
;; **

;; **
;;; ### Sequences over functions
;;; * Can map a generator function to a seq
;;; * Seq is lazy, can be infinite
;;; 	* Can process more than fits in memory
;; **

;; @@
(def r (range 1 100))	; r is a lazy seq
;; @@

;; **
;;; ![Collections seq lazy initial](project-files/images/collections-seq-lazy-initial.svg)
;; **

;; @@
(def a (first r))	; a is 1
;; @@

;; **
;;; ![Collections seq lazy first](project-files/images/collections-seq-lazy-first.svg)
;; **

;; @@
(def s (rest r))	; s is a lazy seq
;; @@

;; **
;;; ![Collections seq lazy rest](project-files/images/collections-seq-lazy-rest.svg)
;; **

;; @@
(def b (first (rest r)))	; b is 2
(def b (second r))			; same thing
;; @@

;; **
;;; ![Collections seq lazy second](project-files/images/collections-seq-lazy-second.svg)
;; **

;; @@
(def c (first (rest (rest r))))	; c is 3
(def c (nth r 2))				; same thing
;; @@

;; **
;;; ![Collections seq lazy third](project-files/images/collections-seq-lazy-third.svg)
;;; 
;;; ### Sequences and garbage collection
;; **

;; @@
(count (range 10000000))
;; @@

;; **
;;; ![Collections seq lazy GC safe](project-files/images/collections-seq-lazy-GC-safe.svg)
;;; 
;;; ### Holding onto the head
;; **

;; @@
(def r (range 10000000))
(count r)	; out of memory error
;; @@

;; **
;;; ![Collections seq lazy GC unsafe](project-files/images/collections-seq-lazy-GC-unsafe.svg)
;;; 
;;; ### Sequences in the REPL
;;; * REPL always prints sequences with parens
;;; 	* But it's not a list!
;;; * Infinite sequences take a long time to print
;; **

;; @@
(set! *print-length* 10)	; only print 10 things
;; @@

;; **
;;; ## Generating a raw sequence
;;; ```clojure
;;; ;; pseudocode
;;; (defn generate-seq [input-source]
;;;   (lazy-seq		;macro creates lazy seq of body
;;;     (when (more-available? input-source)	; termination check
;;;       (cons (get-next-item input-source)	; construct "next seq
;;;             (generate-seq input-source)))))	; recursive call (not recur)
;;; ```
;;; 
;;; ## Combining sequence functions
;;; ### Sequence power
;;; * Generators
;;; 	* list, vector, map, SQL ResultSet, Stream, Directory, Iterator, XML, ...
;;; * Operations
;;; 	* map, filter, reduce, count, some, replace, ...
;;; * Generators * Operators = Power!
;;; 
;;; ### Adopting the sequence mindset
;;; * Sequence library surface space is big
;;; * Most things you want to do are in there somewhere
;;; * If you find youself explicitly iterating, look for a function
;;; 	* The Clojure Cheatsheet helps
;;;     
;;; ### Combining sequence functions
;; **

;; @@
;; Sum of the first 50 odd integers
(reduce + (take 50 (filter odd? (range))))
;; @@

;; @@
;; Frequence of vowels in the docstring of 'ns'
(frequencies (re-seq #"[aeiou]" (:doc (meta #'ns))))
;; @@

;; **
;;; ### Thread-last for readability
;;; * "Thread-last"
;;; * Invoke each step with the prior result as the last argument
;;; * Example:
;; **

;; @@
(->> (range) (filter odd?) (take 50) (reduce +))
;; @@

;; **
;;; ### The Fibonacci sequence
;; **

;; @@
(def fibs			; define a sequence called fibs...
  (map first		; that maps the first value of a pair across...
       (iterate		; a lazy, infinite sequence that's generated by...
         (fn [[a b]]	; a function that destructures a pair of args...
           [b (+ a b)])	; and returns the next pair in the sequence...
         [0 1])))	; starting at [0 1]
;; @@

;; @@
(take 5 fibs)	; consume as many as you'd like
;; @@

;; **
;;; ## LAB: Sums and Ciphers
;;; ### Fibonacci sum
;;; Clojure's `take` function is commonly used to work with limited portions of infinite sequences.
;;; 
;;; Given this infinite, lazy sequence of Fibonacci numbers:
;; **

;; @@
;; Be sure to evaluate this section
(def fibs
  (map first
       (iterate (fn [[a b]] [b (+ a b)])
                [0 1])))
;; @@

;; **
;;; Find the sum of the first fifty Fibonacci numbers.
;; **

;; @@
;; SOLUTION
(reduce + (take 50 fibs))
;; @@

;; **
;;; ## Reduce vs. apply
;;; The solution to the previous exercise can be written with `apply` instead of `reduce`. Why?
;;; 
;;; For most uses of `reduce`, this is not the case. Why not?
;; **

;; **
;;; ;; SOLUTION
;;; 
;;; `reduce` is designed to be used with functions that take exactly two arguments. The `+` function is somewhat unique in that it takes any number of arguments. For the non-trivial cases, it is implemented in terms of `reduce`:
;; **

;; @@
;; SOLUTION
(defn +
  ([] 0)
  ([x] (cast Number x))
  ([x y] (clojure.lang.Numbers/add x y))
  ([x y & more]
   	(reduce + (+ x y) more)))
;; @@

;; **
;;; ;; SOLUTION
;;; 
;;; When you call `(apply + some-collection)`, you are actually reducing over successive pairs of elements from the collection.
;; **

;; **
;;; ### Prime sum
;;; The `take` and `drop` functions, as well as variants like `take-while` and `drop-while`, can be combined to select subsequences out of a larger sequence.
;;; 
;;; Given this finite, lazy, and inefficient sequence of prime numbers:
;; **

;; @@
;; Make sure to evaluate this section
(def primes
  (letfn [(next-prime [known-primes n]
          	(lazy-seq
              (if (some #(zero? (rem n %)) known-primes)
                (next-prime known-primes (inc n))
                (cons n (next-prime (conj known-primes n) (inc n))))))]
    (next-prime [] 2)))
;; @@

;; **
;;; Find the sum of the first fifty primes over one hundred.
;; **

;; @@
;; SOLUTION
(reduce + (take 50 (drop-while #(< % 100) primes)))
;; @@

;; **
;;; ### Ranges of letters
;;; Suppose we want to make a collection of all the capital letters, from _A_ to _Z_. Clojure's `range` function works on numbers, but we can convert between characters and numbers using the `char` and `int` functions.
;;; 
;;; Using those functions, plus `map` and `range`, define `letters` as a sequence of capital letters.
;; **

;; @@
;; SOLUTION
(def letters (map char (range (int \A) (inc (int \Z)))))
;; @@

;; **
;;; ### Rotating a sequence
;;; In the next few exercises, we will implement the famous ROT-13 cipher, also known as the Caesar cipher.
;;; 
;;; ROT-13 works by "rotating" the alphabet 13 places to the left. So _A_ becomes _N_, _B_ becomes _O_, and so on.
;;; 
;;; Clojure's `cycle` function takes a sequential collection and returns the elements of that sequence repeated in an infinite cycle. For example:
;; **

;; @@
(take 10 (cycle [:A :B :C]))
;; @@

;; **
;;; Using `cycle`, rotate the alphabet 13 places to the left.
;; **

;; @@
;; SOLUTION
(take 26 (drop 13 (cycle letters)))
;; @@

;; **
;;; ### Generic rotation
;;; Define a function which takes two arguments, a collection and a number _n_, and rotates the collection by _n_ places.
;; **

;; @@
;; SOLUTION
(defn rotate [coll n]
  (take (count coll) (drop n (cycle coll))))
;; @@

;; **
;;; ### ROT-13 pairs
;;; Clojure's `map` function takes multiple collection arguments. Use this fact to create a sequence of pairs in the ROT-13 cipher, like `[\A \N]`.
;; **

;; @@
;; SOLUTION
(map vector letters (rotate letters 13))
;; @@

;; **
;;; ### ROT-13 as a map
;;; We will define a cipher as a map from plain-text characters to cipher-text characters. We already have a sequence of pairs. Use Clojure's `into` function to convert this sequence into a map.
;; **

;; @@
;; SOLUTION
(into {} (map vector letters (rotate letters 13)))
;; @@

;; **
;;; ### Building a map with reduce
;;; Clojure has a shortcut for this method of constructing maps called `zipmap`. Use `zipmap` to create the same map as the previous exercise. Define this map as `rot13-cipher`.
;; **

;; @@
;; SOLUTION
(def rot13-cipher (zipmap letters (rotate letters 13)))
;; @@

;; **
;;; ### Invoking the map
;;; In the classic ROT-13 cipher, non-letter characters are left unchanged. Define a function rot13-one-char which takes a single character as its argument. If that character is in the rot13-cipher map, it returns the corresponding value. If the character is not in the map, it returns the original character unchanged.
;;; 
;;; _Note:_ Our cipher **only works on capital letters.**
;; **

;; @@
;; SOLUTION
(defn rot13-one-char [c]
  (rot13-cipher c c))
;; @@

;; **
;;; ;; SOLUTION
;;; Here, we take advantage of the fact that maps are invocable, and take an optional second argument that is returned when the first argument is not a key in the map.
;; **

;; **
;;; ### Enciphering text
;;; Define a function `rot13` that takes a string argument and returns the ROT-13 enciphered version.
;;; 
;;; Remember, our cipher **only works on capital letters**.
;;; 
;;; _Hint:_ `apply str` will convert a collection of characters to a string.
;; **

;; @@
;; HINT
(rot13 "HELLO, WORLD!") ;; => "URYYB, JBEYQ!"
;; @@

;; @@
;; HINT
;; The ROT-13 cipher is its own inverse, so we can use the same function to decipher text:
(rot13 "URYYB, JBEYQ!") ;; => "HELLO, WORLD!"
;; @@

;; @@
;; SOLUTION
(defn rot13 [text]
  (apply str (map rot13-one-char text)))
;; @@

;; **
;;; ## Bonus Questions
;;; The next few exercises will help you decipher the following message:
;; **

;; @@
(def secret-message
  "FCMJ C CM U JLIALUGGCHA MSMNYG ZIL NBY CVG 704 ZIL WIGJONCHA QCNB
MSGVIFCW YRJLYMMCIHM. CN BUM VYYH OMYX ZIL MSGVIFCW WUFWOFUNCIHM CH
XCZZYLYHNCUF UHX CHNYALUF WUFWOFOM, YFYWNLCW WCLWOCN NBYILS,
GUNBYGUNCWUF FIACW, UHX ULNCZCWCUF CHNYFFCAYHWY.")
;; @@

;; **
;;; This message was encoded with a rotation cipher similar to ROT-13. We can guess which cipher was used by finding the most common letters.
;;; 
;;; #### Counting letter frequency
;;; Define a function `count-letters` that counts the occurrences of each letter in a string and returns them in a map.
;;; 
;;; _Hint:_ A common strategy for building up a map from another collection is to `reduce` over the collection, adding one or more keys to the map at each step:
;;; 
;;; ```clojure
;;; (reduce (fn [result item]
;;;           (assoc result ...))
;;;         {}	; empty map as seed for result
;;;         collection)
;;; ```
;; **

;; @@
;; SOLUTION
;; Clojure already has a function `frequencies` that does this. It could also be written:
(defn count-letters [text]
  (reduce (fn [counts character]
            (assoc counts character
              		(inc (counts character 0))))
          {}
          text))
;; @@

;; **
;;; #### Counting with fnil
;;; Clojure's `update-in` function is useful when making modifications to a map. It takes a vector of keys and a function, then uses those keys to "reach into" the map, apply the function to a value, and return the modified map. The `fnil` function is useful in conjunction with `update-in`.
;;; 
;;; Use `update-in` and `fnil` to redefine the `count-letters` function.
;; **

;; @@
;; SOLUTION
(defn count-letters [text]
  (reduce (fn [counts character]
            (update-in counts [character] (fnil inc 0)))
          {}
          text))
;; @@

;; **
;;; ### Finding most common letters
;;; Clojure's `sort-by` function can sort a collection by any arbitrary function. Use this to find the most common letters in the secret message cipher-text.
;; **

;; @@
;; SOLUTION
(sort-by (count-letters secret-message) letters)
;; @@

;; **
;;; #### Deciphering
;;; In most English texts, _E_ is the most common letter, and this fact can often be used to break encryption. Our secret message is too short for that technique to work, so we will tell you that the most common letter in the plain-text message is _I_. We know the most common letter in the cipher-text from the previous exercise.
;;; 
;;; Given those facts, define a function to decipher the secret message.
;; **

;; @@
;; SOLUTION
;; We can see from the previous exercise that C is the most common letter int he cipher-text. How far is C from I?
(- (int \I) (int \C))
;; @@

;; @@
;; SOLUTION
;; So, if we rotate the alphabet by six letters, we should be able to decipher the message.
(def rot6-cipher (zipmap letters (rotate lettesrs 6)))

(defn rot6 [text]
  (apply str (map #(rot6-cipher % %) text)))
;; @@

;; @@
;; SOLUTION
(println (rot6 secret-message))
;; @@

;; **
;;; The text is the opening paragraph of the [LISP I Programmer's Manual (PDF)](http://history.siam.org/sup/Fox_1960_LISP.pdf), published in 1960.
;; **

;; **
;;; 
;;; ## Navigation
;;; 
;;; * [Up (Home)](/worksheet.html?filename=src/cljlab/start.clj)
;;; * [Previous (Namespaces)](/worksheet.html?filename=src/cljlab/namespaces.clj)
;;; * [Next (Polymorphism)](/worksheet.html?filename=src/cljlab/polymorphism.clj)
;; **
