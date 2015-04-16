;; gorilla-repl.fileformat = 1

;; **
;;; # Clojure State
;;; 
;;; ### Identity, Value, State
;;; 
;;; * Identity - a logical entity: wife, today, employer, shipping address
;;; * Value - immutable data: 5, "Bob", [0 1 2]
;;; * State - the value of an identity at a given time
;;; 
;;; Example: "today is April 15, 2015"
;;; * identity = today
;;; * value = April 15, 2015
;;; * state of today = April 15, 2015
;;; 
;;; ### OOP with mutable state
;;; 
;;; * Identities are pointers to locations in memory
;;; * Objects changed in-place
;;; * Updating state means overwriting memory
;;;   * Need to protect reads/writes
;;;   * No language support for coordinating changes
;;;   * Locking, deadlocks, and pain
;;;   
;;; ### Clojure's approach
;;; 
;;; * Identities are managed references to immutable values
;;; * Nothing changed in-place
;;; * References provide read/write protection
;;; * Language-level constructs to manage change
;;; * No locking in user code
;;; 
;;; ## Conceptual Model
;;; 
;;; ### Modeling Change
;;; 
;;; TODO: pictures
;;; 
;;; ### Uniform State Transition Model
;;; 
;;; * `(change-fn reference function args*)`
;; **

;; @@

;; @@

;; **
;;; 
;; **
