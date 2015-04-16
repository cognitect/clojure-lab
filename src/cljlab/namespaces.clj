;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure Namespaces
;;; 
;;; 
;; **

;; **
;;; ## What is a namespace?
;;; * Conceptual version
;;; 	* A way to disambiguate names
;;; * Concrete version
;;; 	* Mappings from _symbols_ to
;;;     	* Vars
;;;         * Java classes
;;;         * Aliases to other namespaces
;;;         
;;; ## What is a var?
;;; * An association between a _symbol_ and a _value_
;;; * `def`
;;; 	* Creates a new var in the current namespace
;;;     * Optionally creates _root binding_ to a value
;;; * `defn`
;;; 	* Shortcut for `def` + `fn`
;;;     * Creates a new var whose value is a function
;;;     
;;; ## What is a symbol?
;;; * Just a label
;;; 	* A name _string_
;;;     * With an optional namespace _string_
;;; * Has no value
;;; 
;;; ```clojure
;;; foo		; Unqualified symbol
;;; bar/foo ; Namespace-qualified symbol
;;; ```
;;; 
;;; ## Creating namespaces
;;; ### `ns`
;;; * At the top of every source file
;;; * Creates a namespace
;;; * Automatically refers all of `clojure.core`
;;; 
;;; ```clojure
;;; (ns com.some-example.my-app)
;;; ```
;;; 
;;; ### Names into files
;;; * Dots become directory separators
;;; * Hyphens become underscores
;;; * Add `.clj` to the end
;;; * Find on the CLASSPATH
;;; 
;;; ```clojure
;;; ;; in file $CLASSPATH/com/some_example/my_app.clj
;;; (ns com.some-example.my-app)
;;; ```
;;; 
;;; ### The JVM classpath
;;; * Where to find code to load
;;; * List of directories and JAR files
;;; * Specified when launching the JVM
;;; 	* Cannot be changed (normally)
;;; * Managed by tools such as Leiningen
;;; 
;;; ```clojure
;;; ;; Show the classpath
;;; (System/getProperty "java.class.path")
;;; ```
;;; 
;;; ## Namespace operations
;;; * Load (_require_)
;;; * Copy (_refer_) symbol -> var mappings
;;; * Make a shortcut (_alias_)
;;; 
;;; ### `ns :require`
;;; * Loads a namespace
;;; * Optionally
;;; 	* Provides an alias
;;;     * Refers some symbols
;;; * Never loads the same code twice
;;; 
;;; ### `ns :require` example
;;; ```clojure
;;; (ns com.some-example.my-app
;;; 	(:require clojure.string
;;;     		  [clojure.set :as set]
;;;               [clojure.java.io :refer (file reader)]))
;;; ```
;;; 
;;; ### `ns :require` bare
;;; ```clojure
;;; (ns ... (:require clojure.string ...))
;;; ```
;;; 
;;; * Element inside `(:require ...)` is a symbol
;;; * Just load the code
;;; * Vars available as fully-qualified symbols
;;; 	* e.g. `clojure.string/replace`
;;;     
;;; ### `ns :require :as`
;;; ```clojure
;;; (ns ... (:require [clojure.set :as set] ...))
;;; ```
;;; 
;;; * Element inside `(:require ...)` is a vector
;;; 	* Like `[_name_ :as _alias_]`
;;; * Load code and create an alias
;;; * Vars available under shorter alias
;;; 	* e.g. `set/union`
;;;     
;;; ### `ns :require :refer`
;;; ```clojure
;;; (ns ... (:require [clojure.java.io :refer (file reader)] ...))
;;; ```
;;; 
;;; * Element inside `(:require ...)` is a vector
;;; 	* Like `[_name_ :refer (_symbols_+)]`
;;; * Load code and copy symbol -> var bindings
;;; * Vars available without namespace qualification
;;; 	* e.g. `reader`
;;;     
;;; ### `ns :use :only`
;;; ```clojure
;;; (ns ... (:use [clojure.java.io :only (file reader)] ...))
;;; ```
;;; 
;;; * Older (pre-1.4) form of `:require :refer`
;;; * `(:use ...)` instead of `(:require ...)`
;;; * Element inside `(:use ...)` is a vector
;;; 	* Like `[_name_ :only (_symbols_+)]`
;;; * Vars available without namespace qualification
;;; 	* e.g. `reader`
;;;     
;;; ### `ns :import`
;;; ```clojure
;;; (ns com.some-example.my-app
;;; 	(:import java.io.File
;;;     		 (java.util Map List Map$Entry)))
;;; ```
;;; 
;;; * Element inside `(:import ...)` is
;;; 	* A package-qualified class name
;;;     * Or a list like `(_package_ _classes_+)`
;;; * Java "inner classes" are named `Outer$Inner`
;;; * Every namespace automatically imports `java.lang`
;;; 
;;; ## Namespaces at the REPL
;;; ### Where am I?
;;; * `*ns*` is the current namespace
;;; * `in-ns` switches namespaces
;;; 	* Takes a _symbol_ (quoted)
;;;     * Creates namespace if it doesn't exist
;;;     * Does _not_ automatically refer `clojure.core` like `ns`
;;;     
;;; ### `in-ns`
;; **

;; @@
(ns cljlab.namespaces)
;; @@

;; @@
(in-ns 'never-before-seen)
;; @@

;; @@
(println "This won't work")
;; @@

;; @@
(clojure.core/println "This will")
;; @@

;; @@
;; Get back to the namespace for this worksheet
(in-ns 'cljlab.namespaces)
;; @@

;; **
;;; ### `require`
;;; * `require` is also a function
;;; * Same syntax as in `(ns ... (:require ...))`
;;; 	* But `require` is not a keyword
;;;     * Arguments must be _quoted_ to prevent evaluation
;; **

;; @@
(require clojure.set)
;; @@

;; @@
(require '[clojure.set :as set])
;; @@

;; @@
(set/union #{1 3} #{2 4})
;; @@

;; **
;;; ### `require :reload`
;;; * `require` never loads the same file twice
;;; * To get new definitions, add `:reload` flag
;;; * `:reload-all` will recursively reload dependencies
;;; 
;;; ```clojure
;;; (require 'com.example :reload)
;;; (require 'com.example.myapp :reload-all)
;;; ```
;;; 
;;; ### `import`
;;; * `import` is also a macro
;;; * Same syntax as in `(ns ... (:import ...))`
;;; 	* But `import` is not a keyword
;;;     * Arguments do _not_ need to be quoted
;; **

;; @@
(import (java.util Random))
;; @@

;; @@
(.nextInt (Random.))
;; @@

;; **
;;; ### Namespace concepts
;;; Click on the image to view full-size in a new window (in browsers that support SVG).
;;; ![Namespace concepts](project-files/images/namespaces-total.svg)
;;; 
;;; ### File vs. REPL
;;; ```clojure
;;; ;; In a source file					;; At the REPL
;;; ;; ---------------------------  	;; ---------------------------
;;; (ns foo)							(in-ns 'foo)
;;; 
;;; (ns foo (:require bar))				(require 'bar)
;;; (ns foo (:require [bar :as b]))		(require '[bar :as b])
;;; (ns foo (:import (java.io File)))	(import (java.io File))
;;; ```
;;; 
;;; ## Examining symbols and namespaces
;;; ### Examining and creating symbols
;; **

;; @@
(name 'com.example/foo)
;; @@

;; @@
(namespace 'com.example/foo)
;; @@

;; @@
(namespace 'foo)
;; @@

;; @@
(symbol "foo")
;; @@

;; @@
(symbol "com.example" "foo")
;; @@

;; **
;;; ### Finding namespaces
;; **

;; @@
;; Doesn't work:
clojure.set
;; @@

;; @@
(find-ns 'clojure-set)
;; @@

;; **
;;; ### Viewing namespace mappings
;;; <table>
;;; <tr>
;;; <th>Function</th>
;;; <th>Returns map of symbols to...</th>
;;; </tr>
;;; <tr>
;;; <td>`ns-map`</td>
;;; <td>all vars and classes</td>
;;; </tr>
;;; <tr>
;;; <td>-> `ns-interns`</td>
;;; <td>all vars created in this namespace</td>
;;; </tr>
;;; <tr>
;;; <td>-> -> `ns-publics`</td>
;;; <td>all *public* vars in this namespace</td>
;;; </tr>
;;; <tr>
;;; <td>-> `ns-refers`</td>
;;; <td>*referred* vars from other namespaces</td>
;;; </tr>
;;; <tr>
;;; <td>-> `ns-imports`</td>
;;; <td>Java classes</td>
;;; </tr>
;;; <tr>
;;; <td>`ns-aliases`</td>
;;; <td>aliased namespaces</td>
;;; </tr>
;;; </table>
;;; 
;;; ### Viewing namespace mappings example
;; **

;; @@
(ns-map 'user)
;; @@

;; @@
(ns-aliases 'user)
;; @@

;; **
;;; ## Private vars
;;; * Add `^:private` metadata to def'd symbol
;;; 	* `defn-` is shortcut for private fn
;;; * Prevents accidental refer
;;; * Prevents accidental use by qualified symbol
;;; * Not truly hidden, can expose by deref'ing var
;;; 
;;; ### Private vars example
;; **

;; @@
(def ^:private secret "trustno1")
;; @@

;; @@
(in-ns 'foo.bar)
;; @@

;; @@
(user/secret)
;; @@

;; @@
@#'user/secret
;; @@

;; @@
;; Return to the namespace for this worksheet
(in-ns 'cljlab.namespaces)
;; @@

;; **
;;; ## LAB: Namespaces
;;; 
;;; ### Entering a namespace
;;; At the REPL, use `in-ns` to enter a new namespace `alpha`.
;; **

;; @@
;; SOLUTION
(in-ns 'alpha)
;; @@

;; **
;;; ### An empty namespace
;;; In the `alpha` namespace, try to call a common function like `println`. Why doesn't it work?
;; **

;; @@
;; SOLUTION
(println "Hello, World!")
;; @@

;; **
;;; The `println` function is defined in the `clojure.core` namespace, which is not automatically referred into new namespaces by `in-ns`.
;; **

;; **
;;; ### Getting to the core
;;; In the `alpha` namespace, call the `println` function correctly.
;; **

;; @@
;; SOLUTION
(clojure.core/println "Hello, World!")
;; @@

;; **
;;; ### A less empty namespace
;;; Create a new namespace `beta` using `ns`. Try calling a common function like `println`. Why does it work now?
;; **

;; @@
;; SOLUTION
(ns beta)
;; @@

;; @@
(println "Hello, World!")
;; @@

;; **
;;; `ns` automatically refers all symbols from `clojure.core` into the new namespace.
;; **

;; **
;;; ### File it away
;;; Create a new Clojure source file for the namespace `student.dialect`, with the appropriate `ns` declaration.
;;; 
;;; In this file, define a function canadianize that takes a string and appends ", eh?"
;; **

;; **
;;; ```clojure
;;; ;; SOLUTION
;;; ;; in $CLASSPATH/student/dialect.clj
;;; (ns student.dialect)
;;; 
;;; (defn canadianize [sentence]
;;;   (str sentence ", eh?"))
;;; ```
;; **

;; **
;;; ### Loading from files
;;; In the REPL below, load your new namespace and call the `canadianize` function.
;; **

;; @@
;; Enter your solution here
;; @@

;; @@
;; SOLUTION
(require 'student.dialect)
;; @@

;; @@
;; SOLUTION
(student.dialect/canadianze "Nice weather today.")
;; @@

;; **
;;; ### Modifying sources
;;; Modify your source file to make the `clojure.string` namespace available under the alias `str` in the `student.dialect` namespace.
;;; 
;;; Modify the `canadianize` function to strip a trailing period off its input. Use `clojure.string/replace`.
;; **

;; **
;;; ```clojure
;;; ;; SOLUTION
;;; ;; in $CLASSPATH/student/dialect.clj
;;; (ns student.dialect
;;; 	(:require [clojure.string :as str]))
;;;     
;;; (defn canadianize
;;; 	[sentence]
;;;     (str/replace sentence #"\.$" ", eh?"))
;;; ```
;; **

;; **
;;; ### Reloading
;;; In the REPL below, reload your source file and call the improved `canadianize` function.
;; **

;; @@
;; Enter your solution here
;; @@

;; @@
;; SOLUTION
(require 'student.dialect :reload)
;; @@

;; @@
;; SOLUTION
(student.dialect/canadianize "Nice weather today.")
;; @@

;; **
;;; 
;;; ## Navigation
;;; 
;;; * [Up (Home)](/worksheet.html?filename=src/cljlab/start.clj)
;;; * [Previous (Flow Control)](/worksheet.html?filename=src/cljlab/flowcontrol.clj)
;;; * [Next (Sequences)](/worksheet.html?filename=src/cljlab/sequences.clj)
;; **
