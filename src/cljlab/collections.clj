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
;;; You can get the same outcome with into:
;; **

;; @@
(into {} (map #([% 0])))
;; @@

;; **
;;; 
;; **

;; **
;;; 
;; **
