;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Collections
;;; 
;;; 
;; **

;; **
;;; Clojure collections "collect" values into compound values. There are four key Clojure collection types: vectors, lists, sets, and maps.
;;; 
;;; ## Vectors
;;; 
;;; Vectors are an indexed, sequential data structure. Vectors are represented with `[ ]` like this:
;;; 
;; **

;; @@
[1 2 3]
;; @@

;; **
;;; ### Indexed access
;;; 
;;; "Indexed" means that elements of a vector can be retrieved by index. In Clojure (as in Java), indexes start at 0, not 1. Use the `get` function to retrieve an element at an index:
;; **

;; @@
(get ["abc" false 99] 0)
;; @@

;; @@
(get ["abc" false 99] 1)
;; @@

;; **
;;; Calling `get` with an invalid index returns nil:
;; **

;; @@
(get ["abc" false 99] 14)
;; @@

;; **
;;; 
;; **

;; **
;;; ### count
;;; 
;;; All Clojure collections can be counted:
;; **

;; @@
(count [1 2 3])
;; @@

;; **
;;; ### Constructing
;;; 
;;; In addition to using the literal `[ ]` syntax, Clojure collections can be created with the `vector` function:
;; **

;; @@
(vector 1 2 3)
;; @@

;; **
;;; ### Adding elements
;;; 
;;; Elements are added to a vector with `conj` (short for conjoin). Elements are always added to a vector at the end:
;; **

;; @@
(conj [1 2 3] 4 5 6)
;; @@

;; **
;;; ### Immutability
;;; 
;;; Clojure collections share important properties of simple values like strings and numbers, such as immutability and equality comparison by value.
;;; 
;;; For example, lets create a vector and modify it with `conj`.
;; **

;; @@
(def v [1 2 3])
(conj v 4 5 6)
;; @@

;; **
;;; Here `conj` returned a new vector but if we examine the original vector, we see it's unchanged:
;; **

;; @@
v
;; @@

;; **
;;; Any function that "changes" a collection returns a new instance. Your program will need to remember or pass along the changed instance to take advantage of it.
;;; 
;;; ## Lists
;;; 
;;; Lists are sequential linked lists that add new elements at the head of the list, instead of at the tail like vectors.
;;; 
;;; ### Constructing
;;; 
;;; Because lists are evaluated by invoking the first element as a function, we must quote a list to prevent evaluation:
;;; 
;; **

;; @@
(def cards '(10 :ace :jack 9))
;; @@

;; **
;;; Lists are not indexed so they must be walked using `first` and `rest`. 
;; **

;; @@
(first cards)  ;; 10
(rest cards)   ;; '(:ace :jack 9)
;; @@

;; **
;;; ### Adding elements
;;; 
;;; `conj` can be used to add elements to a list just as with vectors. However, `conj` always adds elements where it can be done in constant time for the data structure. In the case of lists, elements are added at the front:
;; **

;; @@
(conj cards :queen)
;; (:queen 10 :ace :jack 9)
;; @@

;; **
;;; ### Stack access
;;; 
;;; Lists can also be used as a stack with peek and pop
;; **

;; @@
(def stack '(:a :b))
(peek stack)  ;; :a
(pop stack)   ;; (:b)
;; @@

;; **
;;; ### Uses for lists
;;; 
;;; * Efficient access to the first element
;;; * Efficiently remove the first element (`pop`)
;;; * Preserve a reverse order of inputs (list in, first out)
;;; * Simulating a stack
;; **

;; **
;;; ## Sets
;;; 
;;; Sets are like mathematical sets - unordered and with no duplicates. Sets are ideal for efficiently checking whether a collection contains an element, or to remove any arbitrary element.
;;; 
;;; 
;;; 
;;; 
;; **

;; @@
(def players #{"Alice" "Bob" "Kelly"})
;; @@

;; **
;;; ### Adding to a set
;;; 
;;; As with vectors and lists, `conj` is used to add elements.
;; **

;; @@
(conj players "Una")
;;=> #{"Alice" "Una" "Bob" "Kelly"}
;; @@

;; **
;;; ### Removing from a set
;;; 
;;; The `disj` ("disjoin") function is used to remove one or more elements from a set.
;;; 
;; **

;; @@
(disj players "Bob" "Sal")
;;=> #{"Alice" "Kelly"}
;; @@

;; **
;;; ### Checking containment
;; **

;; @@
(contains? players "Kelly")
;;=> true
;; @@

;; **
;;; ### Sorted sets
;;; 
;;; Sorted according to a comparator function which can compare two elements. By default, Clojure's `compare` function is used, which sorts in "natural" order.
;; **

;; @@
(conj (sorted-set) "Bravo" "Charlie" "Sigma" "Alpha")
;;=> #{"Alpha" "Bravo" "Charlie" "Sigma"}
;; @@

;; **
;;; A custom comparator can also be used with `sorted-set-by`.
;; **

;; **
;;; ### Choosing a Collection
;;; 
;;; | If you need... | Then use... |
;;; |----------------|-------------|
;;; |Preserve input order| vector|
;;; |Lookup by index| vector|
;;; |Last in, first out| list |
;;; |"Does it contain X?" | set |
;;; |Remove item | set |
;;; |Sorted order| sorted-set |
;; **

;; **
;;; ### into
;;; 
;;; Putting one collection into another.
;; **

;; @@
(def players #{"Alice" "Bob" "Kelly"})
(def new-players ["Tim" "Sue" "Grog"])
(into players new-players)
;;=> #{"Alice" "Grog" "Sue" "Bob" "Tim" "Kelly"}
;; @@

;; **
;;; `into` returns a collection of the same type as its first argument.
;; **

;; **
;;; ## Maps
;;; 
;;; Maps are commonly used for two purposes - to represent entities and to manage an association of keys to values. We'll focus on the latter use first. In other language these may be known as dictionaries or hash maps.
;;; 
;;; ### Creating a literal map
;;; 
;;; Maps are represented as alternating keys and values surrounded by `{` and `}`.
;; **

;; @@
(def scores {"Una" 1400
             "Bob" 1240
             "Cid" 1024})
;; @@

;; **
;;; When Clojure prints a map at the REPL, it will put `,`'s between each key/value pair. These are purely used for readability - commas are treated as whitespace in Clojure. Feel free to use them in cases where they help you!
;;; 
;; **

;; @@
;; same as the last one!
(def scores {"Una" 1400, "Bob" 1240, "Cid" 1024})
;; @@

;; **
;;; ### Looking up by key
;;; 
;;; There are several ways to look up a value in a map. The most obvious is the function `get`:
;; **

;; @@
(get scores "Cid")
;;=> 1024
;; @@

;; **
;;; When the map in question is being treated as a constant lookup table, its common to invoke the map itself, treating it as a function:
;; **

;; @@
;; a constant lookup table, perhaps used in a game
(def directions {:north 0
                 :east 1
                 :south 2
                 :west 3})

;; invoke the map as a function
(directions :north)
;;=> 0
;; @@

;; **
;;; You should generally not invoke a map like this if there is any chance the map could be nil:
;; **

;; @@
(def bad-lookup-map nil)

;; What will happen?
(bad-lookup-map :foo)
;; @@

;; **
;;; ### Looking up with a default
;;; 
;;; If you want to do a lookup and fall back to a default value when the key is not found, specify the default as an extra parameter:
;; **

;; @@
(get scores "Samwise" 0)
;;=> 0

(directions :northwest -1)
;;=> -1
;; @@

;; **
;;; Using a default is also helpful to distinguish between a missing key and an existing key with a nil value.
;; **

;; **
;;; ### Checking contains
;;; 
;;; There are two other functions that are helpful in checking whether a map contains an entry.
;; **

;; @@
(contains? scores "Una")
;;=> true

(find scores "Una")
;;=> ["Una" 1400]
;; @@

;; **
;;; The `contains?` function is a predicate for checking containment. The `find` function finds the key/value entry in a map, not just the value.
;; **

;; **
;;; ### Keys or values
;;; 
;;; You can also get just the keys or just the values in a map:
;; **

;; @@
(keys scores)
;;=> ("Una" "Bob" "Cid")

(vals scores)
;;=> (1400 1240 1024)
;; @@

;; **
;;; While maps are unordered, there is a guarantee that keys, vals, and other functions that walk in "sequence" order will always walk a particular map instance entries in the same order.
;; **

;; **
;;; ### Building a map
;;; 
;;; The `zipmap` function can be used to "zip" together two sequences (the keys and vals) into a map:
;; **

;; @@
(def players #{"Alice" "Bob" "Kelly"})

(zipmap players (repeat 0))
;;=> {"Kelly" 0, "Bob" 0, "Alice" 0}
;; @@

;; **
;;; There are a variety of other ways to build up a map using Clojure's sequence functions (which we have not yet discussed). Come back to these later!
;; **

;; @@
;; with map and into
(into {} (map (fn [player] [player 0]) players))

;; with reduce
(reduce (fn [m player]
          (assoc m player 0))
        {} ; initial value
        players)
;; @@

;; **
;;; ### Combining maps
;;; 
;;; The `merge` function can be used to combine multiple maps into a single map:
;; **

;; @@
(def new-scores {"Cid" 300 "Baz" 900})
(merge scores new-scores)

;;=> {"Una" 1400, "Bob" 1240, "Baz" 900, "Cid" 300}
;; @@

;; **
;;; We merged two maps here but you can pass more as well.
;;; 
;;; If both maps contain the same key, the rightmost one wins. Alternately, you can use `merge-with` to supply a function to invoke when there is a conflict:
;; **

;; @@
(def new-scores {"Una" 550 "Cid" 900 "Samwise" 1000})
(merge-with + scores new-scores)

;;=> {"Samwise" 1000, "Una" 1950, "Bob" 1240, "Cid" 1924}
;; @@

;; **
;;; In the case of a conflict, the function is called on both values to get the new value.
;; **

;; **
;;; ### Sorted maps
;;; 
;;; Similar to sorted sets, sorted maps maintain the keys in sorted order based on a comparator, using `compare` as the default comparator function.
;; **

;; @@
(def sm (sorted-map
         "Bravo" 204
         "Alfa" 35
         "Sigma" 99
         "Charlie" 100))

;;=> {"Alfa" 35, "Bravo" 204, "Charlie" 100, "Sigma" 99}

(keys sm)
;;=> ("Alfa" "Bravo" "Charlie" "Sigma")

(vals sm)
;;=> (35 204 100 99)
;; @@

;; **
;;; ## Representing entities
;;; 
;;; When we need to represent many information entities with the same set of fields known in advance, there are two common choices.
;;; 
;;; One common choice is to use a map with keyword keys.
;; **

;; @@
(def person
  {:first-name "Kelly"
   :last-name "Keen"
   :age 32
   :occupation "Programmer"})
;; @@

;; **
;;; ### Field accessors
;;; 
;;; Since this is a map, the ways we've already discussed for looking up a value by key work:
;; **

;; **
;;; 
;; **

;; @@
(get person :occupation)
(person :occupation)
;; @@

;; **
;;; But really, the most common way to get field values for this use is by invoking the kewyord. Just like with maps and sets, keywords are also functions. When a keyword is invoked, it looks itself up in the associative data structure that it was passed.
;; **

;; @@
(:occupation person)
;;=> "Programmer"
;; @@

;; **
;;; Keyword invocation also takes an optional default value:
;; **

;; @@
(:favorite-color person "beige")
;;=> "beige"
;; @@

;; **
;;; ### Updating fields
;;; 
;;; Since this is a map, we can just use assoc to add or modify fields:
;; **

;; @@
(assoc person :occupation "Baker")
;;=> {:age 32, :last-name "Keen", :first-name "Kelly", :occupation "Baker"}
;; @@

;; **
;;; ### Removing a field
;;; 
;;; Use dissoc to remove fields:
;; **

;; @@
(dissoc person :age)
;;=> {:last-name "Keen", :first-name "Kelly", :occupation "Programmer"}
;; @@

;; **
;;; ### Nested entities
;;; 
;;; It is common to see entities nested within other entities:
;; **

;; @@
(def company
  {:name "WidgetCo"
   :address {:street "123 Main St"
             :city "Springfield"
             :state "IL"}})
;; @@

;; **
;;; We can use `get-in` to access fields at any level inside nested entities:
;; **

;; @@
(get-in company [:address :city])
;;=> "Springfield"
;; @@

;; **
;;; We can also use `assoc-in` or `update-in` to modify nested entities:
;; **

;; @@
(assoc-in company [:address :street] "303 Broadway")
;; {:name "WidgetCo",
;;  :address
;;  {:state "IL",
;;   :city "Springfield",
;;   :street "303 Broadway"}}
;; @@

;; **
;;; ### Records
;;; 
;;; An alternative to using maps is to create a "record". Records are designed specifically for this use case and generally have better performance. In addition, they have a named "type" which can be used for polymorphic behavior (more on that later).
;;; 
;;; Records are defined with the list of field names for record instances. These will be treated as keyword keys in each record instance.
;; **

;; @@
;; Define a record structure
(defrecord Person [first-name last-name age occupation])

;; Positional constructor - generated
(def kelly (->Person "Kelly" "Keen" 32 "Programmer"))

;; Map constructor - generated
(def kelly (map->Person
             {:first-name "Kelly"
              :last-name "Keen"
              :age 32
              :occupation "Programmer"}))
;; @@

;; **
;;; Records are used almost exactly the same as maps, with the caveat that they cannot be invoked as a function like maps.
;; **

;; @@
(:occupation kelly)
;;=> "Programmer"
;; @@

;; **
;;; # LAB: Collections
;;; 
;;; **Note:** All lab solutions are in the section following this one if you want to check your answers!
;;; 
;;; ## Building Collections 
;;; 
;;; ### Representing scores in a game
;;; 
;;; What collection would be best to represent a table of scores (represented by a number) for players (represented by a string) in a game?
;; **

;; **
;;; ### Add a new player to the scores
;;; 
;;; Given a scores table:
;; **

;; @@
(def scores {"Una" 1400, "Bob" 1240, "Cid" 1024})
;; @@

;; **
;;; Write an expression that adds a new player "Mel" to the table.
;; **

;; @@

;; @@

;; **
;;; ### Representing a player
;;; 
;;; How would you represent a player with the attributes `name` and `ranking`?
;;; 
;;; 
;; **

;; @@

;; @@

;; **
;;; Given a player representation, what are some ways to retrieve the ranking given a player?
;; **

;; @@

;; @@

;; **
;;; ## Modifying Nested Collections
;;; 
;;; ### Advance to the next round
;;; 
;;; A game's state is represented in a data structure like this:
;; **

;; @@
(def game
  {:round 2
   :players #{{:name "Una" :ranking 43}
              {:name "Bob" :ranking 77}
              {:name "Cid" :ranking 33}}
   :scores {"Una" 1400
            "Bob" 1240
            "Cid" 1024}})
;; @@

;; **
;;; Write a function `next-round` that takes a game and returns it with the round incremented.
;; **

;; @@
(defn next-round [game]
  ___)

;; Example use:

(next-round game)
;; => {:round 3
;;     :players #{{:name "Una" :ranking 43}
;;                {:name "Bob" :ranking 77}
;;                {:name "Cid" :ranking 33}}
;;     :scores {"Una" 1400
;;              "Bob" 1240
;;              "Cid" 1024}}
;; @@

;; **
;;; ### Update the scores
;;; 
;;; Write a function `add-score` that increments a players score by a specified amount.
;; **

;; @@
(defn add-score [game name score]
  ___)

;; Example use:

(add-score game "Cid" 500)
;; => {:round 3
;;     :players #{{:name "Una" :ranking 43}
;;                {:name "Bob" :ranking 77}
;;                {:name "Cid" :ranking 33}}
;;     :scores {"Una" 1400
;;              "Bob" 1240
;;              "Cid" 1524}}

;; @@

;; **
;;; ### Add a new player
;;; 
;;; Write a function `add-player` that adds a new player and initializes their score.
;; **

;; **
;;; 
;; **

;; @@
(defn add-player [game name]
  ___)

;; Example use:

(add-player game "Mel" 24)
;; => {:round 3
;;     :players #{{:name "Una" :ranking 43}
;;                {:name "Bob" :ranking 77}
;;                {:name "Cid" :ranking 33}
;;                {:name "Mel" :ranking 24}}
;;     :scores {"Una" 1400
;;              "Bob" 1240
;;              "Cid" 1524
;;              "Mel" 0}}
;; @@

;; **
;;; ## Map Lookup
;;; 
;;; ### Looking up a player
;;; 
;;; We'd like to be able to look up a player's ranking based on their name. This is currently difficult. What data structure would make this easier?
;; **

;; @@

;; @@

;; **
;;; ### Find a player
;;; 
;;; Using this game definition, how can we implement `find-player` for a game?
;; **

;; @@
(def game
  {:round 2
   :players {"Una" {:name "Una" :ranking 43 :score 1400}
             "Bob" {:name "Bob" :ranking 77 :score 1240}
             "Cid" {:name "Cid" :ranking 33 :score 1024}}})

(defn find-player [game name]
  ___)

;; Example:

(find-player game "Una")
;; => {:name "Una", :score 1400, :ranking 43}
;; @@

;; **
;;; ### Remove a player
;;; 
;;; Write a function `remove-player` that removes a player from a game, including their score.
;; **

;; @@
(defn remove-player [game name]
  ___)
;; @@

;; **
;;; # Lab Solutions
;;; 
;;; ### Add a new player to the scores
;; **

;; @@
(assoc scores "Mel" 0)
;; @@

;; **
;;; ### Representing a player
;;; 
;; **

;; @@
;; as map
{:name "Una" :ranking 43}

;; as record
(defrecord Player [name ranking])

;; define a player
(->Player "Una" 43)
;; @@

;; **
;;; ### Retrieving a player's name
;; **

;; @@
(get player :ranking)
(player :ranking)  ;; will not work for records
(:ranking player)
;; @@

;; **
;;; ### Advance to the next round
;; **

;; @@
(defn next-round [game]
  (update-in game [:round] inc))
;; @@

;; **
;;; ### Update the scores
;;; 
;; **

;; @@
(defn add-score [name score]
  (update-in game [:scores name] + score))
;; @@

;; **
;;; ### Add a new player
;;; 
;; **

;; @@
(defn add-player [game name]
  (update-in (update-in game [:players] conj {:name name})
             [:scores] assoc name 0))
;; @@

;; **
;;; ### Looking up a player
;; **

;; **
;;; A map indexing the players by name.
;; **

;; **
;;; ### Find a player
;; **

;; @@
(defn find-player [game name]
  (get-in game [:players name]))
;; @@

;; **
;;; ### Remove a player
;;; 
;; **

;; @@
(defn remove-player [game name]
  (update-in game [:players] dissoc name))
;; @@

;; **
;;; 
;;; ## Navigation
;;; 
;;; * [Up (Home)](/worksheet.html?filename=src/cljlab/start.clj)
;;; * [Previous (Functions)](/worksheet.html?filename=src/cljlab/functions.clj)
;;; * [Next (Flow Control)](/worksheet.html?filename=src/cljlab/flowcontrol.clj)
;;; 
;; **
