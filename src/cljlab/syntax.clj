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
;;; Clojure literals also include syntax for four collections that can be used to combine Clojure expressions:
;; **

;; @@
'(1 2 3)     ; list 
[1 2 3]      ; vector
#{1 2 3}     ; set
{:a 1, :b 2} ; map
;; @@

;; **
;;; We'll talk about these in much greater detail later - for now it's enough to know that these four data structures can be used to represent composite data.
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
;;; 1. The unit of source code is a Clojure expression, not a Clojure source file. Source files are read as a series of expressions.
;;; 2. Separating the Reader and the Compiler is a key step that allows for the creation of macros. Macros are code that operates on code as data, creating new code. Can you see where the second evaluation model provides a place for this to occur?
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
;;; 
;;; 
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
;;; This web page is providing a specialized read and print interface, but otherwise is interacting with Clojure just like any other REPL.
;; **

;; **
;;; 
;;; 
;; **
