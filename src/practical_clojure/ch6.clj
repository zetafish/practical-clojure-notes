(ns practical-clojure.ch6)

;;; State Management

;; State != Identity
;; Refs are implementation of synchronous coordinated identities

;; To create a ref
(def my-ref (ref 5))

;; so now we have a ref with initial state set to '5'
;; to see what the state of the ref is:
(deref my-ref)

;; we can also use a shorthand (looks like the * on pointers)
@my-ref

;; a deref is NEVER blocked, you just look at a snapshot. maybe if you
;; look later the state ahs changed and then another value will be
;; seen.

;; So how do I update a ref? Using transactions (STM) and tools to
;; change the state. Think of database interactions. You can only
;; modify the ref inside a transaction 
(dosync (ref-set my-ref 6))
(dosync (alter my-ref + 5))

;; It is important that the fn to alter is free from side effects
;; because the STM will retry the alter if it fails.

;; commute is another tool to update the state.

;; ensure is to guard that a ref is not changed during the transaction
;; and if it is, then STM will retry the transaction.

;; An example with bank accounts:
(def account1 (ref 1000))
(def account2 (ref 1500))

(defn transfer
  "transfer money from a to b"
  [a b amount]
  (dosync (alter a - amount)
          (alter b + amount)))

(transfer account1 account2 300)
(transfer account2 account1 50)
(println "Account #1" @account1)
(println "Account #2" @account2)

;; The bank account example is simple. The next one is more complex,
;; an address book:
(def contacts (ref []))

(defn add-contact
  [contacts contact]
  (dosync (alter contacts conj (ref contact))))

(defn print-contacts
  [contacts]
  (doseq [c @contacts]
    (println "Name: " (@c :fname)
             " Last name: " (@c :lname)
             " (" (@c :initials) ")")))

;; Now we want to add initials to all the contacts. Either all have an
;; initial or none.

(defn add-initials
  [contact]
  (assoc contact :initials (str (first (contact :fname))
                                (first (contact :lname)))))

(defn add-all-initials
  [contacts]
  (dosync
   (doseq [c (ensure contacts)]
     (alter c add-initials))))

;;; Atoms

;; Atoms are synchronous uncoordinated identities. Similar to Java
;; atomic package. But better. They are lock free. Reads never block
;; and write will retry in case of conflict.
(def my-atom (atom 5))

;; atoms are used like refs but the don't need the dosync. To update
;; the state of the atom use swap! or reset!
(swap! my-atom + 45)
(reset! my-atom 99)

;; Atoms are not used as frequently as refs. You use them when
;; identity is really independent of other identities in the system.
;; Some examples: cached values.

;;; Async Agents.

;; Agents are also identities but unlike refs and atoms, updates to
;; their state happen asynchronous. Agents introduce concurrency
;; without the headache

(def my-agent (agent 5))

;; You 'send' a fn to an agent so that it updates the state (at some
;; moment in time)
(send my-agent + 4)

;; You use 'send-off' if you expect agent to block on I/O. It is a
;; hint to the STM to optimise the concurrency.
(send-off my-agent / 99)

;; When update take place is not guaranteed. But we do have
;; guarantees:
;; - actions are applied serially (not parallel)
;; - actions by 1 thread are applied in order of send
;; - when agent sends next message, these are saved and sent after the
;; current action finishes.
;; - update to an agent in within a STM is sent only after transaction
;; commits

;; Because of async behaviour we need something to retrieve errors.
(set-error-mode! my-agent :continue)
(set-error-mode! my-agent :fail)
(error-mode my-agent)

;; we can set an error handler that will be called in case of errors.
(set-error-handler! my-agent (fn [a ex] (println ex)))

;; How to deal with an agent in failed state? Remember an agent gets
;; into a failed state when it's error mode is :fail and an error
;; occurs.
(def an-agent (agent 10))
(send an-agent / 0)
(send an-agent + 1)
(agent-error an-agent)

;; We need to restart the agent if we want him to be active
(restart-agent an-agent 5 :clear-actions true)

;; We can wait on agent to finish its work
(await an-agent)
(await-for 1000 an-agent)

;; At the end of the program (JVM going down) we shutdown all agents:
(shutdown-agents)

;;; Vars and local state. Clojure 1.4 requires ^:dynamic to declare a
;;; variable as dynamic. Only dynamic variables are allowed in the
;;; list of bindings
(def ^:dynamic x 5)
(def ^:dynamic y 9)
(defn zoo [] (+ x y))
(zoo)
(binding [x 12 y 900] (+ x y))

;;; Validators.

;; With a validator you place a guard on how the state of an identity
;; can be modified. It is a guarantee of commited values.
(def my-ref (ref 5))
(set-validator! my-ref (fn [x] (> x 0)))
(dosync (alter my-ref - 10)) 
(dosync (alter my-ref - 10) (alter my-ref + 15)) ;

;; You can also set a validator on an agent
(def my-agent (agent 5))
@my-agent
(set-validator! my-agent (fn [x] (> x 0)))
(set-validator! my-agent nil)
(send my-agent - 10)

;; And also atoms
(def my-atom (atom 5))
(set-validator! my-atom (fn [x] (< 0 x)))
(swap! my-atom - 4)

;; You can retrieve the validator
(get-validator my-atom)

;;; Watches

;; You can watch an identity and see that it gets changed.
(defn my-watch [key identity old new]
  (println "Old: " old ", New: " new))
(add-watch my-agent "w1" my-watch)
(remove-watch my-agent "w1")