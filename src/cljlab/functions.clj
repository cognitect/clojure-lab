;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Functions
;;; 
;;; Clojure is a functional language. Functions are first-class and can be passed-to or returned-from other functions. Most Clojure code consists primarily of pure functions (no side effects), so invoking with the same inputs yields the same output.
;;; 
;;; ## Creating Functions
;;; 
;;; `defn` defines a named function.
;;; 
;; **

;; @@
;;    name   params         body
;;    -----  ------  -------------------
(defn greet  [name]  (str "Hello, " name) )
;; @@

;; **
;;; This function has a single parameter `name`, however you may include any number of arguments in the params vector.
;;; 
;;; Invoke a function with the name of the function in "function position" (the first element of a list):
;; **

;; @@
(greet "students")
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-string'>&quot;Hello, students&quot;</span>","value":"\"Hello, students\""}
;; <=

;; **
;;; ### Multi-arity functions
;;; 
;;; Functions can be defined to take different numbers of arguments (different "arity"). Different arities must all be defined in the same `defn` - using `defn` more than once will replace the previous function.
;;; 
;;; Each arity is a list `([args*] body*)`. One arity can invoke another.
;; **

;; @@
(defn messenger
  ([]     (messenger "Hello world!"))
  ([msg]  (println msg)))
;; @@

;; **
;;; 
;; **

;; **
;;; This function declares two arities (0 arguments and 1 argument). The 0-argument arity calls the 1-argument arity with a default value to print. We invoke these functions by passing the appropriate number of arguments:
;; **

;; @@
(messenger)
;; @@
;; ->
;;; Hello world!
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; @@
(messenger "Hello class!")
;; @@
;; ->
;;; Hello class!
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; ### Variadic functions
;;; 
;;; Functions may also define a variable number of arguments - this is known as a "variadic" function. The variable arguments must occur at the end of the argument list. They will be collected in a sequence for use by the function. 
;;; 
;;; The beginning of the variable arguments is marked with `&`.
;; **

;; @@
(defn hello [greeting & who]
  (println greeting who))
;; @@

;; **
;;; This function takes an argument `greeting` and a variable number of arguments (0 or more) that will be collected in a list named `who`. We can see this by invoking it with 3 arguments:
;; **

;; @@
(hello "Hello" "world" "class")
;; @@
;; ->
;;; Hello (world class)
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; You can see that when `println` prints `who`, it is printed as a list of two elements that were collected.
;;; 
;;; ### Anonymous Functions
;;; 
;;; An anonymous function can be created with `fn`:
;; **

;; @@
;;    params         body
;;   ---------  -----------------
(fn  [message]  (println message) )
;; @@

;; **
;;; Because the anonymous function has no name, it cannot be referred to later. Rather, the anonymous function is typically created at the point it is passed to another function.
;;; 
;;; Or it's possible to immediately invoke it (this is not a common usage):
;; **

;; @@
;;     operation (function)             argument
;; --------------------------------  --------------
(  (fn [message] (println message))  "Hello world!" )

;; Hello world!
;; @@

;; **
;;; Here we defined the anonymous function in the function position of a larger expression that immediately invokes the expression with the argument.
;;; 
;;; Many languages have both statements, which imperatively do something and do not return a value, and expressions which do. Clojure has **only** expressions that return a value. We'll see later that this includes even flow control expressions like `if`.
;;; 
;;; ### defn vs fn
;;; 
;;; It might be useful to think of `defn` as a contraction of `def` and `fn`. The `fn` defines the function and the `def` binds it to a name. These are equivalent:
;; **

;; @@
(defn greet [name] (str "Hello, " name))

(def greet (fn [name] (str "Hello, " name)))
;; @@

;; **
;;; ### Anonymous Function Syntax
;;; 
;;; There is a shorter form for the `fn` anonymous function syntax implemented in the Clojure reader: `#()`. This syntax omits the parameter list and names parameters based on their position.
;;; 
;;; * `%` is used for a single argument
;;; * `%1`, `%2`, `%3`, etc are used for multiple arguments
;;; * `%&` is used for any remaining (variadic) arguments
;;; 
;;; Nested anonymous functions would create an ambiguity as the parameters are not named, so nesting is not allowed.
;;; 
;; **

;; @@
;; Equivalent to: (fn [x] (+ 6 x))
#(+ 6 %)

;; Equivalent to: (fn [x y] (+ x y))
#(+ %1 %2)

;; Equivalent to: (fn [x y & zs] (println x y zs))
#(println %1 %2 %&)
;; @@

;; **
;;; ### Gotcha
;;; 
;;; One common need is an anonymous function that takes an element and wraps it in a vector. You might try writing that as:
;;; 
;; **

;; @@
;; DO NOT DO THIS
#([%])
;; @@

;; **
;;; This anonymous function expands to the equivalent:
;; **

;; @@
(fn [x] ([x]))
;; @@

;; **
;;; This form will wrap in a vector **and** try to invoke the vector with no arguments (the extra pair of parentheses). Instead:
;; **

;; @@
;; Instead do this:
#(vector %)

;; or this:
(fn [x] [x])

;; or most simply just the vector function itself:
vector
;; @@

;; **
;;; ## Applying Functions
;;; 
;;; ### `apply`
;;; 
;;; The `apply` function invokes a function with 0 or more fixed arguments, and draw the rest of the needed arguments from a final sequence.
;; **

;; @@
(apply f '(1 2 3 4))    ;; same as  (f 1 2 3 4)
(apply f 1 '(2 3 4))    ;; same as  (f 1 2 3 4)
(apply f 1 2 '(3 4))    ;; same as  (f 1 2 3 4)
(apply f 1 2 3 '(4))    ;; same as  (f 1 2 3 4)
;; @@

;; **
;;; All 4 of these calls are equivalent to `(f 1 2 3 4)`. `apply` is useful when arguments are handed to you as a sequence but you must invoke the function with the values in the sequence.
;;; 
;;; For example, you can use `apply` to avoid writing this:
;; **

;; @@
(defn plot [shape coords]   ;; coords is [x y]
  (plotxy shape (first coords) (second coords)))
;; @@

;; **
;;; Instead you can simply write:
;; **

;; @@
(defn plot [shape coords]
  (apply plotxy shape coords))
;; @@

;; **
;;; ### `apply` and Variadics
;;; 
;; **
