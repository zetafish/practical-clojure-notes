(ns practical-clojure.ch11)

;;; Parallelism

;; Define an agent that keeps track of an average
(def my-average (agent {:nums [] :avg 0}))

;; Function to update average, to be used by the agent
(defn update [current n]
  (let [nn (conj (:nums current) n)]
    {:nums nn
     :avg (/ (reduce + nn) (count nn))}))

(send my-average update 1)

(defn make-heavy [f]
  (fn [& args]
    (Thread/sleep 1000)
    (apply f args)))

(time (+ 5 5)) ;=> milliseconds
(time ((make-heavy +) 5 5))

;; pmap is like map but with paralellism built in
(pmap inc [1 2 3 4 5])

;; To see the effect use a heavy function
(time (doall (map (make-heavy inc) [1 2 3 4 5])))  ;=> 5 second
(time (doall (pmap (make-heavy inc) [1 2 3 4 5]))) ;=> 1 second

;; pvalues produces lazy sequence of values processed in parallel
(pvalues (+ 5 5) (- 5 3) (* 2 4))

;; pcalls produces lazy sequence of calling no-arg functions
(pcalls #(+ 5 5) #(- 3 2))

;;; Futures and promisis

;; futures are started immediately on a new-thread
(def my-future (future (* 3 4 5)))
@my-future

(def my-future (future (Thread/sleep 10000)))
@my-future

;; There is also a future-call, same idea as pcalls vs pvalues

;; You can cancel a future
(future-cancel my-future)
(future-cancelled? my-future)
(future-done? my-future)
(future? my-future)

;; Promises. Are like a barrier. Thread blocks until the promise is
;; fulfilled.

;; You simply create a promise like this:
(def my-promise (promise))

;; You give it a value by delivering:
(deliver my-promise 5)

;; You dereference a promise with @
@my-promise

(def my-promise (promise))
(defn f [n p]
  (println "I am function #" n " and I see @promise= " @p))
(pvalues (f 1 my-promise)
         (f 2 my-promise)
         (f 3 my-promise))

;; !! Promises can put you in a deadlock (mutual promises)
;; !! Promises are too low level. Better is to use higher level
;; !! concurrency constructs

;; You can also use the Java threading. It is really easy because all
;; Clojure functions implement the 'Runnable' method.

(def value (atom 0))

(def my-thread (Thread. #(swap! value inc)))

;; Note that instead of making raw threads it is better to use the
;; executor framework. Also in java this is the better choice.