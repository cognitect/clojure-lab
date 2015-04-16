;; gorilla-repl.fileformat = 1

;; **
;;; # State and Concurrency
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
;; **

;; **
;;; ## Conceptual Model
;;; 
;;; ### Modeling Change
;;; 
;;; ![Structure vs semantics](project-files/images/state1.png)
;;; 
;;; ![Structure vs semantics](project-files/images/state2.png)
;;; 
;;; ![Structure vs semantics](project-files/images/state3.png)
;;; 
;;; ![Structure vs semantics](project-files/images/state4.png)
;;; 
;;; ![Structure vs semantics](project-files/images/state5.png)
;;; 
;;; 
;;; 
;;; 
;;;     
;; **

;; **
;;; ### Uniform State Transition Model
;;; 
;;; * `(change-fn reference function args*)`
;;; 	* Calls `function` on old state + args
;;;     * Function returns new state
;;;     * Different `change-fn` functions for different behavior
;;; * `(deref reference)`
;;; 	* Shorthand: @reference  (this is called a "reader macro")
;;;     * Returns snapshot of current state
;;;     
;;; ### Mutable Reference Types
;;; 
;;; * Synchronous
;;; 	* Uncoordinated: Var, Atom
;;;     * Coordinated: Ref
;;; * Asynchronous
;;;     * Write-many: Agent
;;;     * Write-once: Future, Promise (not really reference types)
;;;     
;;; ## Atoms
;;; 
;;; Atoms are the simplest of the reference types - they allow us to share a stable identity for a single value across threads. Changes become visible to all threads at the same time.
;; **

;; @@
(def tick (atom 1))
;;=> #'user/tick

(deref tick)
;;=> 1

(swap! tick inc)
;;=> 2

@tick
;;=> 2
;; @@

;; **
;;; ### Atom Guarantees
;;; 
;;; * Change is *synchronous* on caller thread
;;; * Changes are *atomic*
;;; * No locking
;;; * No deadlocks
;;; 
;;; ### Atom Caveats
;;; 
;;; * Values must be immutable
;;; * Cannot atomically update more than one at a time
;;; * Spinning compare-and-set
;;; 	* Update functions should be quick
;;;     * Update function may be called more than once
;;;     * Must avoid side effects!
;;;    
;; **

;; **
;;; ## Refs
;;; 
;;; * Share identities across threads (like atoms)
;;; * Coordinated change among multiple identities (different than atoms)
;;; * Consistent view of the whole "ref" world
;; **

;; @@
(def a (ref 1))
(def b (ref 10))

(dosync
 (alter a inc)
 (alter b + 10))

@a  ;=> 2
@b  ;=> 20
;; @@

;; **
;;; 
;; **

;; **
;;; ### Ref Guarantees
;;; 
;;; * Change is *synchronous* on caller thread
;;; * Change can only occur in a transaction
;;; * Every transaction is *atomic* and *isolated* 
;;; * No locking in user code
;;; * Internal locking & deadlock prevention
;;; 
;;; ### Within a Transaction
;;; 
;;; * Consistent snapshot of "ref world" from point where transaction started
;;; * Transaction can see changes it has made
;;; 
;;; ### Ref Caveats
;;; 
;;; * Values must be immutable
;;; * Transactions are speculative
;;; 	* Body may be called more than once
;;;     * Must avoid side effects!
;;;     
;;; ### No Read Tracking
;;; 
;;; * Reading a Ref does not prevent other transactions from changing it
;;; * Transaction does not see changes made by other transactions
;;; * Enlist reads in transaction for read tracking with `ensure`
;;; 
;;; ### `ensure`
;;; 
;;; In this example, the fee may change during the life of the transaction and the transaction will only see the starting value.
;; **

;; @@
;; savings and fee are refs
(dosync
  (alter savings + @fee))
;; @@

;; **
;;; Use ensure to guarantee that if the fee changes, the transaction will fail and retry:
;; **

;; @@
(dosync
  (alter savings + (ensure fee)))
;; @@

;; **
;;; ### `commute`
;;; 
;;; * Like `alter`, but provides more concurrency
;;; * Use when order of updates doesn't matter
;;; * Just a performance optimization over `alter`
;;; 
;;; 
;;; 
;; **

;; @@
(def counter (ref 1))

(dosync (commute counter inc))

;; On another thread:
(dosync (commute counter + 30))
;; @@

;; **
;;; ## Agents
;;; 
;;; * Share identities across threads
;;; * Ensure an entity only does one thing at a time
;;; * Send messages between entities
;;; * Utilize all available CPU cores
;;; 
;;; ### Agent Usage
;;; 
;;; 
;; **

;; @@
(def a (agent 5))

(send a + 10)  ; returns immediately
@a  ; => still 5

;; Some time later:
@a  ; => 15
;; @@

;; **
;;; ### Atom Guarantees
;;; 
;;; * Action occurs *asynchronously* on thread pool
;;; * Action called exactly once
;;; * Only one action per Agent at a time
;;; * Sends during an action
;;; 	* Occur *after* state has been updated
;;; 	* Do not occur if action throws an exception
;;; 
;;; ### `send` vs `send-off`
;;; 
;;; * `send`
;;; 	* Fixed-size thread pool
;;;     * Actions should not block
;;;     * For CPU-bound tasks
;;; * `send-off`
;;; 	* Variable-sized thread pool
;;;     * Actions may block
;;;     * For IO-bound tasks
;;;     
;;; ### Agents are not actors
;;; 
;;; * In-process only
;;; * Point-in-time perception is free
;;; * Send functions, not messages
;;; 
;;; ### Agents and Transactions 
;;; 
;;; * Sends duringa transaction
;;; 	* An allowable side-effect
;;;     * Occur *after* transaction is committed
;;;     * Do not occur if transaction is aborted
;;; 
;;; ## Vars
;;; 
;;; * Thread-safe global identity
;;; * Optional dynamic scope/overrides
;;; * Binding overrides isolated to a single thread
;;; 
;;; ### Var Guarantees
;;; 
;;; * One global root binding
;;; 	* `alter-var-root` is *atomic* and *blocking*
;;; * With `^:dynamic`
;;; 	* Many thread-local bindings
;;;     * Thread-local assignments
;;;     * Dynamic scope
;;;     
;;; ### Var Usage
;;; 
;;; 
;; **

;; @@
(def foo 1)
;;=> #'user/foo

(alter-var-root #'foo inc)
;;=> 2

foo
;;=> 2
;; @@

;; **
;;; ### Dynamic Var Usage
;;; 
;; **

;; @@
(def ^:dynamic *foo* 5)  ; root binding is 5

(binding [*foo* 100]  ; thread-local binding
  (println *foo*)     ; => 100
  (set! *foo* 42)     ; thread-local assignment
  (println *foo*))    ; => 42

*foo*  ; root binding still 5
;; @@

;; **
;;; ## Uniform State Transition Model
;;; 
;;; 
;; **

;; @@
         (swap! an-atom  assoc :a "lucy")

 (dosync (alter a-ref    assoc :a "lucy"))

          (send an-agent assoc :a "lucy")

(alter-var-root #'a-var  assoc :a "lucy")
;; @@

;; **
;;; ### Setting state without depending on previous value
;;; 
;; **

;; @@
 (reset! an-atom  42)

(dosync (ref-set a-ref    42))

           (send an-agent (constantly 42))

 (alter-var-root #'a-var  (constantly 42))
;; @@

;; **
;;; ## Watches
;;; 
;;; * Get notified when a reference changes
;;; * Supported on Atom, Ref, Agent, and Var
;;; 
;;; ### Watch functions
;;; 
;;; * A callback function for when change occurs 
;;; * Arguments: key, reference, old, and new states
;;; 
;; **

;; @@
(defn my-watcher [key reference old-state new-state]
  (println "Watcher called for" key)
  (println "Old state:" old-state)
  (println "New state:" new-state))
;; @@

;; **
;;; ### `add-watch`
;;; 
;;; * Arguments: reference, key, watch function
;; **

;; @@
(add-watch r :foo my-watcher)

(dosync (alter r inc))
;; Watcher called for :foo
;; Old state: 42
;; New state: 43
;;=> 43
;; @@

;; **
;;; ## Futures
;;; 
;;; * Execute code in a background thread
;;; 	* Possibly with side effects
;;; * Block only when we ask for the result
;; **

;; @@
(def f (future
         ;; body of expressions
         ))
;; body starts on background thread

@f  ; blocks until complete, returns result
;; @@

;; **
;;; ## Promises
;;; 
;;; * Communicate a single value across threads
;;; * Deliver once from producer thread
;;; * Consumer(s) can wait until value becomes available
;;; 
;; **

;; @@
(def p (promise))

;; on another thread
(deliver p 42)

@p  ; blocks until delivered
;; @@

;; **
;;; ### Inverting async
;; **

;; @@
(def p (promise))

(future
  ;; do something that takes time
  (deliver p 42))

;; do something else for a while

@p  ; blocks until delivered
;; @@

;; **
;;; ## Concurrency and the JVM
;;; 
;;; * `java.util.concurrent` is available via interop
;;; 	* thread pools, locks, atomic, concurrent collections
;;;     * Clojure fns are `java.util.concurrent.Callable` and `java.lang.Runnable`
;;; * Clojure's concurrency features are thread-agnostic
;;; 	* Use futures/agents to create threads
;;;     * Or something else
;; **

;; **
;;; 
;;; ## Navigation
;;; 
;;; * [Up (Home)](/worksheet.html?filename=src/cljlab/start.clj)
;;; * [Previous (Polymorphism))](/worksheet.html?filename=src/cljlab/polymorphism.clj)
;;; 
;; **

;; **
;;; 
;; **
