;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Collections
;;; 
;;; 
;; **

;; @@
(ns cljlab.collections
  (:require [gorilla-plot.core :as plot]))
;; @@

;; **
;;; Clojure collections "collect" values into compound values. There are four key Clojure collection types: lists, vectors, sets, and maps.
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
;;; All Clojure collections can be counted:
;; **

;; @@
(count [1 2 3])
;; @@

;; **
;;; In addition to using the literal `[ ]` syntax, Clojure collections can be created with the `vector` function:
;; **

;; @@
(vector 1 2 3)
;; @@

;; **
;;; Elements are added to a vector with `conj` (short for conjoin). Elements are always added to a vector at the end:
;; **

;; @@
(conj [1 2 3] 4 5 6)
;; @@

;; **
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
;;; Let's move on to examine lists.
;;; 
;;; ## Lists
;;; 
;;; 
;; **
