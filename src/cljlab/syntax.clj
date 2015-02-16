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
;;; All of the literals above are valid Clojure expressions. We'll discuss later some of the less obvious literals above. 
;;; 
;;; For now, try evaluating the block above with <shift-enter> and examine the results. You might notice that each of the literals above evaluates to itself, *except* symbols! Symbols are used to refer to something else. They evaluate to the thing to which they refer, instead of themselves.
;;; 
;;; Most Clojure code is made up of combinations of these literals grouped in parentheses, which form a list:
;; **

;; @@
(+ 3 4)
;; @@
;; =>
;;; {"type":"html","content":"<span class='clj-long'>7</span>","value":"7"}
;; <=

;; **
;;; The box above demonstrates evaluating an expression (+ 3 4) and receiving a result. In this workshop, you will evaluate expressions directly in this web page. 
;;; 
;;; ## REPL
;;; 
;;; However, most of the time when you are using Clojure, you will do so in an editor or a REPL (Read-Eval-Print-Loop). The REPL has the following parts:
;;; 
;;; 1. Read an expression (a string of characters) and return Clojure data.
;;; 2. Evaluate the data returned from #1 to yield a result (also Clojure data).
;;; 3. Print the result by converting it from data back to characters.
;;; 4. Loop back to the beginning.
;; **

;; **
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
;;; In Clojure, source code is read as characters by the Reader. The reader may read the source either from .clj files or straight from the REPL. The Reader produces Clojure data. The Clojure compiler then produces the bytecode for the JVM. 
;;; 
;;; There are two important points here:
;;; 1. The unit of source code is a Clojure expression, not a Clojure source file. Source files are read as a series of expressions.
;;; 2. Separating the Reader and the Compiler is a key step that allows for the creation of macros. Macros are code that operates on code as data, creating new code. Can you see where the second evaluation model provides a place for this to occur?
;;; 
;; **

;; **
;;; 
;;; 
;; **
