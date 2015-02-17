;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Syntax
;;; 
;;; ## Literals
;;; 
;;; Below are some examples of literal representations of common primitives in Clojure. 
;;; 
;;; The ";" creates a comment to the end of the line. Sometimes multiple semicolons are used to indicate header lines.
;; **

;; @@
;; Numeric types
42		  ; Long - 64-bit integer (from -2^63 to 2^63-1)
6.022e23  ; Double - double-precision 64-bit IEEE 754 floating point
42N       ; BigInt - arbitrary precision integer
1.0M      ; BigDecimal - arbitrary precision fixed-point decimal
22/7      ; Ratio

;; Character types
"hello"    ; String
\e         ; Character

;; Other types
nil        ; null value
true       ; Boolean (also, false)
#"[0-9]+"  ; Regular expression
:alpha          ; Keyword
:release/alpha  ; Keyword with namespace
map             ; Symbol
+               ; Symbol - most punctuation allowed
clojure.core/+  ; Namespaced symbol
;; @@

;; **
;;; All of the literals above are valid Clojure expressions.
;;; 
;;; Clojure also includes literal syntax for four collections that can be used to combine Clojure expressions:
;; **

;; @@
'(1 2 3)     ; list 
[1 2 3]      ; vector
#{1 2 3}     ; set
{:a 1, :b 2} ; map
;; @@

;; **
;;; We'll talk about these in much greater detail later - for now it's enough to know that these four data structures can be used to create composite data.
;; **

;; **
;;; ## Evaluation 
;; **

;; **
;;; Next we will consider how Clojure reads and evaluates expressions.
;;; 
;;; ### Traditional Evaluation (Java)
;;; 
;;; ![Traditional evaluation](project-files/images/traditional-evaluation.png)
;;; 
;;; In Java, source code (.java files) are read as characters by the compiler (javac), which produces bytecode (.class files) which can be loaded by the JVM.
;;; 
;;; ### Clojure Evaluation
;;; 
;;; ![Clojure evaluation](project-files/images/clojure-evaluation.png)
;;; 
;;; In Clojure, source code is read as characters by the Reader. The Reader may read the source either from .clj files or be given a series of expressions interactively. The Reader produces Clojure data. The Clojure compiler then produces the bytecode for the JVM. 
;;; 
;;; There are two important points here:
;;; 1. The unit of source code is a Clojure expression, not a Clojure source file. Source files are read as a series of expressions or individual expressions may be sent interactively.
;;; 2. Separating the Reader and the Compiler is a key feature that allows for macros. Macros take code as data, and emit code as data. Can you see where a loop for macro expansion could be inserted in the evaluation model?
;; **

;; **
;;; ### Structure vs Semantics
;;; 
;;; Consider a Clojure expression: 
;;; 
;;; ![Structure vs semantics](project-files/images/structure-and-semantics.png)
;;; 
;;; This diagram illustrates the difference between syntax in green (the Clojure data structure produced by the Reader) and semantics in blue (how that data is understood by the Clojure runtime).
;; **

;; **
;;; Most literal Clojure forms evaluate to themselves, *except* symbols and lists. Symbols are used to refer to something else and when evaluated, return what they refer to. Lists (as in the diagram) are evaluated as invocation.
;;; 
;;; In the diagram, (+ 3 4) is read as a list containing the symbol (+) and two numbers (3 and 4). The first element (where + is found) can be called "function position", that is, a place to find the thing to invoke. While functions are an obvious thing to invoke, there are also a few special operators known to the runtime, macros, and a handful of other invokable things.
;;; 
;;; Considering the evaluation of the expression above:
;;; * 3 and 4 evaluate to themselves (longs)
;;; * `+` evaluates to a function that implements +
;;; * evaluating the list will invoke the + function with 3 and 4 as arguments
;;; 
;;; Many languages have both statements and expressions, where statements have some stateful effect but do not return a value. In Clojure, everything is an expression that evaluates to a value. Some expressions (but not most) also have side effects.
;; **

;; **
;;; 
;; **

;; **
;;; 
;; **

;; **
;;; Now let's consider how we can interactively evaluate expressions in Clojure.
;; **

;; **
;;; ## REPL
;;; 
;;; Most of the time when you are using Clojure, you will do so in an editor or a REPL (Read-Eval-Print-Loop). The REPL has the following parts:
;;; 
;;; 1. Read an expression (a string of characters) to produce Clojure data.
;;; 2. Evaluate the data returned from #1 to yield a result (also Clojure data).
;;; 3. Print the result by converting it from data back to characters.
;;; 4. Loop back to the beginning.
;;; 
;;; One important aspect of #2 is that Clojure always compiles the expression before executing it; Clojure is never interpreted, always compiled first.
;; **

;; @@
(+ 3 4)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}
;; <=

;; **
;;; The box above demonstrates evaluating an expression (+ 3 4) and receiving a result. In this workshop, you will evaluate expressions directly in this web page. Each expression is editable and can be evaluated by pressing Shift-Enter.
;;; 
;;; This web page is providing a bunch of web goo between your browser and the Clojure runtime, but otherwise is operating just like any other Clojure REPL.
;; **

;; **
;;; ### Exploring at the REPL
;;; 
;;; Most REPL environments support a few tricks to help with interactive use. For example, some special symbols remember the results of evaluating the last three expressions: 
;;; 
;;; * `*1` (the last result)
;;; * `*2` (the result two expressions ago)
;;; * `*3` (the result three expressions ago)
;; **

;; @@
(+ 3 4)     
(+ 10 *1)   ;; *1 = 7
(+ *1 *2)   ;; *1 = 17, *2 = 7
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>24</span>","value":"24"}
;; <=

;; **
;;; In addition, there is a namespace `clojure.repl` that is included in the standard Clojure library that provides a number of helpful functions. To load that library and make it's functions available in our current context, call:
;; **

;; @@
(require '[clojure.repl :refer :all])
;; @@

;; **
;;; For now, you can treat that as a magic incantation. Poof! We'll unpack it when we get to namespaces. 
;;; 
;;; We now have access to some additional functions that are useful at the REPL: `doc`, `find-doc`, `apropos`, `source`, and `dir`.
;;; 
;;; The `doc` function displays the documentation for any function. Let's call it on `+`:
;; **

;; @@
(doc +)
;; @@
;; ->
;;; -------------------------
;;; clojure.core/+
;;; ([] [x] [x y] [x y &amp; more])
;;;   Returns the sum of nums. (+) returns 0. Does not auto-promote
;;;   longs, will throw on overflow. See also: +&#x27;
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; The `doc` function prints the documentation for `+`, including the valid signatures. 
;;; 
;;; The doc function prints the documentation, then returns nil as the result - you will see both in the evaluation output.
;;; 
;;; We can invoke `doc` on itself too:
;; **

;; @@
(doc doc)
;; @@
;; ->
;;; -------------------------
;;; clojure.repl/doc
;;; ([name])
;;; Macro
;;;   Prints documentation for a var or special form given its name
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; Not sure what something is called? You can use the `apropos` command to find functions that match a particular string or regular expression.
;; **

;; @@
(apropos "+")
;; @@
;; =>
;;; {"type":"list-like","open":"<span class='clj-list'>(</span>","close":"<span class='clj-list'>)</span>","separator":" ","items":[{"type":"html","content":"<span class='clj-symbol'>clojure.core/+</span>","value":"clojure.core/+"},{"type":"html","content":"<span class='clj-symbol'>clojure.core/+&#x27;</span>","value":"clojure.core/+'"}],"value":"(clojure.core/+ clojure.core/+')"}
;; <=

;; **
;;; You can also widen your search to include the docstrings themselves with `find-doc`:
;; **

;; @@
(find-doc "trim")
;; @@
;; ->
;;; -------------------------
;;; clojure.core/subvec
;;; ([v start] [v start end])
;;;   Returns a persistent vector of the items in vector from
;;;   start (inclusive) to end (exclusive).  If end is not supplied,
;;;   defaults to (count vector). This operation is O(1) and very fast, as
;;;   the resulting vector shares structure with the original and no
;;;   trimming is done.
;;; -------------------------
;;; clojure.string/trim
;;; ([s])
;;;   Removes whitespace from both ends of string.
;;; -------------------------
;;; clojure.string/trim-newline
;;; ([s])
;;;   Removes all trailing newline \n or return \r characters from
;;;   string.  Similar to Perl&#x27;s chomp.
;;; -------------------------
;;; clojure.string/triml
;;; ([s])
;;;   Removes whitespace from the left side of string.
;;; -------------------------
;;; clojure.string/trimr
;;; ([s])
;;;   Removes whitespace from the right side of string.
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; If you'd like to see a full listing of the functions in a particular namespace, you can use the `dir` function. Here we can use it on the `clojure.repl` namespace:
;; **

;; @@
(dir clojure.repl)
;; @@
;; ->
;;; apropos
;;; demunge
;;; dir
;;; dir-fn
;;; doc
;;; find-doc
;;; pst
;;; root-cause
;;; set-break-handler!
;;; source
;;; source-fn
;;; stack-element-str
;;; thread-stopper
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; And finally, we can see not only the documentation but the underlying source for any function accessible by the runtime:
;; **

;; @@
(source dir)
;; @@
;; ->
;;; (defmacro dir
;;;   &quot;Prints a sorted directory of public vars in a namespace&quot;
;;;   [nsname]
;;;   `(doseq [v# (dir-fn &#x27;~nsname)]
;;;      (println v#)))
;;; 
;; <-
;; =>
;;; {"type":"html","content":"<span class='clj-nil'>nil</span>","value":"nil"}
;; <=

;; **
;;; As you go through this workshop, please feel free to examine the docstring and source for the functions you are using. Exploring the implementation of Clojure itself is an excellent way to learn more about the language and how it is used.
;;; 
;;; It is also an excellent idea to keep a copy of the [Clojure Cheatsheet](http://clojure.org/cheatsheet) open while you are learning Clojure. The cheatsheet categorizes the functions available in the standard library and is an invaluable reference.
;;; 
;;; Now let's consider some Clojure basics to get you going....
;; **

;; **
;;; ## Clojure basics
;;; 
;;; ### Vars
;;; 
;;; ### Printing
;;; 
;;; 
;; **
