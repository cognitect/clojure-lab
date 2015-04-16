;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Polymorphism
;;; 
;;; 
;; **

;; **
;;; ## Motivation
;;; 
;;; There are many situations where it is useful to provide behavior that varies based on type or value. Clojure provides two different mechanisms to for conditional (polymorphic) behavior: protocols and multimethods.
;;; 
;;; Also, Clojure provides two constructs that allow you to create new data "types": records (which we saw in collections earlier) and types.
;;; 
;;; Clojure was created with some strong opinions in this area:
;;; 
;;; * Only derive from interfaces, never from implementations
;;; * All generic methods should be defined in interfaces
;;; * Polymorphism doesn't require inheritance
;;; * Data is still immutable
;;; * No data hiding (aka encapsulation)
;;; 
;;; ### What does "type" mean?
;;; 
;;; "What is the type (class) of object x?"
;;; * `(class x)` or `(type x)`
;;; 
;;; "Foo is a type (class)"
;;; * `(defrecord Foo ...)` or `(deftype Foo ...)`
;;; * or Java `public class Foo { ... }`
;;; * Java primitive types - `long`, `double`, arrays, etc
;;; 
;;; ## Protocols
;;; 
;;; Protocols define an abstract behavioral contract. Protocols create a named group of generic functions. Each function has (only) parameters and doc strings, no implementation.
;;; 
;;; Protocols are polymorphic (choose the implementation) based on the type of the first argument (like methods in Java). Each protocol function must have at least one argument (the one used for dispatch) - equivalent to `this` in Java.
;;; 
;;; ### defprotocol
;; **

;; @@
(defprotocol MyProtocol
  "A doc string for MyProtocol abstraction"
  (bar [q r] "bar docs")
  (baz [q] "baz docs"))
;; @@

;; **
;;; Define a protocol named MyProtocol (in the current namespace) with a protocol docstring and functions `bar` and `baz`. These  functions both dispatch based on the type of `q`.
;; **

;; **
;;; ### Protocol dispatch
;;; 
;;; `defprotocol` creates generic functions. These are normal functions, just like `defn`. They are invoked just like any other Clojure function.
;; **

;; @@
(defprotocol Describe
  (desc [self]))

(desc thing)  ; Invoke it like this

(.desc thing) ; Not this

;; Similar to Java:  thing.desc()
;; @@

;; **
;;; ### Extending Protocols to Types
;;; 
;;; What if we want to add protocols to an existing type (for example, Java types like `String`)?
;;; 
;;; This is sometimes called the "Expression Problem". The problem  is how to enable extending either the set of concrete types or the set of generic operations while existing code continues to work.
;;; 
;;; Common solutions won't work:
;;; * Inheritance - can't inherit from `String`
;;; * Multiple inheritance - complex, not allowed in Java
;;; * Wrapping - complex, breaks type and equality
;;; * Open classes - no namespacing, error-prone
;;; * Conditionals - complex, not extensible without changing code
;;; 
;;; ### Protocol extension
;;; 
;;; Clojure allows extending any protocol to any type, including final Java classes. The type is not modified in any way. Implementations can be extended to both nil and Object.
;;; 
;;; ### `extend-type` and `extend-protocol`
;;; 
;;; Extensions are specified with `extend-type` or `extend-protocol`.
;; **

;; @@
(extend-type SomeType          ; 1 type, many protocols
  SomeProtocol
  (some-method [...] ...)
  AnotherProtocol
  (another-method [...] ...))

(extend-protocol SomeProtocol  ; 1 protocol, many types
  SomeType
  (some-method [...] ...)
  AnotherType
  (some-method [...] ...))
;; @@

;; **
;;; ### `extend-type example
;; **

;; @@
; java.lang.String does not implement Describe
(desc "a")

; IllegalArgumentException No implementation of
; method: :desc of protocol: #'user/Describe
; found for class: java.lang.String 

(satisfies? Describe "a")
;;=> false

; extend Describe to java.lang.String
(extend-type String
  Describe
  (desc [s] s))

(satisfies? Describe "a")
;;=> true

; try again...
(desc "a")
;;=> "a"
;; @@

;; **
;;; ### Reifying protocols
;;; 
;;; `reify` builds anonymous type and instance on the fly, conceptually similar to anonymous functions or anonymous inner classes in Java. Function bodies are closures.
;; **

;; @@
(def r (let [x 42]
         (reify Describe
           (desc [_] (str "describe with " x)))))
;;=> #user/r

(desc r)
;;=> "describe with 42"
;; @@

;; **
;;; ## Multimethods
;;; 
;;; Protocols are limited to single-argument dispatch on the type of the first argument. Multimethods support multi-argument dispatch on any criteria of all of the arguments, so it is much more flexible.
;; **

;; @@
(defmulti reaction
  ;; 2-argument dispatch function:
  (fn [a b] [(:species a) (:species b)]))

(defmethod reaction [:hero :monster] ; match criteria
  [hero monster]                     ; function parameters
  (str hero " fights " monster))     ; function body

(defmethod reaction [:monster :hero]
  [monster hero]
  (str monster " eats " hero))

(defmethod reaction [:monster :monster]
  [monster1 monster2]
  (str monster1 " plays with " monster2))

(defmethod reaction [:hero :hero]
  [hero1 hero2]
  (str hero1 " taunts " hero2))
;; @@

;; **
;;; ### Custom dispatch
;;; 
;;; 
;; **

;; @@
(defmulti shape count)

(defmethod shape 3 [points] "triangle")
(defmethod shape 4 [points] "rectangle")
(defmethod shape 5 [points] "pentagon")
(defmethod shape 6 [points] "hexagon")
(defmethod shape :default [n] "who cares?")

(shape [[0 0], [0 5], [5 0]]) ;;=> "triangle"
(shape [[0 0], [0 5], [5 0], [5 5]]) ;;=> "rectangle"
(shape []) ;;=> "who cares?"
;; @@

;; **
;;; ### Multimethods vs Protocols
;;; 
;;; | | Multimethods | Protocols | 
;;; |-|--------------|-----------|
;;; | Extensible | yes | yes|
;;; | Java interop story | Vars | interfaces | 
;;; | Dispatch on arguments | any number | only first | 
;;; | Dispatch function | arbitrary | only type | 
;;; | Method grouping |  no | yes |
;;; | High performance | no | yes |
;;; 
;; **

;; **
;;; ## Records 
;;; 
;;; As we discussed in the Collections section, records create entities with explicit types, not just generic maps. Records can implement both protocols and interfaces directly at definition time:
;; **

;; @@
(defrecord Car [make model year])   ; named type with fields
;;=> user.Car

(def car (->Car "Dodge" "Omni" 1980)) ; instantiation
;;=> #'user/car

(:year car)                   ; field access
;;=> 1980
;; @@

;; **
;;; ### Records are classes
;;; 
;;; 
;; **

;; @@
(def car (Car. "Dodge" "Omni" 1980))  ; Java constructor
;;=> #'user/car

(.-year car)               ; fields are public & final
;;=> 1980                 

(class car)                ; ordinary class
;;=> user.Car

(supers (class car))       ; lots of built-in functionality

;;=> #{clojure.lang.IObj clojure.lang.IKeywordLookup java.util.Map
;; clojure.lang.IPersistentMap clojure.lang.IMeta java.lang.Object
;; java.lang.Iterable clojure.lang.ILookup clojure.lang.Seqable
;; clojure.lang.Counted clojure.lang.IPersistentCollection
;; clojure.lang.Associative}
;; @@

;; **
;;; ### Implementing protocols on records
;;; 
;;; Protocols (and Java interfaces) can be implemented directly in `defrecord`. Method implementation bodies can access record fields directly but *do not* close over the lexical environment. If a record does not implement all of the protocol methods, undefined ones will throw `AbstractMethodError`.
;; **

;; @@
; specify protocol(s) directly inline
(defrecord Car [make model year]
  Describe
  (desc [self] (str year " " make " " model)))

(def car (->Car "Dodge" "Omni" 1980))  
(desc car)
;;=> "1980 Dodge Omni"
;; @@

;; **
;;; ### Using protocols and records
;; **

;; @@
(defprotocol Player "A rock/paper/scissors player"
  (choose [p] "return :rock, :paper or :scissors")
  (update-player [p me you]
    "return a new player based on what you and I did"))

(defrecord Stubborn [choice]
  Player ; implement Player protocol
  (choose [_] choice) ; always play the choice
  (update-player [this _ _] this)) ; never change

(defrecord Mean [last-win] ; last thing that won for me
  Player
  (choose [_]
    (if last-win  ; play last-win or random
      last-win
      (random-choice)))
  (update-player [_ me you]
    ;; reuse last choice, or switch to random
    (->Mean (when (i-won? me you) me))))
;; @@

;; **
;;; ## `deftype`
;;; 
;;; `deftype` is used for (usually) advanced use cases where you want a new type with custom behavior. `deftype` looks like `defrecord` but provides *no* default behavior. Additionally, fields can be mutable (DANGER!).
;;; 
;;; 
;; **

;; @@
(deftype Point [x y])  ; named type with fields
;;=> user.Point

(def p (->Point 1 2))  ; constructor
;;=> #'user/p          ; (but no map->Point)

(.-x p)                ; ordinary field access
;;=> 1                 ; (but no keyword lookup)

(class p)              ; ordinary class
;;=> user.Point

(supers (class p))     ; an (almost) blank slate
;;=> #{java.lang.Object clojure.lang.IType}
;; @@

;; **
;;; # LAB: Rock, Paper, Scissors
;;; 
;;; In this lab, we will write programs to play the classic game of [Rock, Paper, Scissors](http://en.wikipedia.org/wiki/Rock-paper-scissors). The rules are simple:
;;; 
;;; * Rock beats Scissors
;;; * Scissors beats Paper
;;; * Paper beats Rock
;;; 
;;; ## World domination 
;;; 
;;; Define a function dominates that takes a keyword argument and returns the keyword naming the thing that beats it.
;;; 
;;; Hint: remember that data structures are functions.
;;; 
;;; ## Choices, choices
;;; 
;;; Define a vector of the possible choices, reusing the definition of dominates.
;;; 
;;; Hint: the `keys` function returns a sequence of the keys in a map.
;;; 
;;; ## Winners and losers
;;; 
;;; Define a function winner that takes two players' choices and returns the winner, or nil for a tie.
;;; 
;;; ## Draws and ties 
;;; 
;;; Define two predicates:
;;; 
;;; * `draw?` takes two players' choices and returns true if they are a draw
;;; * `iwon?` takes two players' choices and returns true if the first player won
;;; 
;;; ## The players
;;; 
;;; All the players will conform to a `Player` protocol. It should have two methods:
;;; 
;;; * `choose` takes a player and returns that player's choice
;;; * `update-player` takes a player, that player's last choice, and the other player's last choice, returning a new `Player` for the next round
;;; 
;;; Define the `Player` protocol.
;;; 
;;; ## Random player
;;; 
;;; Define a `Random` player who always picks at random and never changes strategy based on what the other player is doing.
;;; 
;;; Hint: Clojure's `rand-nth` function picks a random element from a collection.
;;; 
;;; ## Stubborn player
;;; 
;;; Define a Stubborn player who is initialized with a choice and sticks with it no matter what.
;;; 
;;; ## Mean player
;;; 
;;; Define a Mean player, who is slightly more subtle. The mean player sticks with what worked last time if it won, or plays at random following a loss.
;;; 
;;; ## Playing a game
;;; 
;;; Define a `game` function with three arguments: two players and a number of rounds. The game should return the two player's scores in a map.
;;; 
;;; This can be nicely represented as a `loop` with five variables:
;;; 
;;; 1. Player One
;;; 2. Player Two
;;; 3. Player One's current score
;;; 4. Player Two's current score
;;; 5. The number of rounds remaining
;;; 
;;; Try some games with various combinations of players. Do the results match your intuition?
;;; 
;;; Examples:
;; **

;; @@
(game (->Stubborn :rock) (->Stubborn :scissors) 100)
;;=> {:p1 100, :p2 0}

(game (->Random) (->Random) 100)
;;=> {:p1 34, :p2 25}

(game (->Stubborn :rock) (->Mean nil) 100)
;;=> {:p1 5, :p2 93}
;; @@

;; **
;;; # LAB SOLUTIONS
;;; 
;;; ## World domination
;; **

;; @@
(def dominates
  {:rock :paper
   :scissors :rock
   :paper :scissors})
;; @@

;; **
;;; ## Choices, choices
;; **

;; @@
(def choices (vec (keys dominates)))
;; @@

;; **
;;; ## Winners and losers
;; **

;; @@
(defn winner [p1-choice p2-choice]
  (cond
   (= p1-choice p2-choice) nil
   (= (dominates p1-choice) p2-choice) p2-choice
   :else p1-choice))
;; @@

;; **
;;; ## Draws and ties
;; **

;; @@
(defn draw? [me you] (= me you))

(defn iwon? [me you] (= (winner me you) me))
;; @@

;; **
;;; ## The players
;; **

;; @@
(defprotocol Player
  (choose [p])
  (update-player [p me you]))
;; @@

;; **
;;; ## Random player
;; **

;; @@
(defrecord Random []
  Player
  (choose [_] (rand-nth choices))
  (update-player [this me you] this))
;; @@

;; **
;;; ## Stubborn player
;; **

;; @@
(defrecord Stubborn [choice]
  Player
  (choose [_] choice)
  (update-player [this me you] this))
;; @@

;; **
;;; ## Mean player
;; **

;; @@
(defrecord Mean [last-winner]
  Player
  (choose [_]
    (if last-winner last-winner (rand-nth choices)))
  (update-player [_ me you]
    (->Mean (when (iwon? me you) me))))
;; @@

;; **
;;; ## Playing a game
;; **

;; @@
(defn game
  [p1 p2 rounds]
  (loop [p1 p1
         p2 p2
         p1-score 0
         p2-score 0
         rounds rounds]
    (if (pos? rounds)
      (let [p1-choice (choose p1)
            p2-choice (choose p2)
            result (winner p1-choice p2-choice)]
        (recur
         (update-player p1 p1-choice p2-choice)
         (update-player p2 p2-choice p1-choice)
         (+ p1-score (if (= result p1-choice) 1 0))
         (+ p2-score (if (= result p2-choice) 1 0))
         (dec rounds)))
      {:p1 p1-score :p2 p2-score})))
;; @@

;; **
;;; 
;; **
